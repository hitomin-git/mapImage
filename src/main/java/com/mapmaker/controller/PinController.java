package com.mapmaker.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mapmaker.dto.PinDTO;
import com.mapmaker.entity.Pin;
import com.mapmaker.service.PinService;

@Controller
@RequestMapping("/pin")
public class PinController {

    @Autowired
    private PinService pinService;

    // マップ画面の表示
    @GetMapping("/pin-regist")
    public String showMap() {
        return "/pin/pinRegist";
    }

    // ピンの登録処理（DTO＋Serviceに委譲）
    @PostMapping("/pin-regist")
    public String registerPin(@ModelAttribute PinDTO pinDTO) {
        pinService.registerPin(pinDTO);
        return "/pin/pinRegist";
    }

    // ピン選択ページ表示（Service経由で取得）
    @GetMapping("/pin-select")
    public String showPinList(Model model,HttpServletRequest request) {
        String googleKey = System.getenv("GOOGLE_API_KEY");
        String mapboxKey = System.getenv("MAPBOX_API_KEY");

        request.setAttribute("googleKey", googleKey);
        request.setAttribute("mapboxKey", mapboxKey);

        List<Pin> pinList = pinService.getAllPins();
        model.addAttribute("pins", pinList);
        return "/pin/pinSelect";
    }
}
