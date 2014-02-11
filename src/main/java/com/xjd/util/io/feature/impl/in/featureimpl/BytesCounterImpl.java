package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.xjd.util.io.feature.in.BytesCounter;

public class BytesCounterImpl extends AbstractInputStreamFeature implements BytesCounter {
	protected volatile long readBytes;
	protected volatile long skippedBytes;

	protected List<ProgressListener> progressLsnrs = null;
	protected List<NotifySizeListener> reachLsnrs = null;

	public BytesCounterImpl(InputStream in) {
		super(in);
	}
	
	public BytesCounterImpl(InputStream in, int size) {
		super(in, size);
	}

	@Override
	public long getReadBytes() {
		return readBytes;
	}

	@Override
	public long getSkippedBytes() {
		return skippedBytes;
	}

	@Override
	public long getUsedBytes() {
		return getReadBytes() + getSkippedBytes();
	}

	@Override
	public void reviseReadBytes(long l) {
		readBytes = l;
	}

	@Override
	public void reviseSkippedBytes(long l) {
		skippedBytes = l;
	}

	@Override
	public int read() throws IOException {
		int i = super.read();
		if (i != -1) {
			readBytes++;
			fireProgress(1);
			fireNotifySize();
		}
		return i;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		long remain = getMinNotifySize() - getUsedBytes();
		len = len > remain ? (int) remain : len;
		int i = super.read(b, off, len);
		if (i != -1) {
			readBytes += i;
			fireProgress(i);
			fireNotifySize();
		}
		return i;
	}

	@Override
	public long skip(long n) throws IOException {
		long remain = getMinNotifySize() - getUsedBytes();
		n = n > remain ? remain : n;
		long l = super.skip(n);
		skippedBytes += l;
		fireProgress(l);
		fireNotifySize();
		return l;
	}

	@Override
	public void unread(int b) throws IOException {
		super.unread(b);
		readBytes--;
		fireProgress(-1);
	}

	@Override
	public void unread(byte[] b, int off, int len) throws IOException {
		super.unread(b, off, len);
		readBytes -= len;
		fireProgress(-len);
	}

	protected void fireProgress(long curBytes) {
		if (progressLsnrs != null) {
			for (ProgressListener listener : progressLsnrs) {
				listener.onProgress(this, getUsedBytes(), curBytes);
			}
		}
	}

	protected long getMinNotifySize() {
		long min = Long.MAX_VALUE;
		if (reachLsnrs != null) {
			for (NotifySizeListener listener : reachLsnrs) {
				if (listener.getNotifySize() < min) {
					min = listener.getNotifySize();
				}
			}
		}
		return min;
	}

	protected void fireNotifySize() {
		if (reachLsnrs != null) {
			List<NotifySizeListener> reachedList = new ArrayList<BytesCounter.NotifySizeListener>();
			for (NotifySizeListener listener : reachLsnrs) {
				if (getUsedBytes() >= listener.getNotifySize()) {
					listener.onReachSize(this);
					reachedList.add(listener);
				}
			}
			reachLsnrs.removeAll(reachedList);
		}
	}

	@Override
	public void addProgressListener(ProgressListener listener) {
		if (progressLsnrs == null) {
			progressLsnrs = new ArrayList<BytesCounter.ProgressListener>();
		}
		progressLsnrs.add(listener);
	}

	@Override
	public void removeProgressListener(ProgressListener listener) {
		if (progressLsnrs == null) {
			progressLsnrs.remove(listener);
		}
	}

	@Override
	public void addNotifySizeListener(NotifySizeListener listener) {
		if (reachLsnrs == null) {
			reachLsnrs = new ArrayList<BytesCounter.NotifySizeListener>();
		}
		reachLsnrs.add(listener);
	}

	@Override
	public void removeNotifySizeListener(NotifySizeListener listener) {
		if (reachLsnrs == null) {
			reachLsnrs.remove(listener);
		}
	}

}
