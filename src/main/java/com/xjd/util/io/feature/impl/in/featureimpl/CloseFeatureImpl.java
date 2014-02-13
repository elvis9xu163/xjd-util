package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.xjd.util.io.feature.impl.in.AbstractInputStreamFeature;
import com.xjd.util.io.feature.in.CloseFeature;

public class CloseFeatureImpl extends AbstractInputStreamFeature implements CloseFeature {
    protected int mode = DELEGATE_TO_SOURCE;
    protected boolean closeSource = true;
    protected volatile boolean closed = false;
    protected Collection<CloseListener> lsnrs = null;

    protected void checkMode(int mode) {
	switch (mode) {
	case DELEGATE_TO_SOURCE:
	case RETURN_MINUS_1:
	case THROW_EXCEPTION:
	    break;
	default:
	    throw new IllegalArgumentException("mode[" + mode + "] is invalid.");
	}
    }

    @Override
    public void setMode(int mode) {
	if (getMode() != mode) {
	    checkMode(mode);
	    this.mode = mode;
	}
    }

    @Override
    public int getMode() {
	return mode;
    }

    @Override
    public void setCloseSource(boolean close) {
	if (close != getCloseSource()) {
	    closeSource = close;
	}
    }

    @Override
    public boolean getCloseSource() {
	return closeSource;
    }

    @Override
    public boolean isClosed() {
	return closed;
    }

    protected CloseFeature getThis() {
	return (CloseFeature) getEnhancedInputStream();
    }

    protected void fireClose() {
	if (lsnrs != null && isClosed()) {
	    for (CloseListener listener : lsnrs) {
		listener.onClose(getThis());
	    }
	    lsnrs = null;
	}
    }

    @Override
    public void addCloseListener(CloseListener listener) {
	if (isClosed()) {
	    listener.onClose(getThis());
	    return;
	}
	if (lsnrs == null) {
	    lsnrs = new ArrayList<CloseFeature.CloseListener>();
	}
	lsnrs.add(listener);
    }

    @Override
    public void removeCloseListener(CloseListener listener) {
	if (lsnrs != null) {
	    lsnrs.remove(listener);
	}
    }

    protected boolean checkClosed() throws IOException {
	if (isClosed()) {
	    if (getMode() == THROW_EXCEPTION) {
		throw new IOException("stream has been closed.");
	    }
	    return true;
	}
	return false;
    }

    @Override
    public int read() throws IOException {
	if (getMode() == DELEGATE_TO_SOURCE || !checkClosed()) {
	    return super.read();
	}
	return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
	if (getMode() == DELEGATE_TO_SOURCE || !checkClosed()) {
	    return super.read(b, off, len);
	}
	return -1;
    }

    @Override
    public int available() throws IOException {
	if (getMode() == DELEGATE_TO_SOURCE || !checkClosed()) {
	    return super.available();
	}
	return 0;
    }

    @Override
    public long skip(long n) throws IOException {
	if (getMode() == DELEGATE_TO_SOURCE || !checkClosed()) {
	    return super.skip(n);
	}
	return 0;
    }

    @Override
    public synchronized void close() throws IOException {
	if (isClosed()) {
	    return;
	}
	closed = true;
	if (getCloseSource()) {
	    super.close();
	}
	fireClose();
    }

}
