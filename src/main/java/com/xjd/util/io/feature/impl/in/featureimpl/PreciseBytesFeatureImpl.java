package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;

import com.xjd.util.io.feature.in.InputStreamFeature;

public class PreciseBytesFeatureImpl extends EOFFeatureImpl implements InputStreamFeature {

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int remain = len;
		while (remain > 0) {
			int i = super.read(b, off + (len - remain), remain);
			if (i == -1) {
				break;
			}
			remain -= i;
		}
		return len - remain;
	}

	@Override
	public long skip(long n) throws IOException {
		long remain = n;
		while (remain > 0) {
			long i = super.skip(remain);
			remain -= i;
			if (i == 0 && isEOF()) {
				break;
			}
		}
		return n - remain;
	}

}
