package com.mapmaker.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.mapmaker.entity.CapturedMapEntity;

@Component
public class CapturedMapRepository {

private final String url = "jdbc:postgresql://db:5432/map_app";
private final String user = "postgres";
private final String password = "9632";

    public int save(CapturedMapEntity map) {
        String sql = "INSERT INTO captured_maps (title, description, file_name) VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, map.getTitle());
            stmt.setString(2, map.getDescription());
            stmt.setString(3, map.getFileName());  // path → file_name へ対応

        	System.out.println("取得ファイル名："+map.getFileName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public CapturedMapEntity findById(int id) {
        String sql = "SELECT * FROM captured_maps WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CapturedMapEntity entity = new CapturedMapEntity();
                entity.setId(rs.getInt("id"));
                entity.setTitle(rs.getString("title"));
                entity.setDescription(rs.getString("description"));
                entity.setFileName(rs.getString("file_name"));  // ここも修正！
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public CapturedMapEntity findByFileName(String fileName) {
    	System.out.println("取得ファイル名："+fileName);
        String sql = "SELECT * FROM captured_maps WHERE file_name = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CapturedMapEntity entity = new CapturedMapEntity();
                entity.setId(rs.getInt("id"));
                entity.setFileName(rs.getString("file_name"));
                entity.setTitle(rs.getString("title"));
                entity.setDescription(rs.getString("description"));
                return entity;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
