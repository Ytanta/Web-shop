package ru.gb.controller;

import ru.gb.constants.Pages;
import ru.gb.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PerfumeService perfumeService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("perfumes", perfumeService.getPopularPerfumes());
        return Pages.HOME;
    }
}
