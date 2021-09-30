package com.adamfgcross.drop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DropApplicationTests {

	static {
		System.setProperty("SPRING_DATASOURCE_URL", "jdbc:h2:mem:testdb");
	}

	@Test
	void contextLoads() {
	}

}
