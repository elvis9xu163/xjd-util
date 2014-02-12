package com.xjd.util.io.feature.impl.in.featureimpl;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.xjd.util.io.feature.impl.in.ConstInputStream;
import com.xjd.util.io.feature.in.CountBytesFeature;
import com.xjd.util.io.feature.in.CountBytesFeature.NotifySizeListener;
import com.xjd.util.io.feature.in.CountBytesFeature.ProgressListener;

public class CountBytesFeatureImplTest {

	@Test
	public void test() throws IOException {
		InputStream in = new ConstInputStream();
		CountBytesFeatureImpl f = new CountBytesFeatureImpl();
		f.setSource(in);
		
		final Map<String, Long> rt = new HashMap<String, Long>();
		rt.put("pl", 0L);
		rt.put("pl2", 0L);
		rt.put("nl", 0L);
		rt.put("nl2", 0L);
		
		f.addProgressListener(new ProgressListener() {
			@Override
			public void onProgress(CountBytesFeature obj, long totallUsedBytes, long thisUsedBytes) {
				rt.put("pl", rt.get("pl") + thisUsedBytes);
			}
		});
		f.addProgressListener(new ProgressListener() {
			@Override
			public void onProgress(CountBytesFeature obj, long totallUsedBytes, long thisUsedBytes) {
				rt.put("pl2", rt.get("pl2") + thisUsedBytes);
			}
		});
		
		f.addNotifySizeListener(new NotifySizeListener() {
			@Override
			public void onSize(CountBytesFeature obj) {
				rt.put("nl", getNotifySize());
			}
			
			@Override
			public long getNotifySize() {
				return 4L;
			}
		});
		f.addNotifySizeListener(new NotifySizeListener() {
			@Override
			public void onSize(CountBytesFeature obj) {
				rt.put("nl2", getNotifySize());
			}
			
			@Override
			public long getNotifySize() {
				return 3L;
			}
		});
		f.addNotifySizeListener(new NotifySizeListener() {
			@Override
			public void onSize(CountBytesFeature obj) {
				rt.put("nl3", getNotifySize());
			}
			
			@Override
			public long getNotifySize() {
				return 3L;
			}
		});
		
		f.read();
		assertThat(1L).isEqualTo(f.getUsedBytes()).isEqualTo(f.getReadBytes());
		assertThat(1L).isEqualTo(rt.get("pl"));
		assertThat(1L).isEqualTo(rt.get("pl2"));
		
		f.skip(1L);
		assertThat(2L).isEqualTo(f.getUsedBytes());
		assertThat(1L).isEqualTo(f.getSkippedBytes());
		assertThat(2L).isEqualTo(rt.get("pl"));
		assertThat(2L).isEqualTo(rt.get("pl2"));
		
		f.read(new byte[1]);
		assertThat(3L).isEqualTo(f.getUsedBytes());
		assertThat(2L).isEqualTo(f.getReadBytes());
		assertThat(3L).isEqualTo(rt.get("nl2"));
		assertThat(3L).isEqualTo(rt.get("nl3"));
		
		f.read(new byte[2], 0, 1);
		assertThat(4L).isEqualTo(rt.get("nl"));
		
	}

}
