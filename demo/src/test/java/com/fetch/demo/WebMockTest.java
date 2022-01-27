package com.fetch.demo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetch.demo.controller.TransactionController;
import com.fetch.demo.entity.Transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
public class WebMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Transaction txn;
    
    @MockBean
    private TransactionController txnController;


    // Resources:
    // https://reflectoring.io/spring-boot-web-controller-test/
    // https://stackoverflow.com/questions/41794263/asserting-list-of-return-items-from-spring-controller-with-mockmvc
    @Test
    public void methodShouldReturnListOfTransactions() {
        // Given
        List<Transaction> txnList = new ArrayList<>();
    
        // When
        txnList.add(new Transaction("Will", 1000));
        txnList.add(new Transaction("Jack", 1000));
        txnList.add(new Transaction("John", 1000));
        txnList.add(new Transaction("Mark", 1000));
        objectMapper = new ObjectMapper();
        String jsonList = objectMapper.writeValueAsString(txnList);

        when(txnController.getTransactions()).thenReturn(txnList);

        // Then
        this.mockMvc.perform(get("/transactions")).andDo(print()).andExpect(content().json(jsonList));
    }
}
