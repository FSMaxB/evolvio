package io.evolvio;

class Axon {
	private static final double MUTABILITY_MUTABILITY = 0.7;
	private static final int MUTATE_POWER = 9;
	private static final double MUTATE_MULTI = Math.pow(0.5, MUTATE_POWER);

	final double weight;
	final double mutability;

	Axon(double w, double m) {
		weight = w;
		mutability = m;
	}

	Axon mutateAxon() {
		double mutabilityMutate = Math.pow(0.5, pmRan() * MUTABILITY_MUTABILITY);
		return new Axon(weight + r() * mutability / MUTATE_MULTI, mutability * mutabilityMutate);
	}

	private static double r() {
		return Math.pow(pmRan(), MUTATE_POWER);
	}

	private static double pmRan() {
		return (Math.random() * 2 - 1);
	}
}

