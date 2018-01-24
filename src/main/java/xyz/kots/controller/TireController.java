package xyz.kots.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import xyz.kots.service.TireService;

import java.io.IOException;

/**
 * Created by kots on 23.01.2018.
 */

@Controller
public class TireController {

    @Autowired
    TireService tireService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tires", tireService.getTires());
        return "index";
    }

    @PostMapping("/")
    public String uploadFile(@RequestParam MultipartFile excelFile) throws IOException {
        tireService.saveFile(excelFile);
        return "redirect:/";
    }
}
