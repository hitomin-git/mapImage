package com.mapmaker.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.mapmaker.entity.Pin;

@Component
public class PinRepository {

private final String url = "jdbc:postgresql://db:5432/map_app";
private final String user = "postgres";
private final String password = "9632";


    public void insertPin(Pin pin) {
    	try {
    	    Class.forName("org.postgresql.Driver");
    	    System.out.println("✅ PostgreSQL Driver is loaded!");
    	} catch (ClassNotFoundException e) {
    	    System.out.println("❌ PostgreSQL Driver not found!");
    	    e.printStackTrace();
    	}
//    	Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
//    	while (drivers.hasMoreElements()) {
//    	    System.out.println("▶️ Registered Driver: " + drivers.nextElement().getClass().getName());
//    	}

    	String sql = "INSERT INTO pins ("
    			+ "name, "
    			+ "description, "
    			+ "category, "
    			+ "latitude, "
    			+ "longitude, "
    			+ "address) "
    			+ "VALUES (?, ?, ?, ?, ?, ?)";


        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

        	stmt.setString(1, pin.getName());
        	stmt.setString(2, pin.getDescription());
        	stmt.setString(3, pin.getCategory());
        	stmt.setDouble(4, pin.getLatitude());
        	stmt.setDouble(5, pin.getLongitude());
            stmt.setString(6, pin.getAddress());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // 必要に応じてログに切り替えてもOK
        }
    }
	// 一覧取得用
	public List<Pin> findAll() {
    	try {
    	    Class.forName("org.postgresql.Driver");
    	    System.out.println("✅ PostgreSQL Driver is loaded!");
    	} catch (ClassNotFoundException e) {
    	    System.out.println("❌ PostgreSQL Driver not found!");
    	    e.printStackTrace();
    	}
		List<Pin> pins = new ArrayList<>();
		String sql = "SELECT * FROM pins ORDER BY id";

		try (Connection conn = DriverManager.getConnection(url, user, password);
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Pin pin = new Pin();
				pin.setId(rs.getLong("id"));
				pin.setName(rs.getString("name"));
				pin.setDescription(rs.getString("description"));
				pin.setCategory(rs.getString("category"));
				pin.setLatitude(rs.getDouble("latitude"));
				pin.setLongitude(rs.getDouble("longitude"));
                pin.setAddress(rs.getString("address"));

				pins.add(pin);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pins;
	}

	public List<Pin> findByIds(List<Integer> ids) {
	    List<Pin> pins = new ArrayList<>();
	    if (ids == null || ids.isEmpty()) return pins;

	    String placeholders = ids.stream().map(id -> "?").collect(Collectors.joining(","));
	    String sql = "SELECT * FROM pins WHERE id IN (" + placeholders + ")";

	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        for (int i = 0; i < ids.size(); i++) {
	            stmt.setInt(i + 1, ids.get(i));
	        }

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            Pin pin = new Pin();
	            pin.setId(rs.getLong("id"));
	            pin.setName(rs.getString("name"));
	            pin.setDescription(rs.getString("description"));
	            pin.setCategory(rs.getString("category"));
	            pin.setLatitude(rs.getDouble("latitude"));
	            pin.setLongitude(rs.getDouble("longitude"));
	            pin.setAddress(rs.getString("address"));
	            pins.add(pin);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return pins;
	}

}
