
/**
 * Un minuteur
 */
public class Timer {

	private double lastTick;
	private long sec;

	public Timer() {
		this.lastTick = 0;
		this.sec = System.nanoTime();
	}

	/**
	 * @return Derniere fois que tick() a ete appele (milli-secondes)
	 */
	public double lastTickMS() {
		return this.lastTick / 1000000;
	}

	/**
	 * @return Derniere fois que tick() a ete appele (nano-secondes)
	 */
	public double lastTickNS() {
		return this.lastTick * 1000000;
	}

	/**
	 * @return Derniere fois que tick() a ete appele (secondes)
	 */
	public double lastTickS() {
		return this.lastTickMS() / 1000;
	}

	/**
	 * @return Derniere fois que tick() a ete appele (milli-secondes)
	 */
	public double tick() {
		long time = System.nanoTime();
		this.lastTick = System.nanoTime() - this.sec;
		this.sec = time;
		return this.lastTick;
	}
}