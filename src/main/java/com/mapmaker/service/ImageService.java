package com.mapmaker.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ImageService {

    // 🔽 hidden input の値を取り直さないスタイルに変更（selectedPinsを信頼）
    public List<Integer> extractPinIds(List<Integer> selectedPinIds) {
        return new ArrayList<>(selectedPinIds); // 今後フィルタ等があればここで処理
    }

    public List<String> getDemoImageList() {
        List<String> demoImages = new ArrayList<>();
        demoImages.add("/uploads/normal.png"); // TODO: 動的取得にするなら後日拡張
        return demoImages;
    }

    public List<String> getImagePaths(String uploadDirPath) {
        List<String> imagePaths = new ArrayList<>();
        File dir = new File(uploadDirPath);

        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".png")) {
                    System.out.println("出力対象：" + file.getName());
                    imagePaths.add("/map-app/static/uploads/" + file.getName());
                }
            }
        }

        return imagePaths;
    }
}
