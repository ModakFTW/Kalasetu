package com.kalasetu.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * ImageProtectionService
 *
 * Provides two IP-protection capabilities for KalaSetu product images:
 *
 *  1. WATERMARKING — Burns a semi-transparent diagonal text watermark
 *     ("© KalaSetu YYYY | Artist Name") onto the image at request time
 *     using Java2D / Graphics2D. No external libraries required.
 *
 *  2. PERCEPTUAL HASHING (pHash) — Computes a 64-bit perceptual hash
 *     of an image by scaling to 32×32 greyscale and applying a simplified
 *     DCT. The resulting hex string can be stored on the Product entity and
 *     compared via Hamming distance to detect visually similar duplicates.
 */
@Service
public class ImageProtectionService {

    private static final Logger log = Logger.getLogger(ImageProtectionService.class.getName());

    /** Static resources base path (resolved at runtime via classpath). */
    @Value("${spring.web.resources.static-locations:classpath:/static/}")
    private String staticLocation;

    // -------------------------------------------------------------------------
    // WATERMARKING
    // -------------------------------------------------------------------------

    /**
     * Load a product image from the static directory, burn the watermark onto
     * it, and return the resulting PNG bytes.
     *
     * @param imageUrl    the image URL stored on the Product (e.g. "/images/foo.jpg")
     * @param artistName  the artist's name to embed in the watermark text
     * @return watermarked image as PNG byte array, or {@code null} if the image
     *         cannot be read (caller should fall back to static redirect)
     */
    public byte[] generateWatermarkedImage(String imageUrl, String artistName) {
        if (imageUrl == null || imageUrl.isBlank()) return null;

        // Resolve path: strip leading "/" then look under target/classes/static/
        String relativePath = imageUrl.startsWith("/") ? imageUrl.substring(1) : imageUrl;
        File imageFile = resolveStaticFile(relativePath);

        if (imageFile == null || !imageFile.exists()) {
            log.warning("Image file not found for watermarking: " + relativePath);
            return null;
        }

        try {
            BufferedImage source = ImageIO.read(imageFile);
            if (source == null) {
                log.warning("ImageIO could not decode file (unsupported format?): " + imageFile.getName());
                return null;
            }

            BufferedImage watermarked = applyWatermark(source, artistName);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(watermarked, "png", baos);
            return baos.toByteArray();

        } catch (IOException e) {
            log.severe("Error processing image for watermark: " + e.getMessage());
            return null;
        }
    }

    private BufferedImage applyWatermark(BufferedImage original, String artistName) {
        int w = original.getWidth();
        int h = original.getHeight();

        // Convert to ARGB so we can composite alpha
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();

        // Draw original image
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawImage(original, 0, 0, null);

        // Build watermark text
        int year = LocalDate.now().getYear();
        String watermarkText = "© KalaSetu " + year + " | " + artistName;

        // Calculate font size relative to image size (roughly 2.5% of width, min 14px)
        int fontSize = Math.max(14, (int) (w * 0.025));
        Font font = new Font("SansSerif", Font.BOLD, fontSize);
        g.setFont(font);

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(watermarkText);

        // Diagonal angle (-30°) and centre the text diagonally
        AffineTransform saved = g.getTransform();
        g.translate(w / 2.0, h / 2.0);
        g.rotate(Math.toRadians(-30));

        // Draw shadow pass (dark, slightly offset)
        g.setColor(new Color(0, 0, 0, 80));
        g.drawString(watermarkText, -textWidth / 2 + 2, fontSize / 2 + 2);

        // Draw main watermark text (white, semi-transparent)
        g.setColor(new Color(255, 255, 255, 160));
        g.drawString(watermarkText, -textWidth / 2, fontSize / 2);

        g.setTransform(saved);

        // Draw a subtle bottom-right corner badge as a secondary mark
        String badge = "KalaSetu.in";
        int badgeFontSize = Math.max(10, fontSize - 4);
        g.setFont(new Font("SansSerif", Font.ITALIC, badgeFontSize));
        FontMetrics bfm = g.getFontMetrics();
        int badgeWidth = bfm.stringWidth(badge);
        int padding = (int) (w * 0.015);

        // Badge background pill
        int bx = w - badgeWidth - padding * 2;
        int by = h - badgeFontSize - padding * 2;
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(bx, by, badgeWidth + padding * 2, badgeFontSize + padding * 2, 8, 8);

        // Badge text
        g.setColor(new Color(255, 255, 255, 200));
        g.drawString(badge, bx + padding, by + padding + badgeFontSize - 2);

        g.dispose();
        return output;
    }

