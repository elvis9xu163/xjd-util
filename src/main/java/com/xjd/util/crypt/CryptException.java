package com.xjd.util.crypt;

public class CryptException extends RuntimeException {
	
	private static final long serialVersionUID = 8639830668054790207L;

	public CryptException() {
		super();
	}

	public CryptException(String message, Throwable cause) {
		super(message, cause);
	}

	public CryptException(String message) {
		super(message);
	}

	public CryptException(Throwable cause) {
		super(cause);
	}

}
