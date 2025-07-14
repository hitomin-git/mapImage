package com.mapmaker.entity;

public class MapPinPositionEntity {
    private int id;
    private int mapId;
    private int pinId;
    private int x;
    private int y;

    // Getter / Setter
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getPinId() {
		return pinId;
	}
	public void setPinId(int pinId) {
		this.pinId = pinId;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}

