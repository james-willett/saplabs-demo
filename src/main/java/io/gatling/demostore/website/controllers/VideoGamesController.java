package io.gatling.demostore.website.controllers;

import io.gatling.demostore.models.VideoGameRepository;

import io.gatling.demostore.models.data.VideoGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/videogame")
public class VideoGamesController {

    @Autowired
    private VideoGameRepository videoGameRepository;

    @GetMapping
    public String videogame(@PathVariable Integer id, Model model) {
        VideoGame videoGame = videoGameRepository.findById(id).get();

        // todo - fix this, it isn't right
        if (videoGame == null) {
            return "redirect:/error";
        }

        model.addAttribute("videoGame", videoGame);

        return "videoGame_details";
    }
}