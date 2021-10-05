package com.benz.data.handler.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.benz.data.handler.entity.UserEntity;
import com.benz.data.handler.repo.UserDataRepo;
import com.benz.data.handler.service.DataHandlerService;
import com.benz.data.handler.utils.DataEncryptionDecryption;
import com.benz.data.handler.utils.FileOperationHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class DataHandlerServiceImplTest {

	@Autowired
	private DataHandlerService dataHandlerService;

	@Mock
	DataHandlerServiceImpl dataHandlerServiceImpl;

	@MockBean
	private UserDataRepo userDataRepo;

	@MockBean
	private FileOperationHelper fileOperationHelper;

	@MockBean
	private DataEncryptionDecryption dataEncryptionDecryption;

	@Autowired
	private ObjectMapper objectMapper;

	String emailId = "kirand@gmail.com";

	@Test
	void retrieveUserDataTest() throws IOException {
		JsonNode jsonNode = objectMapper.readTree("{\r\n" + "  \"id\": 1,\r\n" + "  \"name\": \"Sam\",\r\n"
				+ "  \"place\": \"Chennai\",\r\n" + "  \"phone\": \"9790123341\",\r\n"
				+ "  \"email\":\"sam@gmail.com\",\r\n" + "  \"ext\":123\r\n" + "}");
		UserEntity userEntity = new UserEntity(1, emailId, "filelocation", "xml");
		when(userDataRepo.findByUserEmail(emailId)).thenReturn(userEntity);
		when(fileOperationHelper.convertFromXML(userEntity.getDocLocation())).thenReturn(jsonNode);
		when(dataEncryptionDecryption.encrypt(Mockito.anyString(), Mockito.anyString())).thenReturn("encryptedData");
		assertEquals("encryptedData", dataHandlerService.retrieveUserData(emailId).getJsonString());

		userEntity.setDocType("CSV");
		when(fileOperationHelper.convertFromCSV(userEntity.getDocLocation())).thenReturn(jsonNode);
		assertEquals("encryptedData", dataHandlerService.retrieveUserData(emailId).getJsonString());
	}

	@Test
	void processDataTest() throws IOException {
		UserEntity userEntity = new UserEntity(1, emailId, "filelocation", "xml");
		when(dataEncryptionDecryption.decrypt(Mockito.anyString(), Mockito.anyString())).thenReturn(
				"{\"jsonNode\":{\"id\":1,\"name\":\"Kiran\",\"place\":\"Bangalore\",\"phone\":\"9790969341\",\"email\":\"kirand@gmail.com\",\"ext\":123},\"fileType\":\"csv\"}");
		when(fileOperationHelper.convertToCSV(Mockito.any())).thenReturn("filelocation");
		when(userDataRepo.save(Mockito.any())).thenReturn(userEntity);
		dataHandlerService.processData(
				"{\"jsonString\":\"Ne79EengRc+psNseQpv+oNgdDITRJqrTpXWZA1XbjX+lcv/ZqPA+dNknl27AVx3/R/2HtbpxoyNnoDOCPMFBPOQs81LqVSty6hcMDvXvItIXX6vLcCRcm9BogYAwcSqhrlW4erFUyXfqnhOc8xBfGqt0WT2AVl3/fcx3IeSV12NOwILgEn1nOAlV7QJDOgKI\",\"requestType\":\"INSERT\",\"secretKey\":\"clQjD\"}");

		when(userDataRepo.findByUserEmail(emailId)).thenReturn(userEntity);
		when(fileOperationHelper.convertToXML(Mockito.any())).thenReturn("filelocation");
		when(fileOperationHelper.fileCleanUp(Mockito.anyString())).thenReturn(true);
		dataHandlerService.processData(
				"{\"jsonString\":\"Ne79EengRc+psNseQpv+oNgdDITRJqrTpXWZA1XbjX+lcv/ZqPA+dNknl27AVx3/R/2HtbpxoyNnoDOCPMFBPOQs81LqVSty6hcMDvXvItIXX6vLcCRcm9BogYAwcSqhrlW4erFUyXfqnhOc8xBfGqt0WT2AVl3/fcx3IeSV12NOwILgEn1nOAlV7QJDOgKI\",\"requestType\":\"UPDATE\",\"secretKey\":\"clQjD\"}");
		assertEquals(true, fileOperationHelper.fileCleanUp("fileLocation"));
	}
}
