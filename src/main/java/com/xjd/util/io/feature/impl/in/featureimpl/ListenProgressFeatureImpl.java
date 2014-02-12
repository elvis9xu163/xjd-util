package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.xjd.util.io.feature.in.ListenProgressFeature;

public class ListenProgressFeatureImpl extends BytesCounterImpl implements ListenProgressFeature {

	protected List<ProgressListener> progressLsnrs = null;

	@Override
	public int read() throws IOException {
		int i = super.read();
		if (i != -1) {
			fireProgress(1);
		}
		return i;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int i = super.read(b, off, len);
		if (i != -1) {
			fireProgress(i);
		}
		return i;
	}

	@Override
	public long skip(long n) throws IOException {
		long l = super.skip(n);
		fireProgress(l);
		return l;
	}

	protected void fireProgress(long curBytes) {
		if (progressLsnrs != null) {
			for (ProgressListener listener : progressLsnrs) {
				listener.onProgress(this, getUsedBytes(), curBytes);
			}
		}
	}

	@Override
	public void addProgressListener(ProgressListener listener) {
		if (progressLsnrs == null) {
			progressLsnrs = new ArrayList<ProgressListener>();
		}
		progressLsnrs.add(listener);
	}

	@Override
	public void removeProgressListener(ProgressListener listener) {
		if (progressLsnrs == null) {
			progressLsnrs.remove(listener);
		}
	}
}
