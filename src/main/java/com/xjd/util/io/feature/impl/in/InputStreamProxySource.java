package com.xjd.util.io.feature.impl.in;

import java.io.InputStream;

/**
 * <pre>
 * 用于从InputStream代理对象中获得被代理的InputStream
 * </pre>
 * @author elvis.xu
 * @since 2014-2-11
 */
public interface InputStreamProxySource {
	
	/**
	 * <pre>
	 * 返回被代理的InputStream
	 * </pre>
	 * @return
	 */
	InputStream getSource();

	/**
	 * <pre>
	 * 设置被代理的InputStream
	 * </pre>
	 * @param source
	 */
	void setSource(InputStream source);
	
}
