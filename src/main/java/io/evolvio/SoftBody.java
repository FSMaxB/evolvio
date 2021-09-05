package io.evolvio;

import java.util.ArrayList;

class SoftBody extends GlobalScope {
	private static final float ENERGY_DENSITY = 1.0f / (0.2f * 0.2f * PI); //set so when a creature is of minimum size, it equals one.
	static final float FRICTION = 0.03f;
	private static final float COLLISION_FORCE = 0.1f;
	static final float FIGHT_RANGE = 2.0f;
	double px;
	double py;
	double vx;
	double vy;
	double energy;
	double density;
	double hue;
	final double saturation;
	final double brightness;
	final double birthTime;
	boolean isCreature = false;
	double fightLevel = 0;

	private int prevSBIPMinX;
	private int prevSBIPMinY;
	private int prevSBIPMaxX;
	private int prevSBIPMaxY;
	int SBIPMinX;
	int SBIPMinY;
	int SBIPMaxX;
	int SBIPMaxY;
	ArrayList<SoftBody> colliders;
	final Board board;

	SoftBody(double tpx, double tpy, double tvx, double tvy, double tenergy, double tdensity,
					double thue, double tsaturation, double tbrightness, Board tb, double bt) {
		px = tpx;
		py = tpy;
		vx = tvx;
		vy = tvy;
		energy = tenergy;
		density = tdensity;
		hue = thue;
		saturation = tsaturation;
		brightness = tbrightness;
		board = tb;
		setSBIP(false);
		setSBIP(false); // just to set previous SBIPs as well.
		birthTime = bt;
	}

	private void setSBIP(boolean shouldRemove) {
		double radius = getRadius() * FIGHT_RANGE;
		prevSBIPMinX = SBIPMinX;
		prevSBIPMinY = SBIPMinY;
		prevSBIPMaxX = SBIPMaxX;
		prevSBIPMaxY = SBIPMaxY;
		SBIPMinX = xBound((int) (Math.floor(px - radius)));
		SBIPMinY = yBound((int) (Math.floor(py - radius)));
		SBIPMaxX = xBound((int) (Math.floor(px + radius)));
		SBIPMaxY = yBound((int) (Math.floor(py + radius)));
		if (prevSBIPMinX != SBIPMinX || prevSBIPMinY != SBIPMinY ||
			prevSBIPMaxX != SBIPMaxX || prevSBIPMaxY != SBIPMaxY) {
			if (shouldRemove) {
				for (int x = prevSBIPMinX; x <= prevSBIPMaxX; x++) {
					for (int y = prevSBIPMinY; y <= prevSBIPMaxY; y++) {
						if (x < SBIPMinX || x > SBIPMaxX ||
							y < SBIPMinY || y > SBIPMaxY) {
							board.softBodiesInPositions[x][y].remove(this);
						}
					}
				}
			}
			for (int x = SBIPMinX; x <= SBIPMaxX; x++) {
				for (int y = SBIPMinY; y <= SBIPMaxY; y++) {
					if (x < prevSBIPMinX || x > prevSBIPMaxX ||
						y < prevSBIPMinY || y > prevSBIPMaxY) {
						board.softBodiesInPositions[x][y].add(this);
					}
				}
			}
		}
	}

	int xBound(int x) {
		return Math.min(Math.max(x, 0), board.boardWidth - 1);
	}

	int yBound(int y) {
		return Math.min(Math.max(y, 0), board.boardHeight - 1);
	}

	private double xBodyBound(double x) {
		double radius = getRadius();
		return Math.min(Math.max(x, radius), board.boardWidth - radius);
	}

	private double yBodyBound(double y) {
		double radius = getRadius();
		return Math.min(Math.max(y, radius), board.boardHeight - radius);
	}

	void collide() {
		colliders = new ArrayList<SoftBody>(0);
		for (int x = SBIPMinX; x <= SBIPMaxX; x++) {
			for (int y = SBIPMinY; y <= SBIPMaxY; y++) {
				for (int i = 0; i < board.softBodiesInPositions[x][y].size(); i++) {
					SoftBody newCollider = (SoftBody) board.softBodiesInPositions[x][y].get(i);
					if (!colliders.contains(newCollider) && newCollider != this) {
						colliders.add(newCollider);
					}
				}
			}
		}
		for (int i = 0; i < colliders.size(); i++) {
			SoftBody collider = colliders.get(i);
			float distance = dist((float) px, (float) py, (float) collider.px, (float) collider.py);
			double combinedRadius = getRadius() + collider.getRadius();
			if (distance < combinedRadius) {
				double force = combinedRadius * COLLISION_FORCE;
				vx += ((px - collider.px) / distance) * force / getMass();
				vy += ((py - collider.py) / distance) * force / getMass();
			}
		}
		fightLevel = 0;
	}

	 void applyMotions(double timeStep) {
		px = xBodyBound(px + vx * timeStep);
		py = yBodyBound(py + vy * timeStep);
		vx *= (1 - FRICTION / getMass());
		vy *= (1 - FRICTION / getMass());
		setSBIP(true);
	}

	void drawSoftBody(float scaleUp) {
		double radius = getRadius();
		stroke(0);
		strokeWeight(2);
		fill((float) hue, (float) saturation, (float) brightness);
		ellipseMode(RADIUS);
		ellipse((float) (px * scaleUp), (float) (py * scaleUp), (float) (radius * scaleUp), (float) (radius * scaleUp));
	}

	double getRadius() {
		if (energy <= 0) {
			return 0;
		} else {
			return Math.sqrt(energy / ENERGY_DENSITY / Math.PI);
		}
	}

	double getMass() {
		return energy / ENERGY_DENSITY * density;
	}
}
