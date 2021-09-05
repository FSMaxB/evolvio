package de.maxbruckner.java;

import processing.core.PApplet;

/**
 * Hello world!
 */
public class App extends PApplet {
	public static void main(String[] args) {
		PApplet.main("de.maxbruckner.java.App");
	}

	@Override
	public void setup() {
		setSize(600, 600);
	}

	@Override
	public void draw() {
		background(255);
	}
}
