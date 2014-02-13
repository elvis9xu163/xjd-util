package com.xjd.util.io.feature;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.xjd.util.io.feature.in.CountBytesFeature;

public class IOFeaturesTest {
	
	InputStream in = new ConstInputStream();

	@Test
	public void test() throws IOException {
		InputStream win = IOFeatures.bind(in, CountBytesFeature.class);
		
		win.read();
		win.read(new byte[1]);
		assertThat(2L).isEqualTo(((CountBytesFeature) win).getUsedBytes());
		assertThat(2L).isEqualTo(((CountBytesFeature) win).getReadBytes());
		
		InputStream win2 = IOFeatures.bindWithNewStage(win, CountBytesFeature.class);
		
		win2.read();
		win2.read(new byte[1]);
		assertThat(2L).isEqualTo(((CountBytesFeature) win2).getUsedBytes());
		assertThat(2L).isEqualTo(((CountBytesFeature) win2).getReadBytes());
		assertThat(4L).isEqualTo(((CountBytesFeature) win).getUsedBytes());
		assertThat(4L).isEqualTo(((CountBytesFeature) win).getReadBytes());
		
	}

}
