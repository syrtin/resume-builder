package com.syrtin.dataprocessor.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class ResumePagesController {

    @GetMapping("resumes")
    public String addListPage() {
        return "resumes-profile";
    }

    @GetMapping("resumes/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        model.addAttribute("resumeId", id);
        return "resume-edit";
    }

    @GetMapping("resumes/new")
    public String showNewForm() {
        return "resume-new";
    }
}
