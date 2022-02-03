package com.fetch.demo;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetch.demo.controller.TransactionController;
import com.fetch.demo.entity.Transaction;
import com.fetch.demo.service.TransactionService;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

@WebMvcTest(TransactionController.class)
public class WebMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private TransactionService serviceMock;

    private List<Transaction> txnList = new ArrayList<>();

    // Resources:
    // https://reflectoring.io/spring-boot-web-controller-test/
    // https://stackoverflow.com/questions/41794263/asserting-list-of-return-items-from-spring-controller-with-mockmvc
    // https://www.petrikainulainen.net/programming/spring-framework/unit-testing-of-spring-mvc-controllers-rest-api/
    @Test
    public void findTransactions_ShouldReturnListOfTransactions() {
        
        try {
            txnList.add(new Transaction("Will", 1000));
            txnList.add(new Transaction("Jack", 1000));
            txnList.add(new Transaction("John", 1000));
            txnList.add(new Transaction("Mark", 1000));

            when(serviceMock.findTransactions()).thenReturn(txnList);

            this.mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[*]", hasSize(4)))
                .andExpect(jsonPath("$[0].payer").value("Will"))
                .andExpect(jsonPath("$[0].points").value(1000))
                .andExpect(jsonPath("$[1].payer").value("Jack"))
                .andExpect(jsonPath("$[1].points").value(1000))
                .andExpect(jsonPath("$[2].payer").value("John"))
                .andExpect(jsonPath("$[2].points").value(1000))
                .andExpect(jsonPath("$[3].payer").value("Mark"))
                .andExpect(jsonPath("$[3].points").value(1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findPayersPoints_ShouldReturnMapOfPayersAndPoints(){
        HashMap<String,Integer> mockPayersPoints = new HashMap<>();
        mockPayersPoints.put("Will", 1000);
        mockPayersPoints.put("Jack", 2000);
        
        try {
            when(serviceMock.findPayersPoints()).thenReturn(mockPayersPoints);

            // TODO
            ResultActions result = this.mockMvc.perform(get("/transactions/points"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].key.payer").value("Will"))
                .andExpect(jsonPath("$[0].points").value(1000))
                .andExpect(jsonPath("$[1].payer").value("Jack"))
                .andExpect(jsonPath("$[1].points").value(2000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void assertThatGetTransactionsReturnsListOfTransactions() {
        objectMapper = new ObjectMapper();
        try {
            String txnString = objectMapper.writeValueAsString(txnList);

            assert(txnString).equals(txnList.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
