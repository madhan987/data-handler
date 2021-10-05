package com.benz.data.handler.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.benz.data.handler.dto.UserDataResponse;
import com.benz.data.handler.service.DataHandlerService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DataHandlerController {

	@Autowired
	private DataHandlerService dataHandlerService;

	@GetMapping("/read")
	public ResponseEntity<UserDataResponse> retrieveUserData(@RequestParam String emailId) throws IOException {
		log.info("Request received to retrieve user data for a email = {} ", emailId);
		return new ResponseEntity<>(dataHandlerService.retrieveUserData(emailId), HttpStatus.OK);
	}
}
