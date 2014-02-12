package com.xjd.util.io.feature.in;

import java.io.IOException;

/**
 * <pre>
 * 所有InputStream特性的基类
 * 该类是把InputStream中的所有方法定义提出来做成接口，
 * 被所有InputStream的特性继承，从而用户在使用过程中
 * 不需要强制类型转换为InputStream也能使用其所有方法。
 * </pre>
 * @see java.io.InputStream
 * @author elvis.xu
 * @since 2014-2-11
 */
public interface InputStreamFeature {
	
	int read() throws IOException;

	int read(byte b[]) throws IOException;

	int read(byte b[], int off, int len) throws IOException;

	long skip(long n) throws IOException;

	int available() throws IOException;

	void close() throws IOException;

	void mark(int readlimit);

	void reset() throws IOException;

	public boolean markSupported();
	
}
