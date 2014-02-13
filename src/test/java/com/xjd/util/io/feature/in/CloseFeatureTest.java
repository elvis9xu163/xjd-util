package com.xjd.util.io.feature.in;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.xjd.util.io.feature.IOFeatures;

public class CloseFeatureTest {

    InputStream in = new ByteArrayInputStream(new byte[10]);
    
    
    @Test
    public void test() throws IOException {
	CloseFeature win = (CloseFeature) IOFeatures.bind(in, CloseFeature.class);
	
	assertThat(win.isClosed()).isFalse();
	win.close();
	assertThat(win.isClosed()).isTrue();
	
	assertThat(win.read()).isEqualTo(0);
	
	win.setMode(CloseFeature.RETURN_MINUS_1);
	assertThat(win.read()).isEqualTo(-1);
	
	win.setMode(CloseFeature.THROW_EXCEPTION);
	Exception e = null;
	try {
	    win.read();
	} catch (Exception e1) {
	    e = e1;
	    e.printStackTrace();
	}
	assertThat(e).isNotNull().isInstanceOf(IOException.class);
    }

}
