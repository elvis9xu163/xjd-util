package com.xjd.util.io.feature.in;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.xjd.util.io.feature.IOFeatures;
import com.xjd.util.io.feature.in.MatchBytesFeature.MatchListener;

public class MatchBytesFeatureTest {

    @Test
    public void test() throws IOException {
	byte[] data = new byte[] {1,2,1,2,3,4,5,1,2,1,2,3,4,5,1,2};
	
	InputStream in = new ByteArrayInputStream(data);
	MatchBytesFeature win = (MatchBytesFeature) IOFeatures.bind(in, MatchBytesFeature.class);
	
	win.setBytesToMatch(new byte[]{1, 2, 3, 4, 5});
	final int[] rt = new int[]{0};
	win.addMatchListener(new MatchListener() {
	    @Override
	    public void onMatched(MatchBytesFeature in) {
		rt[0]++;
	    }
	});
	
	byte[] buf = new byte[data.length];
	while (win.read(buf) != -1) {
	    //nothing
	}
	assertThat(rt[0]).isEqualTo(2);
	assertThat(buf).isEqualTo(data);
    }
    
    @Test
    public void test2() throws IOException {
	byte[] data = new byte[] {1,2,1,2,3,4,5,1,2,1,2,3,4,5,1,2};
	
	InputStream in = new ByteArrayInputStream(data);
	MatchBytesFeature win = (MatchBytesFeature) IOFeatures.bind(in, MatchBytesFeature.class);
	
	win.setBytesToMatch(new byte[]{1, 2});
	final int[] rt = new int[]{0};
	win.addMatchListener(new MatchListener() {
	    @Override
	    public void onMatched(MatchBytesFeature in) {
		rt[0]++;
	    }
	});
	
	byte[] buf = new byte[data.length];
	while (win.read(buf) != -1) {
	    //nothing
	}
	assertThat(rt[0]).isEqualTo(5);
	assertThat(buf).isEqualTo(data);
    }
    
    @Test
    public void test3() throws IOException {
	byte[] data = new byte[] {1,2,1,2,3,4,5,1,2,1,2,3,4,5,1,2};
	
	InputStream in = new ByteArrayInputStream(data);
	MatchBytesFeature win = (MatchBytesFeature) IOFeatures.bind(in, MatchBytesFeature.class);
	
	win.setBytesToMatch(new byte[]{1, 2, 3, 4, 5});
	win.setFilterMatched(true);
	final int[] rt = new int[]{0};
	win.addMatchListener(new MatchListener() {
	    @Override
	    public void onMatched(MatchBytesFeature in) {
		rt[0]++;
	    }
	});
	
	byte[] buf = new byte[6];
	while (win.read(buf) != -1) {
	    //nothing
	}
	assertThat(rt[0]).isEqualTo(2);
	assertThat(buf).isEqualTo(new byte[]{1,2,1,2,1,2});
    }

}
