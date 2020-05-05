package com.dpSoftware.fp.util;

public class Timer {

	private long timer;
	private long waitTime;
	private boolean running;
	
	public Timer(long waitTime) {
		this.waitTime = waitTime;
	}
	
	public void update(long passedTime) {
		timer += passedTime;
	}
	public boolean query() {
		if (timer >= waitTime) {
			reset();
			return true;
		}
		return false;
	}
	public void start() {
		if (!running) {
			reset();
			running = true;
		}
	}
	
	public long getTimer() {
		return timer;
	}
	public long getWaitTime() {
		return waitTime;
	}
	
	public void reset() {
		timer = 0;
		running = false;
	}
	public void reset(long waitTime) {
		this.waitTime = waitTime;
		reset();
	}
	
	public boolean isRunning() {
		return running;
	}
}
