package kr.co.sunnyvale.sunny.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * HttpServletRequest 의 Body 가져오는 클래스
 * @author Mook
 *
 */
public class BufferedRequestWrapper extends HttpServletRequestWrapper {
	private ByteArrayInputStream byteArrayInputStream = null;

	private ByteArrayOutputStream byteArrayOutputStream = null;

	private BufferedServletInputStream bufferedServletInputStream = null;

	private byte[] buffer = null;

	public BufferedRequestWrapper(HttpServletRequest req) throws IOException {

		super(req);

		// Read InputStream and store its content in a buffer.

		InputStream is = req.getInputStream();

		this.byteArrayOutputStream = new ByteArrayOutputStream();

		byte buf[] = new byte[1024];

		int letti;

		while ((letti = is.read(buf)) > 0) {

			this.byteArrayOutputStream.write(buf, 0, letti);

		}

		this.buffer = this.byteArrayOutputStream.toByteArray();

	}

	@Override
	public ServletInputStream getInputStream() {

		this.byteArrayInputStream = new ByteArrayInputStream(this.buffer);

		this.bufferedServletInputStream = new BufferedServletInputStream(
				this.byteArrayInputStream);

		return this.bufferedServletInputStream;

	}

	public String getRequestBody() throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				this.getInputStream()));

		String line = null;

		StringBuilder inputBuffer = new StringBuilder();

		do {

			line = reader.readLine();

			if (null != line) {

				inputBuffer.append(line.trim());

			}

		} while (line != null);

		reader.close();

		return inputBuffer.toString().trim();

	}

}
