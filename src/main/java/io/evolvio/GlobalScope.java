package io.evolvio;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

import static io.evolvio.helpers.Color.hsb;

public class GlobalScope {
	final public static int CODED = PConstants.CODED;
	final public static int CENTER = PConstants.CENTER;
	final public static int UP = PConstants.UP;
	final public static int DOWN = PConstants.DOWN;
	final public static int RIGHT = PConstants.RIGHT;
	final public static int LEFT = PConstants.LEFT;
	final public static int RADIUS = PConstants.RADIUS;
	final public static float PI = PConstants.PI;

	static float max(float a, float b) {
		return PApplet.max(a, b);
	}

	static float min(float a, float b) {
		return PApplet.min(a, b);
	}

	static float round(float n) {
		return PApplet.round(n);
	}

	static void setStrokeColor(int color) {
		Evolvio.app.stroke(color);
	}

	static void setStrokeColorHSB(float hue, float saturation, float brightness) {
		setStrokeColor(hsb(hue, saturation, brightness));
	}

	static void setStrokeColorHSB(float hue, float saturation, float brightness, float alpha) {
		setStrokeColor(hsb(hue, saturation, brightness, alpha));
	}

	static void strokeWeight(float weight) {
		Evolvio.app.strokeWeight(weight);
	}

	static void setFillColor(int color) {
		Evolvio.app.fill(color);
	}

	static void setFillColorHSB(float hue, float saturation, float brightness) {
		setFillColor(hsb(hue, saturation, brightness));
	}

	static void setFillColorHSB(float hue, float saturation, float brightness, float alpha) {
		setFillColor(hsb(hue, saturation, brightness, alpha));
	}

	static void rect(float a, float b, float c, float d) {
		Evolvio.app.rect(a, b, c, d);
	}

	static void textAlign(int alignX) {
		Evolvio.app.textAlign(alignX);
	}

	static void textFont(PFont which, float size) {
		Evolvio.app.textFont(which, size);
	}

	static void text(String str, float x, float y) {
		Evolvio.app.text(str, x, y);
	}

	static void text(String str, float x1, float y1, float x2, float y2) {
		Evolvio.app.text(str, x1, y1, x2, y2);
	}

	static String nf(float num, int left, int right) {
		return PApplet.nf(num, left, right);
	}

	static String nf(int num, int digits) {
		return PApplet.nf(num, digits);
	}

	static float dist(float x1, float y1, float x2, float y2) {
		return PApplet.dist(x1, y1, x2, y2);
	}

	static void ellipseMode(int mode) {
		Evolvio.app.ellipseMode(mode);
	}

	static void ellipse(float a, float b, float c, float d) {
		Evolvio.app.ellipse(a, b, c, d);
	}

	static void noStroke() {
		Evolvio.app.noStroke();
	}

	static void line(float x1, float y1, float x2, float y2) {
		Evolvio.app.line(x1, y1, x2, y2);
	}

	static void noFill() {
		Evolvio.app.noFill();
	}

	static void pushMatrix() {
		Evolvio.app.pushMatrix();
	}

	static void translate(float x, float y) {
		Evolvio.app.translate(x, y);
	}

	static void scale(float s) {
		Evolvio.app.scale(s);
	}

	static void rotate(float angle) {
		Evolvio.app.rotate(angle);
	}

	static void popMatrix() {
		Evolvio.app.popMatrix();
	}

	static float random(float low, float high) {
		return Evolvio.app.random(low, high);
	}

	static float atan2(float y, float x) {
		return PApplet.atan2(y, x);
	}

	static void noiseSeed(long seed) {
		Evolvio.app.noiseSeed(seed);
	}

	static void randomSeed(long seed) {
		Evolvio.app.randomSeed(seed);
	}

	static float pow(float n, float e) {
		return PApplet.pow(n, e);
	}

	static float noise(float x, float y) {
		return Evolvio.app.noise(x, y);
	}

	static float textWidth(String str) {
		return Evolvio.app.textWidth(str);
	}

	static float floor(float n) {
		return PApplet.floor(n);
	}

	static float cos(float angle) {
		return PApplet.cos(angle);
	}

	static void saveFrame(String filename) {
		Evolvio.app.saveFrame(filename);
	}

	static void saveStrings(String filename, String[] data) {
		Evolvio.app.saveStrings(filename, data);
	}

	static int mouseX() {
		return Evolvio.app.mouseX;
	}

	static int mouseY() {
		return Evolvio.app.mouseY;
	}

	static boolean keyPressed() {
		return Evolvio.app.keyPressed;
	}

	static char key() {
		return Evolvio.app.key;
	}

	static int keyCode() {
		return Evolvio.app.keyCode;
	}

	static PFont font() {
		return Evolvio.app.font;
	}
}
