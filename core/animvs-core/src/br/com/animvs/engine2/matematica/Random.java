package br.com.animvs.engine2.matematica;

import com.badlogic.gdx.math.MathUtils;

public abstract class Random {
	private static MersenneTwister random;

	private Random() {
	}

	public static final void setSeed(long seed) {
		checaInicializacao();
		random.setSeed(seed);
	}

	/**
	 * Returns a random number between 0 (inclusive) and the specified value
	 * (exclusive).
	 */
	static public final float random(float range) {
		checaInicializacao();
		return random.nextFloat() * range;
	}

	/** Returns a random number between start (inclusive) and end (exclusive). */
	public static final float random(float start, float end) {
		checaInicializacao();
		return start + random.nextFloat() * (end - start);
	}

	/** Returns a random boolean value. */
	static public final boolean randomBoolean() {
		checaInicializacao();
		return random.nextBoolean();
	}

	/** Returns random number between 0.0 (inclusive) and 1.0 (exclusive). */
	static public final float random() {
		checaInicializacao();
		return random.nextFloat();
	}

	/**
	 * Returns true if a random value between 0 and 1 is less than the specified
	 * value.
	 */
	static public final boolean randomBoolean(float chance) {
		checaInicializacao();
		return MathUtils.random() < chance;
	}

	/**
	 * Returns a random number between 0 (inclusive) and the specified value
	 * (inclusive).
	 */
	static public final int random(int range) {
		checaInicializacao();
		return random.nextInt(range + 1);
	}

	/** Returns a random number between start (inclusive) and end (inclusive). */
	static public final int random(int start, int end) {
		checaInicializacao();
		return start + random.nextInt(end - start + 1);
	}

	static private final void checaInicializacao() {
		if (random == null)
			random = new MersenneTwister();
	}
}
