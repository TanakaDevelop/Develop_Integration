package com.example.integretion;

import java.nio.file.Path;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class UserSearchTest extends AbstractTest {

	protected static final Path RESOURCE_DIR = Path.of("src/test/resources/");

	@Disabled
	@Test
	void test() {
		webTestClient
				.post()
				.uri("/api/user/search")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue("{\"id\":\"12345\"}")
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class)
				.isEqualTo("Hello, World!");
	}

}
