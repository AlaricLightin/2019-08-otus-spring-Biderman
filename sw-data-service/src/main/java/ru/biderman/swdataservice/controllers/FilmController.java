package ru.biderman.swdataservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.biderman.swdataservice.model.Film;
import ru.biderman.swdataservice.services.FilmDataService;

@Controller
@RequiredArgsConstructor
public class FilmController {
    private final FilmDataService dataService;

    @GetMapping("/")
    public String showStartPage(Model model) {
        model.addAttribute("id", "");
        model.addAttribute("showDetails", false);
        return "index";
    }

    @PostMapping("/")
    public String getFilmInfoById(@ModelAttribute("id") int id, Model model) {
        Film film = dataService.findById(id).orElse(null);
        model.addAttribute("id", String.valueOf(id));
        model.addAttribute("showDetails", true);
        model.addAttribute("film", film);
        return "index";
    }
}