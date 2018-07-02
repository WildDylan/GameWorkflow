package work.utils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.sql.Timestamp;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 2018/7/1
 * @author dylan.
 * Home: http://www.devdylan.cn
 */
public class MillisecondClock {
	private final long period;

	private final AtomicLong now;

	private MillisecondClock(long period) {
		this.period = period;
		this.now = new AtomicLong(System.currentTimeMillis());
		scheduleClockUpdating();
	}

	private static MillisecondClock instance() {
		return InstanceHolder.INSTANCE;
	}

	public static long now() {
		return instance().currentTimeMillis();
	}

	public static String nowDate() {
		return new Timestamp(instance().currentTimeMillis()).toString();
	}

	private void scheduleClockUpdating() {

		ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r, "System Clock");
				thread.setDaemon(true);
				return thread;
			}
		});

		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				now.set(System.currentTimeMillis());
			}
		}, period, period, TimeUnit.MILLISECONDS);
	}

	private long currentTimeMillis() {
		return now.get();
	}

	private static class InstanceHolder {
		public static final MillisecondClock INSTANCE = new MillisecondClock(1);
	}
}
