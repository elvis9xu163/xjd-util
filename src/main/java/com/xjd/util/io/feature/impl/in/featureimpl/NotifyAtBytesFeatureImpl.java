package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.xjd.util.io.feature.IOFeatureRuntimeException;
import com.xjd.util.io.feature.in.NotifyAtBytesFeature;

public class NotifyAtBytesFeatureImpl extends BytesCounterImpl implements NotifyAtBytesFeature {

	protected TreeSet<NotifySizeListener> notifyLsnrs = null;

	public int read() throws IOException {
		int i = super.read();
		if (i != -1) {
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
			fireNotifySize();
		}
		return i;
	}

	@Override
	public long skip(long n) throws IOException {
		long remain = getMinNotifySize() - getUsedBytes();
		n = n > remain ? remain : n;
		long l = super.skip(n);
		fireNotifySize();
		return l;
	}

	protected boolean notEmpty(Set set) {
		if (set != null && set.size() > 0) {
			return true;
		}
		return false;
	}

	protected long getMinNotifySize() {
		long min = Long.MAX_VALUE;
		if (notEmpty(notifyLsnrs)) {
			min = notifyLsnrs.first().getNotifySize();
		}
		return min;
	}

	protected void fireNotifySize() {
		while (notEmpty(notifyLsnrs)) {
			NotifySizeListener lsnr = notifyLsnrs.first();
			if (getUsedBytes() >= lsnr.getNotifySize()) {
				lsnr.onSize(this);
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
		if (notifyLsnrs == null) {
			notifyLsnrs = new TreeSet<NotifySizeListener>(new NotifySizeListenerComparator());
		}
		notifyLsnrs.add(listener);
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
