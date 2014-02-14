package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

import com.xjd.util.io.feature.impl.in.AbstractInputStreamFeature;
import com.xjd.util.io.feature.in.MatchBytesFeature;

public class MatchBytesFeatureImpl extends AbstractInputStreamFeature implements MatchBytesFeature {
	protected MatchBytesStream matchStream = new MatchBytesStream();
	protected List<MatchListener> lsnrs = null;

	@Override
	public InputStream getSource() {
		return matchStream.getInputStream();
	}

	@Override
	public void setSource(InputStream source) {
		matchStream.setInputStream(source);
		super.setSource(matchStream);
	}

	@Override
	public void setBytesToMatch(byte[] bytes) {
		matchStream.setBytesToMatch(bytes);
	}

	@Override
	public byte[] getBytesToMatch() {
		return matchStream.getBytesToMatch();
	}

	@Override
	public void setFilterMatched(boolean filter) {
		matchStream.setFilterMatched(filter);
	}

	@Override
	public boolean isFilterMatched() {
		return matchStream.isFilterMatched();
	}

	@Override
	public void addMatchListener(MatchListener lsnr) {
		if (lsnrs == null) {
			lsnrs = new ArrayList<MatchBytesFeature.MatchListener>();
		}
		lsnrs.add(lsnr);
	}

	@Override
	public void removeMatchListener(MatchListener lsnr) {
		if (lsnrs != null) {
			lsnrs.remove(lsnr);
		}
	}

	protected MatchBytesFeature getMatchBytesFeature() {
		return (MatchBytesFeature) getEnhancedInputStream();
	}

	protected void fire() {
		if (lsnrs != null && lsnrs.size() > 0) {
			for (MatchListener lsnr : lsnrs) {
				lsnr.onMatched(getMatchBytesFeature());
			}
		}
	}

	public class MatchBytesStream extends PushbackInputStream {
		protected byte[] bytesToMatch;
		protected boolean bytesToMatchSet;
		protected boolean filterMatched;

		public MatchBytesStream() {
			super(null);
		}

		public void setInputStream(InputStream in) {
			this.in = in;
		}

		public InputStream getInputStream() {
			return in;
		}

		public void setBytesToMatch(byte[] bytes) {
			bytesToMatch = bytes;
			if (bytes == null || bytes.length == 0) {
				bytesToMatchSet = false;
			} else {
				bytesToMatchSet = true;
			}
			ensureBuf();
		}

		protected void ensureBuf() {
			if (!bytesToMatchSet) {
				buf = new byte[0];
				pos = 0;
			} else {
				int toLen = bytesToMatch.length;
				int bufSize = buf.length - pos;
				toLen = toLen > bufSize ? toLen : bufSize;
				if (buf.length - toLen < 0 || 10 < buf.length - toLen) {
					// 如果当前buf过短或过长
					byte[] newbuf = new byte[toLen];
					int newpos = newbuf.length - bufSize;
					System.arraycopy(buf, pos, newbuf, newpos, bufSize);
					buf = newbuf;
					pos = newpos;
				}
			}
		}

		public byte[] getBytesToMatch() {
			return bytesToMatch;
		}

		public void setFilterMatched(boolean filter) {
			filterMatched = filter;
		}

		public boolean isFilterMatched() {
			return filterMatched;
		}

		@Override
		public int read() throws IOException {
			if (!bytesToMatchSet) {
				return super.read();
			}

			int r = 0;
			boolean matched = true;
			for (int i = 0; i < bytesToMatch.length; i++) { // 匹配查找
				r = super.read();
				if (bytesToMatch[i] != r) {
					matched = false;
					if (r != -1) {
						unread(r); // -1不要unread否则会变成255
					}
					unread(bytesToMatch, 0, i);
					break;
				}
			}

			if (matched) {
				fire();
				if (filterMatched) { // 要过滤掉完全匹配的数据
					return read();
				} else { // 保留匹配数据
					unread(bytesToMatch);
					return super.read();
				}
			} else {
				return super.read();
			}
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			if (!bytesToMatchSet) {
				super.read(b, off, len);
			}

			int i = 0;
			int r = 0;
			for (; i < len; i++) {
				r = read();
				if (r == -1) {
					break;
				} else {
					b[off + i] = (byte) r;
				}
			}

			if (r == -1) { // 流末尾
				if (i > 0) {
					return i;
				} else {
					return -1;
				}
			} else {
				return len;
			}
		}

	}
}
