package io.evolvio;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import processing.core.PConstants;
import processing.core.PGraphics;

public class ColorLearningTests implements WithAssertions {
	@Test
	void argbIsStoredAsARGB() {
		PGraphics graphics = new PGraphics();
		// NOTE: Although there is an ARGB constant, RGB is the correct one for the ARGB color mode!
		graphics.colorMode(PConstants.RGB, 1.0f);

		float red = byteToFloat(1);
		float green = byteToFloat(2);
		float blue = byteToFloat(3);
		float alpha = byteToFloat(4);

		int color = graphics.color(red, green, blue, alpha);

		assertThat(color & 0xFF).isEqualTo(floatToByte(blue));
		assertThat((color >> 8) & 0xFF).isEqualTo(floatToByte(green));
		assertThat((color >> 16) & 0xFF).isEqualTo(floatToByte(red));
		assertThat((color >> 24) & 0xFF).isEqualTo(floatToByte(alpha));
	}

	@Test
	void hsbIsStoredAsARGB() {
		PGraphics graphics = new PGraphics();
		graphics.colorMode(PConstants.HSB, 1.0f);

		float hue = 0.1f;
		float saturation = 0.2f;
		float brightness = 0.3f;
		float alpha = 0.5f;

		int color = graphics.color(hue, saturation, brightness, alpha);

		assertThat(color & 0xFF).isEqualTo(floatToByte(graphics.blue(color)));
		assertThat((color >> 8) & 0xFF).isEqualTo(floatToByte(graphics.green(color)));
		assertThat((color >> 16) & 0xFF).isEqualTo(floatToByte(graphics.red(color)));
		assertThat((color >> 24) & 0xFF).isEqualTo(floatToByte(graphics.alpha(color)));
	}

	@Test
	void hsbIsConvertedToRGBAsExpected() {
		PGraphics graphics = new PGraphics();
		graphics.colorMode(PConstants.HSB, 1.0f);

		int hueDegrees = 10;
		int saturationPercent = 20;
		int brightnessPercent = 30;

		float hue = hueDegrees / 360f;
		float saturation = saturationPercent / 100f;
		float brightness = brightnessPercent / 100f;
		float alpha = 0.4f;

		int color = graphics.color(hue, saturation, brightness, alpha);

		// should be 77, 64, 61 in RGB according to several online calculators
		assertThat(color & 0xFF).isEqualTo(61);

		// Seems like processing is rounding differently!
		assertThat((color >> 8) & 0xFF).isNotEqualTo(64);
		assertThat((color >> 8) & 0xFF).isEqualTo(63);

		// Seems like processing is rounding differently!
		assertThat((color >> 16) & 0xFF).isNotEqualTo(77);
		assertThat((color >> 16) & 0xFF).isEqualTo(76);

		assertThat((color >> 24) & 0xFF).isEqualTo(floatToByte(alpha));

	}

	@Test
	void zeroBrightnessIsBlack() {
		PGraphics graphics = new PGraphics();
		graphics.colorMode(PConstants.HSB, 1.0f);

		float hue = byteToFloat(10);
		float saturation = byteToFloat(20);
		float brightness = byteToFloat(0);
		float alpha = byteToFloat(50);

		int color = graphics.color(hue, saturation, brightness, alpha);

		assertThat(color & 0xFF).isZero();
		assertThat((color >> 8) & 0xFF).isZero();
		assertThat((color >> 16) & 0xFF).isZero();
		assertThat((color >> 24) & 0xFF).isEqualTo(floatToByte(alpha));
	}

	@Test
	void zeroSaturationIsGray() {
		PGraphics graphics = new PGraphics();
		graphics.colorMode(PConstants.HSB, 1.0f);

		float hue = byteToFloat(10);
		float saturation = byteToFloat(0);
		float brightness = byteToFloat(30);
		float alpha = byteToFloat(50);

		int color = graphics.color(hue, saturation, brightness, alpha);

		assertThat(color & 0xFF).isEqualTo(floatToByte(brightness));
		assertThat((color >> 8) & 0xFF).isEqualTo(floatToByte(brightness));
		assertThat((color >> 16) & 0xFF).isEqualTo(floatToByte(brightness));
		assertThat((color >> 24) & 0xFF).isEqualTo(floatToByte(alpha));
	}

	private float byteToFloat(int byteValue) {
		return byteValue / 255f;
	}

	private int floatToByte(float number) {
		return (int)(number * 255);
	}
}
