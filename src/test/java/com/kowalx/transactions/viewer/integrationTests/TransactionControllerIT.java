package com.kowalx.transactions.viewer.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kowalx.transactions.viewer.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addTransaction_givenCorrectList_ThenOK() throws Exception {
        //GIVEN
        String url = "/transaction";

        List<TransactionDTO> receivedTransactions = new ArrayList<>();
        TransactionDTO transaction1 = new TransactionDTO(123, LocalDate.parse("2019-11-11"), "Opłata za internet", BigDecimal.valueOf(30.44), "PLN");
        TransactionDTO transaction2 = new TransactionDTO(125, LocalDate.parse("2019-11-11"), "Restauracja u Ambrożego", BigDecimal.valueOf(130.4), "PLN");
        receivedTransactions.add(transaction1);
        receivedTransactions.add(transaction2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.withRootName("receivedTransactions").writeValueAsString(receivedTransactions);

        //WHEN-THEN
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String successMessage = result.getResponse().getContentAsString();
        String expectedMessage = "Transactions added successfully.";

        assertTrue(successMessage.contains(expectedMessage));
    }

    @Test
    void addTransaction_givenTheSameTransactionIdInTheSameFile_thenException() throws Exception {
        //GIVEN
        String url = "/transaction";

        List<TransactionDTO> receivedTransactions = new ArrayList<>();
        TransactionDTO transaction1 = new TransactionDTO(123, LocalDate.parse("2019-11-11"), "Opłata za internet", BigDecimal.valueOf(30.44), "PLN");
        TransactionDTO transaction2 = new TransactionDTO(123, LocalDate.parse("2019-11-11"), "Restauracja u Ambrożego", BigDecimal.valueOf(130.4), "PLN");
        receivedTransactions.add(transaction1);
        receivedTransactions.add(transaction2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.withRootName("receivedTransactions").writeValueAsString(receivedTransactions);

        //WHEN-THEN
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedMessage = "Incorrect file. Transaction id defined multiple times in file. Correct id's and try again.";

        assertTrue(message.contains(expectedMessage));
    }

    @Test
    void addTransaction_givenTheSameTransactionIdAlreadyInDB_thenException() throws Exception {
        //GIVEN
        String url = "/transaction";

        List<TransactionDTO> receivedTransactions = new ArrayList<>();
        TransactionDTO transaction1 = new TransactionDTO(110, LocalDate.parse("2019-11-11"), "Opłata za internet", BigDecimal.valueOf(30.44), "PLN");
        receivedTransactions.add(transaction1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.withRootName("receivedTransactions").writeValueAsString(receivedTransactions);

        //WHEN-THEN
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedMessage = "Transaction Id's already existing in DB: [110]. Correct id's and try again.";

        assertTrue(message.contains(expectedMessage));
    }

    @Test
    void getAllTransactionsWithCurrencyConversion_thenOK() throws Exception {
        //GIVEN
        String url = "/transaction/all/withCurrency";

        //WHEN-THEN
        MvcResult result = mockMvc.perform(get(url))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedMessage = "{\"id\":110,\"bookingDate\":\"2019-10-22\",\"mainTitle\":\"Paliwo\"," +
                "\"amount\":100.22,\"currency\":\"PLN\",\"exchangedAmount\":23.06,\"exchangeCurrency\":\"EUR\"}";

        assertTrue(message.contains(expectedMessage));
    }

    @Test
    void getAllTransactions_thenOK() throws Exception {
        //GIVEN
        String url = "/transaction/all";

        //WHEN-THEN
        MvcResult result = mockMvc.perform(get(url))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedMessage = "{\"id\":110,\"bookingDate\":\"2019-10-22\",\"mainTitle\":\"Paliwo\"" +
                ",\"amount\":100.22,\"currency\":\"PLN\",\"exchangedAmount\":null,\"exchangeCurrency\":null}";

        assertTrue(message.contains(expectedMessage));
    }
}
