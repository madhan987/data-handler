package com.benz.data.handler.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.benz.data.handler.constant.FileType;
import com.benz.data.handler.constant.RequestType;
import com.benz.data.handler.dto.KafkaMessageDTO;
import com.benz.data.handler.dto.UserDataDTO;
import com.benz.data.handler.dto.UserDataResponse;
import com.benz.data.handler.entity.UserEntity;
import com.benz.data.handler.repo.UserDataRepo;
import com.benz.data.handler.service.DataHandlerService;
import com.benz.data.handler.utils.CommonUtils;
import com.benz.data.handler.utils.DataEncryptionDecryption;
import com.benz.data.handler.utils.FileOperationHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

@Service
@Slf4j
public class DataHandlerServiceImpl implements DataHandlerService {

	@Autowired
	private DataEncryptionDecryption dataEncryptionDecryption;

	@Autowired
	private FileOperationHelper fileOperationHelper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserDataRepo userDataRepo;

	@Value("${private.key}")
	private String privateKey;

	@Override
	public void processData(String message) throws IOException {
		log.info("Processing user data");
		KafkaMessageDTO kafkaMessage = objectMapper.readValue(message, KafkaMessageDTO.class);

		UserDataDTO userData = objectMapper.readValue(dataEncryptionDecryption.decrypt(kafkaMessage.getJsonString(),
				kafkaMessage.getSecretKey() + privateKey), UserDataDTO.class);
		UserEntity userEntity = null;
		String fileLocation;
		if (kafkaMessage.getRequestType().equalsIgnoreCase(RequestType.INSERT.name())) {
			fileLocation = processToFile(userData);
			userEntity = saveUserEntity(userData, fileLocation);
			log.info("User date is successfully persisted in DB with id {} ", userEntity.getUserId());
		} else {
			userEntity = userDataRepo.findByUserEmail(userData.getJsonNode().get(CommonUtils.EMAIL).textValue());
			fileOperationHelper.fileCleanUp(userEntity.getDocLocation());
			fileLocation = processToFile(userData);
			userEntity.setDocLocation(fileLocation);
			userEntity.setDocType(userData.getFileType());
			userEntity = userDataRepo.save(userEntity);
			log.info("User date is successfully updated for the id {} ", userEntity.getUserId());
		}

	}

	private String processToFile(UserDataDTO userData) throws IOException {
		String fileLocation;
		if (userData.getFileType().equalsIgnoreCase(FileType.XML.toString())) {
			fileLocation = fileOperationHelper.convertToXML(userData);
		} else {
			fileLocation = fileOperationHelper.convertToCSV(userData);
		}
		return fileLocation;
	}

	private UserEntity saveUserEntity(UserDataDTO userData, String fileLocation) {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserEmail(userData.getJsonNode().get(CommonUtils.EMAIL).textValue());
		userEntity.setDocType(userData.getFileType());
		userEntity.setDocLocation(fileLocation);
		return userDataRepo.save(userEntity);
	}

	@Override
	public UserDataResponse retrieveUserData(String emailId) throws IOException {
		log.info("Retrieving the data for emailId = {}", emailId);
		UserEntity userEntity = userDataRepo.findByUserEmail(emailId);
		JsonNode jsonNode;
		if (userEntity.getDocType().equalsIgnoreCase(FileType.XML.toString())) {
			jsonNode = fileOperationHelper.convertFromXML(userEntity.getDocLocation());
		} else {
			jsonNode = fileOperationHelper.convertFromCSV(userEntity.getDocLocation());
		}
		String secret = RandomString.make(5);
		String userDataJSONString = objectMapper.writeValueAsString(jsonNode);
		UserDataResponse userDataResponse = new UserDataResponse(
				dataEncryptionDecryption.encrypt(userDataJSONString, secret + privateKey), secret);
		log.info("Data retrieved successfully for emailId = {}", emailId);
		return userDataResponse;
	}

}
