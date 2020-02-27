package com.kowalx.transactions.viewer.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kowalx.transactions.viewer.dto.CurrencyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addCurrency_givenNewCurrency_ThenOK() throws Exception {
        //GIVEN
        String url = "/currency";
        CurrencyDTO newCurrency = new CurrencyDTO("funt brytyjski", "GBP");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(newCurrency);

        //WHEN-THEN
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedMessage = "Currency added successfully.";

        assertTrue(message.contains(expectedMessage));
    }

    @Test
    void addCurrency_givenExistingCurrency_thenException() throws Exception {
        //GIVEN
        String url = "/currency";
        CurrencyDTO newCurrency = new CurrencyDTO("euro", "EUR");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(newCurrency);

        //WHEN-THEN
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedErrorMessage = "Provided currency symbol already exists: EUR.";

        assertTrue(message.contains(expectedErrorMessage));
    }
}