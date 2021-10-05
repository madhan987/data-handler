package com.benz.data.handler.model;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	/** The status. */
	private String status;

	/** The error message. */
	private String errorMessage;

	/** status code. */
	private HttpStatus statusCode;

	/** The timestamp. */
	private String timestamp;

}
