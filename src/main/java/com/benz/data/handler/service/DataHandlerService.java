package com.benz.data.handler.service;

import java.io.IOException;

import com.benz.data.handler.dto.UserDataResponse;

public interface DataHandlerService {

	void processData(String message) throws IOException;

	UserDataResponse retrieveUserData(String emailId) throws IOException;
}
