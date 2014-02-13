package com.xjd.util.io.feature.impl.in;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.xjd.util.io.feature.ConstInputStream;
import com.xjd.util.io.feature.impl.in.featureimpl.CountBytesFeatureImpl;
import com.xjd.util.io.feature.in.CountBytesFeature;
import com.xjd.util.io.feature.in.CountBytesFeature.ProgressListener;
import com.xjd.util.io.feature.in.InputStreamFeature;

public class InputStreamFeatureBinderTest {

	InputStream in = new ConstInputStream();

	@Test
	public void test1() throws IOException {
		Map<Class<? extends InputStreamFeature>, Object> featureMap = new LinkedHashMap<Class<? extends InputStreamFeature>, Object>();
		featureMap.put(CountBytesFeature.class, null);

		InputStream win = InputStreamFeatureBinder.bind(in, featureMap);
		CountBytesFeature cf = (CountBytesFeature) win;

		win.read();
		assertThat(1L).isEqualTo(cf.getUsedBytes());
		assertThat(1L).isEqualTo(cf.getReadBytes());

		win.read(new byte[2]);
		assertThat(3L).isEqualTo(cf.getUsedBytes());
		assertThat(3L).isEqualTo(cf.getReadBytes());

		win.skip(3);
		assertThat(6L).isEqualTo(cf.getUsedBytes());
		assertThat(3L).isEqualTo(cf.getSkippedBytes());
		
		cf.addProgressListener(new ProgressListener() {
			@Override
			public void onProgress(CountBytesFeature obj, long totallUsedBytes, long thisUsedBytes) {
				assertThat(obj.getClass().equals(CountBytesFeatureImpl.class)).isFalse();
			}
		});

	}

	@Test
	public void test2() throws IOException {
		InputStream win = InputStreamFeatureBinder.bind(in, CountBytesFeature.class);

		win.read();

		InputStream win2 = InputStreamFeatureBinder.bind(InputStreamFeatureBinder.newStage(win), CountBytesFeature.class);

		win2.read();

		assertThat(2L).isEqualTo(((CountBytesFeature) win).getUsedBytes());
		assertThat(1L).isEqualTo(((CountBytesFeature) win2).getUsedBytes());

		win.read();

		assertThat(3L).isEqualTo(((CountBytesFeature) win).getUsedBytes());
		assertThat(1L).isEqualTo(((CountBytesFeature) win2).getUsedBytes());
	}

}
