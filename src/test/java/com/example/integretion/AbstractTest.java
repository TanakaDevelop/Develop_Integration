package com.example.integretion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.DevelopIntegretionApplication;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(classes = { DevelopIntegretionApplication.class, DynamodbLocalConfig.class,
		AbstractTest.TestConfig.class })

public abstract class AbstractTest {

	protected static final Path RESOURCE_DIR = Path.of("src/test/resources/");

	@Autowired
	protected WebTestClient webTestClient;

	@LocalServerPort
	int port;

	@Configuration
	static class TestConfig {
		@Bean
		DynamodbLocalInitializer dynamodbLocalInitializer() {
			return new DynamodbLocalInitializer();
		}
	}

	protected String readFile(String fileName) throws IOException {
		Path target = RESOURCE_DIR.resolve(fileName);
		return Files.readString(target);
	}

}
