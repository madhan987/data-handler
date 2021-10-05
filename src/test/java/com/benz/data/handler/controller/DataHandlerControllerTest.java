package com.benz.data.handler.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import com.benz.data.handler.dto.UserDataResponse;
import com.benz.data.handler.service.DataHandlerService;

@SpringBootTest
class DataHandlerControllerTest {

	@Autowired
	private DataHandlerController dataHandlerController;

	@MockBean
	private DataHandlerService dataHandlerService;

	@Test
	void retrieveUserDataTest() throws IOException {
		String emailId = "abc@gmail.com";
		when(dataHandlerService.retrieveUserData(emailId)).thenReturn(new UserDataResponse());
		assertEquals(HttpStatus.OK, dataHandlerController.retrieveUserData(emailId).getStatusCode());
	}

}
