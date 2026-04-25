package com.kalasetu.controller;

import com.kalasetu.model.Complaint;
import com.kalasetu.model.User;
import com.kalasetu.repository.ComplaintRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComplaintController {

    private final ComplaintRepository complaintRepository;

    public ComplaintController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @GetMapping("/complaint/new")
    public String newComplaint(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "complaint-form";
    }

    @PostMapping("/complaint/submit")
    public String submitComplaint(@RequestParam String subject, @RequestParam String message, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Complaint complaint = new Complaint(user, subject, message);
        complaintRepository.save(complaint);

        return "redirect:/profile?complaintSuccess";
    }
}
