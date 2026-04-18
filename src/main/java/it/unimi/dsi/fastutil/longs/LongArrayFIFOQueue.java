package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.NoSuchElementException;

public class LongArrayFIFOQueue implements LongPriorityQueue, Serializable {
	private static final long serialVersionUID = 0L;
	public static final int INITIAL_CAPACITY = 4;
	protected transient long[] array;
	protected transient int length;
	protected transient int start;
	protected transient int end;

	public LongArrayFIFOQueue(int capacity) {
		if (capacity > 2147483638)
			throw new IllegalArgumentException("Initial capacity (" + capacity + ") exceeds " + 2147483638);
		if (capacity < 0)
			throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");

		this.array = new long[Math.max(1, capacity + 1)];
		this.length = this.array.length;
	}

	public LongArrayFIFOQueue() {
		this(4);
	}

	public LongComparator comparator() {
		return null;
	}

	public long dequeueLong() {
		if (this.start == this.end)
			throw new NoSuchElementException();
		long t = this.array[this.start];
		if (++this.start == this.length)
			this.start = 0;
		reduce();
		return t;
	}

	public long dequeueLastLong() {
		if (this.start == this.end)
			throw new NoSuchElementException();
		if (this.end == 0)
			this.end = this.length;
		long t = this.array[--this.end];
		reduce();
		return t;
	}

	private final void resize(int size, int newLength) {
		long[] newArray = new long[newLength];
		if (this.start >= this.end) {
			if (size != 0) {
				System.arraycopy(this.array, this.start, newArray, 0, this.length - this.start);
				System.arraycopy(this.array, 0, newArray, this.length - this.start, this.end);
			}
		} else {
			System.arraycopy(this.array, this.start, newArray, 0, this.end - this.start);
		}
		this.start = 0;
		this.end = size;
		this.array = newArray;
		this.length = newLength;
	}

	private final void expand() {
		resize(this.length, (int) Math.min(2147483639L, 2L * this.length));
	}

	private final void reduce() {
		int size = size();
		if (this.length > 4 && size <= this.length / 4)
			resize(size, this.length / 2);

	}

	public void enqueue(long x) {
		this.array[this.end++] = x;
		if (this.end == this.length)
			this.end = 0;
		if (this.end == this.start)
			expand();

	}

	public void enqueueFirst(long x) {
		if (this.start == 0)
			this.start = this.length;
		this.array[--this.start] = x;
		if (this.end == this.start)
			expand();

	}

	public long firstLong() {
		if (this.start == this.end)
			throw new NoSuchElementException();
		return this.array[this.start];
	}

	public long lastLong() {
		if (this.start == this.end)
			throw new NoSuchElementException();
		return this.array[((this.end == 0) ? this.length : this.end) - 1];
	}

	public void clear() {
		this.start = this.end = 0;
	}

	public void trim() {
		int size = size();
		long[] newArray = new long[size + 1];
		if (this.start <= this.end) {
			System.arraycopy(this.array, this.start, newArray, 0, this.end - this.start);
		} else {
			System.arraycopy(this.array, this.start, newArray, 0, this.length - this.start);
			System.arraycopy(this.array, 0, newArray, this.length - this.start, this.end);
		}

		this.start = 0;
		this.length = (this.end = size) + 1;
		this.array = newArray;
	}

	public int size() {
		int apparentLength = this.end - this.start;
		return (apparentLength >= 0) ? apparentLength : (this.length + apparentLength);
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		int size = size();
		s.writeInt(size);
		for (int i = this.start; size-- != 0;) {
			s.writeLong(this.array[i++]);
			if (i == this.length)
				i = 0;
		}
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		this.end = s.readInt();
		this.array = new long[this.length = HashCommon.nextPowerOfTwo(this.end + 1)];
		for (int i = 0; i < this.end;) {
			this.array[i] = s.readLong();
			i++;
		}

	}
}
