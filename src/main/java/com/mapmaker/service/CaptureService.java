package com.mapmaker.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapmaker.dto.CapturedPinDTO;
import com.mapmaker.entity.CapturedMapEntity;
import com.mapmaker.entity.MapPinPositionEntity;
import com.mapmaker.entity.Pin;
import com.mapmaker.repository.CapturedMapRepository;
import com.mapmaker.repository.MapPinPositionRepository;
import com.mapmaker.repository.PinRepository;

@Service
public class CaptureService {

    @Autowired
    private PinRepository pinRepository;
    
    @Autowired
    private CapturedMapRepository capturedMapRepository;

    @Autowired
    private MapPinPositionRepository mapPinPositionRepository;

    public List<Pin> getPinsByIds(List<Integer> ids) {
        return pinRepository.findByIds(ids);
    }

    public Map<String, Object> saveImage(MultipartFile file, HttpServletRequest request) throws IOException {
        String uploadDir = request.getServletContext().getRealPath("/static/uploads");
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = "map_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".png";
        File destination = new File(dir, fileName);
        file.transferTo(destination);

        String relativePath = "/map-app/static/uploads/" + fileName;
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("path", relativePath);
        return response;
    }

    public CapturedMapEntity saveMapAndPins(
            MultipartFile imageFile,
            String title,
            String detail,
            String pinPositionsJson,
            HttpServletRequest request
    ) throws IOException {

        // アップロード先ディレクトリ取得
        String uploadRoot = request.getServletContext().getRealPath("/static/uploads");
        Path uploadDir = Paths.get(uploadRoot);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // ファイル名作成して保存
        String fileName = "map_" + System.currentTimeMillis() + ".png";
        Path uploadPath = uploadDir.resolve(fileName);
        Files.copy(imageFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

        // Map情報をDB保存
        CapturedMapEntity imageEntity = new CapturedMapEntity();
        imageEntity.setTitle(title);
        imageEntity.setDescription(detail);
        imageEntity.setFileName(fileName);
        int savedId = capturedMapRepository.save(imageEntity);  // ID取得
        imageEntity.setId(savedId); // ← 戻しておくのが大事！

        // ピン座標（JSON文字列）をDTOリストに変換して保存
        ObjectMapper mapper = new ObjectMapper();
        List<CapturedPinDTO> pinList = Arrays.asList(
            mapper.readValue(pinPositionsJson, CapturedPinDTO[].class));

        for (CapturedPinDTO dto : pinList) {
            MapPinPositionEntity pos = new MapPinPositionEntity();
            pos.setMapId(savedId);
            pos.setPinId(dto.getPinId());
            pos.setX(dto.getX());
            pos.setY(dto.getY());
            mapPinPositionRepository.save(pos);
        }

        return imageEntity;
    }
    
    public Map<String, Object> deleteImage(String path, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String uploadDir = request.getServletContext().getRealPath("/static/uploads");
            Path inputPath = Paths.get(path);
            String fileName = inputPath.getFileName().toString();
            Path filePath = Paths.get(uploadDir, fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                response.put("status", "success");
                response.put("message", "画像が削除されました！");
            } else {
                response.put("status", "error");
                response.put("message", "画像が見つかりませんでした");
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "削除中にエラーが発生しました");
        }
        return response;
    }
    public CapturedMapEntity findByFileName(String path) {
        return capturedMapRepository.findByFileName(path); // パス末尾のファイル名で検索
    }
    
    public List<MapPinPositionEntity> findPinsByMapId(int mapId) {
        return mapPinPositionRepository.findByMapId(mapId);
    }

}
