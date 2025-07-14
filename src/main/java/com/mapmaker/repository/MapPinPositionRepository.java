package com.mapmaker.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mapmaker.entity.MapPinPositionEntity;

@Component
public class MapPinPositionRepository {

private final String url = "jdbc:postgresql://db:5432/map_app";
private final String user = "postgres";
private final String password = "9632";


    public void save(MapPinPositionEntity pin) {
        String sql = "INSERT INTO map_pin_positions  (map_id, pin_id, x, y) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pin.getMapId());
            stmt.setInt(2, pin.getPinId());
            stmt.setInt(3, pin.getX());
            stmt.setInt(4, pin.getY());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<MapPinPositionEntity> findByMapId(int mapId) {
    List<MapPinPositionEntity> result = new ArrayList<>();
    String sql = "SELECT * FROM map_pin_positions WHERE map_id = ?";

    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, mapId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            MapPinPositionEntity entity = new MapPinPositionEntity();
            entity.setId(rs.getInt("id"));
            entity.setMapId(rs.getInt("map_id"));
            entity.setPinId(rs.getInt("pin_id"));
            entity.setX(rs.getInt("x"));
            entity.setY(rs.getInt("y"));
            result.add(entity);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return result;
}

}
