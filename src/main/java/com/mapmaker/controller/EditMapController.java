package com.mapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mapmaker.entity.CapturedMapEntity;
import com.mapmaker.repository.CapturedMapRepository;

@Controller
@RequestMapping("/edit")
public class EditMapController {

    @Autowired
    private CapturedMapRepository capturedMapRepository;

    @GetMapping("/mapPins")
    public String showEditMapPins(@RequestParam("mapId") int mapId, Model model) {
        CapturedMapEntity map = capturedMapRepository.findById(mapId);
        if (map == null) {
            model.addAttribute("message", "指定された地図が見つかりませんでした。");
            return "capture/error";
        }

        model.addAttribute("map", map);
        return "capture/editMapPins"; // /WEB-INF/views/capture/editMapPins.jsp
    }
}
