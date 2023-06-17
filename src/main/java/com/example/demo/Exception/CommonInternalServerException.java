package com.example.demo.Exception;

/**
 * 
 * @author Trupti
 *
 */
public class CommonInternalServerException extends ProcessExecutionException {

	private static final long serialVersionUID = -4072841457646075353L;

	public CommonInternalServerException(String msg) {
		super(msg);
	}

	public CommonInternalServerException(Throwable ex) {
		super(ex);
	}
}
