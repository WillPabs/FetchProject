package com.fetch.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.fetch.demo.controller.TransactionController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private TransactionController txnController;

	@Test
	void contextLoads() throws Exception{
		assertThat(txnController).isNotNull();
	}

}
