package io.evolvio;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class GlobalScope {
	final public static int CODED = PConstants.CODED;
	final public static int CENTER = PConstants.CENTER;
	final public static int UP = PConstants.UP;
	final public static int DOWN = PConstants.DOWN;
	final public static int RIGHT = PConstants.RIGHT;
	final public static int LEFT = PConstants.LEFT;
	final public static int RADIUS = PConstants.RADIUS;
	final public static float PI = PConstants.PI;

	public static int color(float v1, float v2, float v3) {
		return Evolvio.app.color(v1, v2, v3);
	}

	public static int color(float v1, float v2, float v3, float alpha) {
		return Evolvio.app.color(v1, v2, v3, alpha);
	}

	public static float max(float a, float b) {
		return PApplet.max(a, b);
	}

	public static float min(float a, float b) {
		return PApplet.min(a, b);
	}

	public static float round(float n) {
		return PApplet.round(n);
	}

	public static void stroke(int color) {
		Evolvio.app.stroke(color);
	}

	public static void stroke(float v1, float v2, float v3) {
		Evolvio.app.stroke(v1, v2, v3);
	}

	public static void stroke(float v1, float v2, float v3, float alpha) {
		Evolvio.app.stroke(v1, v2, v3, alpha);
	}

	public static void strokeWeight(float weight) {
		Evolvio.app.strokeWeight(weight);
	}

	public static void fill(int color) {
		Evolvio.app.fill(color);
	}

	public static void fill(float v1, float v2, float v3) {
		Evolvio.app.fill(v1, v2, v3);
	}

	public static void fill(float v1, float v2, float v3, float alpha) {
		Evolvio.app.fill(v1, v2, v3, alpha);
	}

	public static void rect(float a, float b, float c, float d) {
		Evolvio.app.rect(a, b, c, d);
	}

	public static float brightness(int color) {
		return Evolvio.app.brightness(color);
	}

	public static void textAlign(int alignX) {
		Evolvio.app.textAlign(alignX);
	}

	public static void textFont(PFont which, float size) {
		Evolvio.app.textFont(which, size);
	}

	public static void text(String str, float x, float y) {
		Evolvio.app.text(str, x, y);
	}

	public static void text(String str, float x1, float y1, float x2, float y2) {
		Evolvio.app.text(str, x1, y1, x2, y2);
	}

	public static String nf(float num, int left, int right) {
		return PApplet.nf(num, left, right);
	}

	public static String nf(int num, int digits) {
		return PApplet.nf(num, digits);
	}

	public static float hue(int color) {
		return Evolvio.app.hue(color);
	}

	public static float saturation(int color) {
		return Evolvio.app.saturation(color);
	}

	public static float dist(float x1, float y1, float x2, float y2) {
		return PApplet.dist(x1, y1, x2, y2);
	}

	public static void ellipseMode(int mode) {
		Evolvio.app.ellipseMode(mode);
	}

	public static void ellipse(float a, float b, float c, float d) {
		Evolvio.app.ellipse(a, b, c, d);
	}

	public static void noStroke() {
		Evolvio.app.noStroke();
	}

	public static void line(float x1, float y1, float x2, float y2) {
		Evolvio.app.line(x1, y1, x2, y2);
	}

	public static void noFill() {
		Evolvio.app.noFill();
	}

	public static void pushMatrix() {
		Evolvio.app.pushMatrix();
	}

	public static void translate(float x, float y) {
		Evolvio.app.translate(x, y);
	}

	public static void scale(float s) {
		Evolvio.app.scale(s);
	}

	public static void rotate(float angle) {
		Evolvio.app.rotate(angle);
	}

	public static void popMatrix() {
		Evolvio.app.popMatrix();
	}

	public static float random(float low, float high) {
		return Evolvio.app.random(low, high);
	}

	public static float atan2(float x, float y) {
		return PApplet.atan2(x, y);
	}

	public static void noiseSeed(long seed) {
		Evolvio.app.noiseSeed(seed);
	}

	public static void randomSeed(long seed) {
		Evolvio.app.randomSeed(seed);
	}

	public static float pow(float n, float e) {
		return PApplet.pow(n, e);
	}

	public static float noise(float x, float y) {
		return Evolvio.app.noise(x, y);
	}

	public static float textWidth(String str) {
		return Evolvio.app.textWidth(str);
	}

	public static float floor(float n) {
		return PApplet.floor(n);
	}

	public static float cos(float angle) {
		return PApplet.cos(angle);
	}

	public static void saveFrame(String filename) {
		Evolvio.app.saveFrame(filename);
	}

	public static void saveStrings(String filename, String[] data) {
		Evolvio.app.saveStrings(filename, data);
	}

	public static int mouseX() {
		return Evolvio.app.mouseX;
	}

	public static int mouseY() {
		return Evolvio.app.mouseY;
	}

	public static boolean keyPressed() {
		return Evolvio.app.keyPressed;
	}

	public static char key() {
		return Evolvio.app.key;
	}

	public static int keyCode() {
		return Evolvio.app.keyCode;
	}

	public static PFont font() {
		return Evolvio.app.font;
	}
}
