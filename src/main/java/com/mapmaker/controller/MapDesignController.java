package com.mapmaker.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mapmaker.entity.Pin;
import com.mapmaker.repository.PinRepository;

@Controller
public class MapDesignController {

    @Autowired
    private PinRepository pinRepository;
/*
    // 初期表示：登録済みピンの先頭5件
    @GetMapping("/map-app/design")
    public String showMapDesign(Model model) {
        List<Pin> pins = pinRepository.findTop5ByOrderByIdAsc();
        model.addAttribute("pins", pins);

        // JSON変換してクライアントに渡す
        try {
            ObjectMapper mapper = new ObjectMapper();
            String pinsJson = mapper.writeValueAsString(pins);
            model.addAttribute("pinsJson", pinsJson);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("pinsJson", "[]");
        }

        return "mapDesign";
    }
*/
    // POST送信：チェックされたピンIDを受け取って反映
    @PostMapping("/design")
    public String handleSelectedPins(
    		@RequestParam(name = "selectedPins", required = false) List<Integer> selectedPinIds,
            HttpServletRequest request,
            Model model) {

        if (selectedPinIds == null || selectedPinIds.isEmpty()) {
            // ピン未選択時の処理
            model.addAttribute("error", "ピンが選択されていません");
            return "redirect:/pins"; // 一覧画面に戻すなど
        }
        

        List<Integer> pinIds = new ArrayList<>();
        for (Integer id : selectedPinIds) {
            String param = request.getParameter("pinId_" + id); // hiddenの値を取得
            if (param != null) {
                pinIds.add(Integer.parseInt(param));
            }
        }
        List<Pin> pins = pinRepository.findByIds(pinIds);
        model.addAttribute("pins", pins);
        return "mapDesign"; // ⇒ mapDesign.jsp
    }
    @PostMapping("/map/capture")
    public ResponseEntity<String> saveMapImage(@RequestBody MapImageRequest request) {
        try {
            // Base64をファイルに変換
            String base64Image = request.getImage().replace("data:image/png;base64,", "");
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            Path filePath = Paths.get("uploads/map_" + System.currentTimeMillis() + ".png");
            Files.write(filePath, imageBytes);

            return ResponseEntity.ok("保存完了");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("保存失敗");
        }
    }

    public static class MapImageRequest {
        private String image;
        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }
    }
}
