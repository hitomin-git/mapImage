package com.mapmaker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapmaker.entity.CapturedMapEntity;
import com.mapmaker.entity.MapPinPositionEntity;
import com.mapmaker.entity.Pin;
import com.mapmaker.service.CaptureService;

@Controller
@RequestMapping("/capture")
public class CaptureController {

    @Autowired
    private CaptureService captureService;

    // キャプチャ画面（ピン取得＋画像選択）
    @PostMapping("/capture-select")
    public String showMapCapturePage(
            @RequestParam("selectedImage") String selectedImage,
            HttpSession session,
            Model model,HttpServletRequest request) {
        String googleKey = System.getenv("GOOGLE_API_KEY");
        String mapboxKey = System.getenv("MAPBOX_API_KEY");

        request.setAttribute("googleKey", googleKey);
        request.setAttribute("mapboxKey", mapboxKey);

        @SuppressWarnings("unchecked")
        List<Integer> selectedPins = (List<Integer>) session.getAttribute("pinIds");

        List<Pin> pins = captureService.getPinsByIds(selectedPins);
        model.addAttribute("pins", pins);
        model.addAttribute("selectedImage", selectedImage);

        return "/capture/captureSelect";
    }

// キャプチャ結果表示ページ
@GetMapping("/capture-result")
public String confirmSavedImage(@RequestParam("path") String path, Model model) {

    // 画像ファイル名だけ抜き出す
    String fileName = path.substring(path.lastIndexOf("/") + 1);
    System.out.println("画像のファイル名：" + fileName);

    // 画像に対応するMap情報を取得
    CapturedMapEntity mapEntity = captureService.findByFileName(fileName);
    if (mapEntity == null) {
        model.addAttribute("error", "データが見つかりませんでした。");
        return "/capture/error";
    }

    // 🧭 ピン情報を取得（Map IDに紐づく座標一覧）
    List<MapPinPositionEntity> pins = captureService.findPinsByMapId(mapEntity.getId());

    // 🧳 Modelに詰める
    model.addAttribute("imagePath", path);                    // 画像表示用
    model.addAttribute("title", mapEntity.getTitle());        // タイトル
    model.addAttribute("detail", mapEntity.getDescription()); // 説明文
    model.addAttribute("pins", pins);                         // ピンリスト

    try {
        String json = new ObjectMapper().writeValueAsString(pins);
        model.addAttribute("pinPositionsJson", json);
    } catch (JsonProcessingException e) {
        e.printStackTrace(); // エラー内容を出力（あとで直せるようにログ残す）
    }
    
    return "/capture/captureResult"; // JSP側で表示するパス
}


    // 画像のアップロード処理
@PostMapping("/capture-saved")
@ResponseBody
public ResponseEntity<Map<String, Object>> saveCapturedMap(
        @RequestParam("image") MultipartFile image,
        @RequestParam("title") String title,
        @RequestParam("detail") String detail,
        @RequestParam("pinPositions") String pinPositionsJson,
        HttpServletRequest request
) {
    try {
        // Serviceに保存処理を委譲
        CapturedMapEntity savedImage = captureService.saveMapAndPins(
            image, title, detail, pinPositionsJson, request);

        // JSONで保存結果を返す
        Map<String, Object> response = new HashMap<>();
        response.put("path", "/map-app/static/uploads/" + savedImage.getFileName());
        response.put("title", savedImage.getTitle());
        response.put("detail", savedImage.getDescription());
        response.put("mapId", savedImage.getId());
        return ResponseEntity.ok(response);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


    // 画像の削除処理
    @PostMapping("/delete-image")
    @ResponseBody
    public Map<String, Object> deleteImage(@RequestParam("path") String path, HttpServletRequest request) {
        return captureService.deleteImage(path, request);
    }

    // メッセージ表示ページ
    @GetMapping("/success")
    public String successPage(@RequestParam("message") String message, Model model) {
        model.addAttribute("message", message);
        return "capture/success";
    }
}