    // -------------------------------------------------------------------------
    // PERCEPTUAL HASHING (pHash)
    // -------------------------------------------------------------------------

    /**
     * Compute a 64-bit perceptual hash for an image file.
     * The hash is returned as a 16-character hex string.
     * Returns {@code null} if the file cannot be read.
     *
     * Two images are "visually similar" when their Hamming distance is ≤ 10.
     */
    public String computeHash(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return null;
        String relativePath = imageUrl.startsWith("/") ? imageUrl.substring(1) : imageUrl;
        File imageFile = resolveStaticFile(relativePath);
        if (imageFile == null || !imageFile.exists()) return null;

        try {
            BufferedImage source = ImageIO.read(imageFile);
            if (source == null) return null;
            return computePHash(source);
        } catch (IOException e) {
            log.warning("pHash computation failed for " + imageUrl + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Simplified DCT-based perceptual hash (8×8 DCT on a 32×32 grayscale image).
     * Returns a 16-char hex string representing a 64-bit hash.
     */
    private String computePHash(BufferedImage image) {
        final int SIZE = 32;
        final int DCT_SIZE = 8;

        // Step 1: scale to 32×32 greyscale
        BufferedImage small = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = small.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, SIZE, SIZE, null);
        g.dispose();

        // Step 2: build greyscale float matrix
        double[][] pixels = new double[SIZE][SIZE];
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                pixels[y][x] = small.getRaster().getSample(x, y, 0);
            }
        }

        // Step 3: 2D DCT (top-left 8×8 block)
        double[][] dct = applyDCT(pixels, SIZE, DCT_SIZE);

        // Step 4: compute mean of the 64 DCT values (excluding [0][0])
        double total = 0;
        for (int y = 0; y < DCT_SIZE; y++) {
            for (int x = 0; x < DCT_SIZE; x++) {
                if (x == 0 && y == 0) continue;
                total += dct[y][x];
            }
        }
        double mean = total / (DCT_SIZE * DCT_SIZE - 1);

        // Step 5: build 64-bit hash: 1 if above mean, 0 if not
        long hash = 0;
        for (int y = 0; y < DCT_SIZE; y++) {
            for (int x = 0; x < DCT_SIZE; x++) {
                hash = (hash << 1) | (dct[y][x] > mean ? 1L : 0L);
            }
        }

        return Long.toHexString(hash);
    }

    private double[][] applyDCT(double[][] pixels, int n, int m) {
        double[][] dct = new double[m][m];
        for (int u = 0; u < m; u++) {
            for (int v = 0; v < m; v++) {
                double sum = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        sum += pixels[i][j]
                                * Math.cos((2 * i + 1) * u * Math.PI / (2.0 * n))
                                * Math.cos((2 * j + 1) * v * Math.PI / (2.0 * n));
                    }
                }
                double cu = (u == 0) ? 1.0 / Math.sqrt(2) : 1.0;
                double cv = (v == 0) ? 1.0 / Math.sqrt(2) : 1.0;
                dct[u][v] = (2.0 / n) * cu * cv * sum;
            }
        }
        return dct;
    }

    /**
     * Compute Hamming distance between two hex hash strings.
     * Distance ≤ 10 typically indicates visually similar images.
     */
    public int hammingDistance(String hashA, String hashB) {
        if (hashA == null || hashB == null) return Integer.MAX_VALUE;
        long a = Long.parseUnsignedLong(hashA, 16);
        long b = Long.parseUnsignedLong(hashB, 16);
        return Long.bitCount(a ^ b);
    }

    // -------------------------------------------------------------------------
    // HELPERS
    // -------------------------------------------------------------------------

    /**
     * Resolve a relative path (e.g. "images/foo.jpg") to a File on disk.
     * Checks the compiled classes static dir first, then the source tree.
     */
    private File resolveStaticFile(String relativePath) {
        // Primary: compiled output (works when running via mvn spring-boot:run)
        String[] candidates = {
            "target/classes/static/" + relativePath,
            "src/main/resources/static/" + relativePath
        };
        for (String candidate : candidates) {
            File f = new File(candidate);
            if (f.exists()) return f;
        }
        return null;
    }
}
