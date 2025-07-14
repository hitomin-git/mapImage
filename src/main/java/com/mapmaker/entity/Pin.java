package com.mapmaker.entity;

public class Pin {
    private Long id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private String category;
    private String address;

    // --- Getter & Setter ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getAddress() { return address;}
    public void setAddress(String address) { this.address = address; }
}
