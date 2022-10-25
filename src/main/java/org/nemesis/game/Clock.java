package org.nemesis.game;

public class Clock extends Thread {
	private long millis;
	private int nanos;

	public Clock(Runnable target, long millis, int nanos) {
		super(target, Clock.class.getName());
		assert target != null;
		setDaemon(true);
		this.millis = millis;
		this.nanos = nanos;
	}

	public Clock(Runnable target, long millis) {
		this(target, millis, 0);
	}

	public Clock(Runnable target) {
		this(target, 0l, 0);
	}

	@Override
	public final void run() {
		while (!isInterrupted()) {
			synchronized (this) {
				try {
					wait(millis, nanos);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
	}
}
