package com.xjd.util.io.feature.impl.in;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.xjd.util.io.feature.in.InputStreamFeature;

/**
 * <pre>
 * 所有InputStream特性实现类的基类
 * </pre>
 * 
 * @author elvis.xu
 * @since 2014-2-12
 */
public abstract class AbstractInputStreamFeature extends FilterInputStream implements InputStreamFeature {

	public InputStream getSource() {
		return in;
	}

	public void setSource(InputStream source) {
		this.in = source;
	}

	public AbstractInputStreamFeature() {
		super(null);
	}

}