package com.example.integretion;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class UserSearchTest extends AbstractTest {

	protected static final Path REQUEST_DIR = Path.of("jsonData/request/");
	protected static final Path RESPONSE_DIR = Path.of("jsonData/response/");

	@Test
	void test1() throws IOException {

		String requestData = readFile(REQUEST_DIR + "/test1.json");
		String responseData = readFile(RESPONSE_DIR + "/test1.json");

		webTestClient = webTestClient.mutate()
				.responseTimeout(Duration.ofSeconds(50))
				.build();

		webTestClient
				.post()
				.uri("/api/user/search")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestData)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.OK)//
				.expectBody().json(responseData);//
	}

}
