package com.benz.data.handler.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.benz.data.handler.dto.UserDataDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileOperationHelper {

	private final String FILE_DIR = FileUtils.getTempDirectoryPath() + File.separator + "user_files" + File.separator;

	private String fileName = null;
	private File file;
	private CsvMapper csvMapper = new CsvMapper();
	private XmlMapper xmlMapper = new XmlMapper();

	public String convertToXML(UserDataDTO userData) throws IOException {
		log.info("Request received to convert the data to XML file ");
		fileName = StringUtils.substringBeforeLast(userData.getJsonNode().get(CommonUtils.EMAIL).textValue(), "@");
		file = new File(FILE_DIR + fileName + "." + userData.getFileType().toLowerCase());
		FileUtils.forceMkdirParent(file);

		xmlMapper.writeValue(file, userData.getJsonNode());
		log.info("Data converted successfully to a file, file location {} ", file.getAbsolutePath());
		return file.getAbsolutePath();
	}

	public String convertToCSV(UserDataDTO userData) throws IOException {
		log.info("Request received to convert the data to CSV file ");
		fileName = StringUtils.substringBeforeLast(userData.getJsonNode().get(CommonUtils.EMAIL).textValue(), "@");
		file = new File(FILE_DIR + fileName + "." + userData.getFileType().toLowerCase());
		FileUtils.forceMkdirParent(file);
		Builder csvSchemaBuilder = CsvSchema.builder();
		userData.getJsonNode().fieldNames().forEachRemaining(fieldName -> {
			csvSchemaBuilder.addColumn(fieldName);
		});
		CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

		csvMapper.writerFor(JsonNode.class).with(csvSchema).writeValue(file, userData.getJsonNode());
		log.info("Data converted successfully to a file, file location {} ", file.getAbsolutePath());
		return file.getAbsolutePath();
	}

	public JsonNode convertFromXML(String filePath) throws IOException {
		log.info("Request received to convert to data from XML file ");
		file = new File(filePath);
		JsonNode jsonNode = xmlMapper.readValue(file, JsonNode.class);
		log.info("Data converted successfully to a Object");
		return jsonNode;
	}

	public JsonNode convertFromCSV(String filePath) throws IOException {
		log.info("Request received to convert to data from CSV file ");
		file = new File(filePath);
		Builder csvSchemaBuilder = CsvSchema.builder();
		CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
		JsonNode jsonNode = csvMapper.readerFor(JsonNode.class).with(csvSchema).readValue(file, JsonNode.class);
		log.info("Data converted successfully to a Object");
		return jsonNode;
	}

	public boolean fileCleanUp(String docLocation) throws IOException {
		file = new File(docLocation);
		FileUtils.forceDelete(file);
		return true;
	}
}
