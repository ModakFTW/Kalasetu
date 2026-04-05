import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class TestImageIO {
    public static void main(String[] args) {
        String[] files = {
            "blue glaze ug.jpg",
            "woven wall hanging.jpg",
            "mountain landscape painting.jpg",
            "hand carved walnut bowl.jpg",
            "teracotta vase.jpg",
            "geometric brass earisgns.jpg"
        };
        for (String f : files) {
            try {
                File file = new File("src/main/resources/static/images/" + f);
                BufferedImage img = ImageIO.read(file);
                if (img == null) {
                    System.out.println(f + " : NULL");
                } else {
                    System.out.println(f + " : OK -> " + img.getWidth() + "x" + img.getHeight());
                }
            } catch (Exception e) {
                System.out.println(f + " : ERROR -> " + e.getMessage());
            }
        }
    }
}
