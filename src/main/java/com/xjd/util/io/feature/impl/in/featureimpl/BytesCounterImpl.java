package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.xjd.util.io.feature.impl.in.AbstractInputStreamFeature;
import com.xjd.util.io.feature.in.BytesCounter;

public class BytesCounterImpl extends AbstractInputStreamFeature implements BytesCounter {
	protected volatile long readBytes;
	protected volatile long skippedBytes;

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
		}
		return i;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int i = super.read(b, off, len);
		if (i != -1) {
			readBytes += i;
		}
		return i;
	}

	@Override
	public long skip(long n) throws IOException {
		long l = super.skip(n);
		skippedBytes += l;
		return l;
	}

}
