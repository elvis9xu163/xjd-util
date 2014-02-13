package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.xjd.util.io.feature.IOFeatureRuntimeException;
import com.xjd.util.io.feature.impl.in.AbstractInputStreamFeature;
import com.xjd.util.io.feature.in.CountBytesFeature;

public class CountBytesFeatureImpl extends AbstractInputStreamFeature implements CountBytesFeature {
	protected volatile long readBytes;
	protected volatile long skippedBytes;

	protected List<ProgressListener> progressLsnrs = null;
	protected List<NotifySizeListener> notifyLsnrs = null;
	protected NotifySizeListenerComparator comparator = null;

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
	
	protected CountBytesFeature getThis() {
		return (CountBytesFeature) getEnhancedInputStream();
	}

	protected void fireProgress(long curBytes) {
		if (progressLsnrs != null) {
			CountBytesFeature targetObj = getThis();
			for (ProgressListener listener : progressLsnrs) {
				listener.onProgress(targetObj, getUsedBytes(), curBytes);
			}
		}
	}

	@Override
	public void addProgressListener(ProgressListener listener) {
		if (progressLsnrs == null) {
			progressLsnrs = new ArrayList<ProgressListener>();
		}
		//有必要先调用一次
		listener.onProgress(getThis(), getUsedBytes(), 0);
		progressLsnrs.add(listener);
	}

	@Override
	public void removeProgressListener(ProgressListener listener) {
		if (progressLsnrs == null) {
			progressLsnrs.remove(listener);
		}
	}

	protected long getMinNotifySize() {
		long min = Long.MAX_VALUE;
		if (notifyLsnrs != null && notifyLsnrs.size() > 0) {
			min = notifyLsnrs.get(0).getNotifySize();
		}
		return min;
	}

	protected void fireNotifySize() {
		CountBytesFeature targetObj = getThis();
		while (notifyLsnrs != null && notifyLsnrs.size() > 0) {
			NotifySizeListener lsnr = notifyLsnrs.get(0);
			if (getUsedBytes() >= lsnr.getNotifySize()) {
				lsnr.onSize(targetObj);
				notifyLsnrs.remove(lsnr);
			} else {
				break;
			}
		}
	}

	@Override
	public void addNotifySizeListener(NotifySizeListener listener) {
		if (listener == null) {
			throw new IOFeatureRuntimeException("Cannot add null.");
		}
		if (getUsedBytes() >= listener.getNotifySize()) {
			listener.onSize(getThis());
			return;
		}
		if (notifyLsnrs == null) {
			notifyLsnrs = new ArrayList<NotifySizeListener>();
			comparator = new NotifySizeListenerComparator();
		}
		notifyLsnrs.add(listener);
		Collections.sort(notifyLsnrs, comparator);
	}

	@Override
	public void removeNotifySizeListener(NotifySizeListener listener) {
		if (notifyLsnrs == null) {
			notifyLsnrs.remove(listener);
		}
	}

	protected class NotifySizeListenerComparator implements Comparator<NotifySizeListener> {

		@Override
		public int compare(NotifySizeListener o1, NotifySizeListener o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}
			if (o1.getNotifySize() == o2.getNotifySize()) {
				return 0;
			}
			if (o1.getNotifySize() < o2.getNotifySize()) {
				return -1;
			}
			return 1;
		}

	}

}
