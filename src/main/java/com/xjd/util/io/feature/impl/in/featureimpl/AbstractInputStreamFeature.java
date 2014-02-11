package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import com.xjd.util.io.feature.impl.in.InputStreamProxySource;
import com.xjd.util.io.feature.in.InputStreamFeature;

public abstract class AbstractInputStreamFeature extends PushbackInputStream implements InputStreamFeature, InputStreamProxySource {

	public InputStream getSource() {
		return in;
	}

	public void setSource(InputStream source) {
		this.in = source;
	}

	public AbstractInputStreamFeature(InputStream in) {
		super(in);
	}

	public AbstractInputStreamFeature(InputStream in, int size) {
		super(in, size);
	}

}