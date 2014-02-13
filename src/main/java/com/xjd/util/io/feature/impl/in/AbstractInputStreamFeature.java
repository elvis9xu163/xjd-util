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

	private InputStreamInterceptor interceptor;

	public AbstractInputStreamFeature() {
		super(null);
	}

	public InputStream getSource() {
		return in;
	}

	public void setSource(InputStream source) {
		this.in = source;
	}

	/**
	 * <pre>
	 * 获取增强特性后的InputStream
	 * 注意该方法只有在增强的对象被使用过一次以后才能真正获取到，
	 * 若增强的对象尚未使用，则获取到的就是自身。
	 * 一般用于回调函数中。
	 * </pre>
	 * @return
	 */
	public InputStream getEnhancedInputStream() {
		if (interceptor != null && interceptor.getEnhancedInputStream() != null) {
			return interceptor.getEnhancedInputStream();
		}
		return this;
	}

	void setInputStreamInterceptor(InputStreamInterceptor interceptor) {
		this.interceptor = interceptor;
	}
	

}