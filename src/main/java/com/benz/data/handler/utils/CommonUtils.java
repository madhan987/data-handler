package com.benz.data.handler.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommonUtils {

	public static String getCurrentDate() {
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
		return dateTimeFormat.format(LocalDateTime.now());
	}

	public static final String SUCCESS = "Success";

	public static final String FAILURE = "Failure";

	public static final String EMAIL = "email";
}
