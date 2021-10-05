package com.benz.data.handler.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.benz.data.handler.service.DataHandlerService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaConsumer {

	@Autowired
	private DataHandlerService dataHandlerService;

	@KafkaListener(topics = "${user.data.topic}")
	public void receiveMessage(String content) throws IOException {
		log.info("received content = '{}'", content);
		dataHandlerService.processData(content);
	}

}
