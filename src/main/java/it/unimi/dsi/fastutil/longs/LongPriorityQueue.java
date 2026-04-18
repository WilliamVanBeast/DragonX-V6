package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.PriorityQueue;

public interface LongPriorityQueue extends PriorityQueue<Long> {
	default long lastLong() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	default void enqueue(Long x) {
		enqueue(x.longValue());
	}

	@Deprecated
	default Long dequeue() {
		return Long.valueOf(dequeueLong());
	}

	@Deprecated
	default Long first() {
		return Long.valueOf(firstLong());
	}

	@Deprecated
	default Long last() {
		return Long.valueOf(lastLong());
	}

	void enqueue(long paramLong);

	long dequeueLong();

	long firstLong();

	LongComparator comparator();
}
