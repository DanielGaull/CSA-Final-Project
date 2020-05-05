package com.dpSoftware.fp.util;

public class Camera {

	private double x, y;
	private double speed = 10;
	private double zoom = 1.0;
	private double zoomSpeed = 0.5;
	public Camera(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getSpeed() {
		return speed;
	}
	public double getZoom() {
		return zoom;
	}
	public double getZoomSpeed() {
		return zoomSpeed;
	}
	public void increaseX() {
		x += speed;
	}
	public void increaseY() {
		y += speed;
	}
	public void decreaseX() {
		x -= speed;
	}
	public void decreaseY() {
		y -= speed;
	}
	public void changeX(double amount) {
		x += amount;
	}
	public void changeY(double amount) {
		y += amount;
	}
	public void setX(double value) {
		x = value;
	}
	public void setY(double value) {
		y = value;
	}
	public void setSpeed(double value) {
		speed = value;
	}
	public void setZoom(double value) {
		zoom = value;
	}
	public void setZoomSpeed(double value) {
		zoomSpeed = value;
	}
	public void increaseZoom() {
		zoom += zoomSpeed;
	}
	public void decreaseZoom() {
		zoom -= zoomSpeed;
	}
	
	public Camera clone() {
		Camera newC = new Camera(this.x, this.y);
		newC.zoom = this.zoom;
		newC.zoomSpeed = this.zoomSpeed;
		return newC;
	}
}
