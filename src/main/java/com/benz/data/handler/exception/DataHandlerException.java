package com.benz.data.handler.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DataHandlerException extends RuntimeException {

	private static final long serialVersionUID = -8208008761293140807L;
	/** error message for the exception */
	private final String message;

	public DataHandlerException(String message) {
		this.message = message;
	}
}
