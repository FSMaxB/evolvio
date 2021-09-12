package io.evolvio.helpers;

import processing.core.PConstants;
import processing.core.PGraphics;

public class Color {
	final private static PGraphics graphics = new PGraphics();

	static {
		graphics.colorMode(PConstants.HSB, 1.0f);
	}

	public static int hsb(float hue, float saturation, float brightness) {
		return graphics.color(hue, saturation, brightness);
	}

	public static int hsb(float hue, float saturation, float brightness, float alpha) {
		return graphics.color(hue, saturation, brightness, alpha);
	}

	public static float brightness(int color) {
		return graphics.brightness(color);
	}

	public static float hue(int color) {
		return graphics.hue(color);
	}

	public static float saturation(int color) {
		return graphics.saturation(color);
	}
}
