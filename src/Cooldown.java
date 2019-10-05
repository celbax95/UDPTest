import java.io.Serializable;

/**
 * Un Cooldown
 */
public class Cooldown implements Serializable {

	private static final long serialVersionUID = 1L;
	private long freq;
	private long last;

	private Cooldown(Cooldown c) {
		this(c.freq, c.last);
	}

	/**
	 * @param freq : Delai pour le cooldown (ms)
	 */
	public Cooldown(long freq) {
		this(freq, System.currentTimeMillis());
	}

	/**
	 *
	 * @param freq : Delai pour le cooldown
	 * @param last : Derniere fois que le cooldown a ete appele et termine
	 */
	private Cooldown(long freq, long last) {
		assert freq >= 0;
		this.freq = freq;
		this.last = last;
	}

	@Override
	public Cooldown clone() {
		return new Cooldown(this);
	}

	/**
	 * @return frequence d'activation du Cooldown (ms)
	 */
	public long getFreq() {
		return this.freq;
	}

	/**
	 * @return dernier appel
	 */
	public long getLast() {
		return this.last;
	}

	/**
	 * @return le cooldown est termine
	 */
	public boolean isDone() {
		return this.last + this.freq <= System.currentTimeMillis();
	}

	/**
	 * Reset le cooldown
	 */
	public void reset() {
		this.last = System.currentTimeMillis();
	}

	/**
	 * Reset Le cooldown s'il est termine
	 *
	 * @return Cooldown termine
	 */
	public boolean resetOnDone() {
		if (this.last + this.freq <= System.currentTimeMillis()) {
			this.reset();
			return true;
		}
		return false;
	}

	/**
	 * Change la frequence du Cooldown
	 *
	 * @param freq : nouvelle frequence (ms)
	 */
	public void setFreq(long freq) {
		assert freq >= 0;
		this.freq = freq;
	}

	/**
	 * Lance le cooldown
	 */
	public void start() {
		this.last = System.currentTimeMillis();
	}

	/**
	 * @return temps avant que le Cooldown se termine
	 */
	public long timeBefore() {
		long tmp = System.currentTimeMillis();
		if (this.last + this.freq > tmp)
			return this.last + this.freq - tmp;
		else
			return 0;
	}

}
