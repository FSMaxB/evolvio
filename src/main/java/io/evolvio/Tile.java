package io.evolvio;

class Tile extends GlobalScope {
	private static final int BARREN_COLOR = color(0, 0, 1);
	private static final int FERTILE_COLOR = color(0, 0, 0.2f);
	private static final int BLACK_COLOR = color(0, 1, 0);
	private static final int WATER_COLOR = color(0, 0, 0);
	private static final float FOOD_GROWTH_RATE = 1.0f;
	private static final float MAX_GROWTH_LEVEL = 1.0f;
	private final float climateType;
	float foodType;
	final float fertility;
	float foodLevel;
	private final int posX;
	private final int posY;

	public Tile(int x, int y, float f, float food, float type) {
		posX = x;
		posY = y;
		fertility = max(0, f);
		foodLevel = max(0, food);
		climateType = foodType = type;
	}

	public void drawTile(float scaleUp, boolean showEnergy) {
		stroke(0, 0, 0, 1);
		strokeWeight(2);
		int landColor = getColor();
		fill(landColor);
		rect(posX * scaleUp, posY * scaleUp, scaleUp, scaleUp);
		if (showEnergy) {
			if (brightness(landColor) >= 0.7f) {
				fill(0, 0, 0, 1);
			} else {
				fill(0, 0, 1, 1);
			}
			textAlign(CENTER);
			textFont(font(), 21);
			text(nf(100 * foodLevel, 0, 2) + " yums", (posX + 0.5f) * scaleUp, (posY + 0.3f) * scaleUp);
			text("Clim: " + nf(climateType, 0, 2), (posX + 0.5f) * scaleUp, (posY + 0.6f) * scaleUp);
			text("Food: " + nf(foodType, 0, 2), (posX + 0.5f) * scaleUp, (posY + 0.9f) * scaleUp);
		}
	}

	public void iterate(double timeStep, float growableTime) {
		if (fertility > 1) {
			foodLevel = 0;
		} else {
			if (growableTime > 0) {
				if (foodLevel < MAX_GROWTH_LEVEL) {
					double foodGrowthAmount = (MAX_GROWTH_LEVEL - foodLevel) * fertility * FOOD_GROWTH_RATE * timeStep * growableTime;
					addFood(foodGrowthAmount, climateType);
				}
			} else {
				foodLevel += MAX_GROWTH_LEVEL * foodLevel * FOOD_GROWTH_RATE * timeStep * growableTime;
			}
		}
		foodLevel = max(foodLevel, 0);
	}

	public void addFood(double amount, double addedFoodType) {
		foodLevel += amount;
		if (foodLevel > 0) {
			foodType += (addedFoodType - foodType) * (amount / foodLevel); // We're adding new plant growth, so we gotta "mix" the colors of the tile.
		}
	}

	public int getColor() {
		int foodColor = color(foodType, 1, 1);
		if (fertility > 1) {
			return WATER_COLOR;
		} else if (foodLevel < MAX_GROWTH_LEVEL) {
			return interColorFixedHue(interColor(BARREN_COLOR, FERTILE_COLOR, fertility), foodColor, foodLevel / MAX_GROWTH_LEVEL, hue(foodColor));
		} else {
			return interColorFixedHue(foodColor, BLACK_COLOR, 1.0f - MAX_GROWTH_LEVEL / foodLevel, hue(foodColor));
		}
	}

	public int interColor(int a, int b, float x) {
		float hue = inter(hue(a), hue(b), x);
		float sat = inter(saturation(a), saturation(b), x);
		float bri = inter(brightness(a), brightness(b), x); // I know it's dumb to do interpolation with HSL but oh well
		return color(hue, sat, bri);
	}

	public int interColorFixedHue(int a, int b, float x, float hue) {
		float satB = saturation(b);
		if (brightness(b) == 0) { // I want black to be calculated as 100% saturation
			satB = 1;
		}
		float sat = inter(saturation(a), satB, x);
		float bri = inter(brightness(a), brightness(b), x); // I know it's dumb to do interpolation with HSL but oh well
		return color(hue, sat, bri);
	}

	public float inter(float a, float b, float x) {
		return a + (b - a) * x;
	}
}
