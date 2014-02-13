package com.xjd.util.io.feature.in;

/**
 * <pre>
 * 该类没有特别的方法
 * 在使用下列方法时, 会按给定的参数值进行精确读取或跳过, 直到流结尾:
 * 1. InputStream.read(byte[])
 * 2. InputStream.read(byte[],int,int)
 * 3. InputStream.skip(long)
 * 
 * </pre>
 * @author elvis.xu
 * @since 2014-2-13
 */
public interface PreciseBytesFeature extends InputStreamFeature {

}
