package com.mapmaker.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mapmaker.service.ImageService;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Resource
    private ServletContext servletContext;

    @Autowired
    private ImageService imageService;

    // ピン選択後に呼ばれる
    @PostMapping("/image-select")
    public String showImageSelectPage(
        @RequestParam(value = "selectedPins", required = false) List<Integer> selectedPinIds,
        HttpSession session,
        Model model
    ) {
        if (selectedPinIds == null || selectedPinIds.isEmpty()) {
            model.addAttribute("error", "ピンが選択されていません");
            return "/pin/pinSelect";
        }

        // ✅ ピンID抽出ロジックをサービスへ
        List<Integer> pinIds = imageService.extractPinIds(selectedPinIds);
        session.setAttribute("pinIds", pinIds);

        // ✅ デモ画像の取得もサービスへ
        List<String> demoImages = imageService.getDemoImageList();
        model.addAttribute("demoImages", demoImages);

        return "/image/imageSelect";
    }

    // 保存画像一覧を表示
    @GetMapping("/image-listed")
    public String showSavedImages(Model model) {
        String uploadPath = servletContext.getRealPath("/static/uploads");
        List<String> imagePaths = imageService.getImagePaths(uploadPath);
        model.addAttribute("images", imagePaths);
        return "image/imageList";
    }
}
