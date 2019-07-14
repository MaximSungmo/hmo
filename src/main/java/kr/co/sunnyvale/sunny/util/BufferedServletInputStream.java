package kr.co.sunnyvale.sunny.util;

import java.io.ByteArrayInputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class BufferedServletInputStream extends ServletInputStream {
	private ByteArrayInputStream byteArrayInputStream;

	public BufferedServletInputStream(ByteArrayInputStream bais) {
		this.byteArrayInputStream = bais;
	}
	
	@Override
	public int available() {
		return this.byteArrayInputStream.available();
	}

	@Override
	public int read() {
		return this.byteArrayInputStream.read();
	}

	@Override
	public int read(byte[] buf, int off, int len) {
		return this.byteArrayInputStream.read(buf, off, len);
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setReadListener(ReadListener arg0) {
		// TODO Auto-generated method stub
		
	}

}
