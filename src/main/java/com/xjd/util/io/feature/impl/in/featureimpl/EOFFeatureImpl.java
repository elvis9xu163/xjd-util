package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Collection;

import com.xjd.util.io.feature.impl.in.AbstractInputStreamFeature;
import com.xjd.util.io.feature.in.EOFFeature;
import com.xjd.util.io.feature.in.EOFFeature.EOFListener;

public class EOFFeatureImpl extends AbstractInputStreamFeature implements EOFFeature {
	protected boolean eof = false;
	protected Collection<EOFListener> lsnrs = null;
	protected InputStream realSource;
	protected PushbackInputStream pin;

	@Override
	public InputStream getSource() {
		return realSource;
	}

	@Override
	public void setSource(InputStream source) {
		realSource = source;
		pin = new PushbackInputStream(source);
		in = pin;
	}

	@Override
	public boolean isEOF() throws IOException {
		if (eof) {
			return true;
		}
		int i = pin.read();
		if (i == -1) {
			eof();
			return true;
		} else {
			pin.unread(i);
			return false;
		}
	}

	protected void eof() {
		eof = true;
		fireEOF();
	}

	protected EOFFeature getThis() {
		return (EOFFeature) getEnhancedInputStream();
	}

	protected void fireEOF() {
		if (lsnrs != null && eof) {
			for (EOFListener lsnr : lsnrs) {
				lsnr.onEOF(getThis());
			}
			lsnrs = null;
		}
	}

	@Override
	public void addEOFListener(EOFListener lsnr) {
		if (eof) {
			lsnr.onEOF(getThis());
			return;
		}
		if (lsnrs == null) {
			lsnrs = new ArrayList<EOFFeature.EOFListener>();
		}
		lsnrs.add(lsnr);
	}

	@Override
	public void removeEOFListener(EOFListener lsnr) {
		if (lsnrs != null) {
			lsnrs.remove(lsnr);
		}
	}

	@Override
	public int read() throws IOException {
		int i = super.read();
		if (i == -1) {
			eof();
		}
		return i;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int i = super.read(b, off, len);
		if (i == -1) {
			eof();
		}
		return i;
	}

}
