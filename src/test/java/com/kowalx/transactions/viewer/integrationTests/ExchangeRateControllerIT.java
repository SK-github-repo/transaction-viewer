package com.kowalx.transactions.viewer.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kowalx.transactions.viewer.dto.ExchangeRateDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExchangeRateControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addExchangeRates_givenCorrectList_ThenOK() throws Exception {
        //GIVEN
        String url = "/exchange";

        List<ExchangeRateDTO> receivedExchangeRates = new ArrayList<>();
        ExchangeRateDTO exchangeRate1 = new ExchangeRateDTO(LocalDate.parse("2019-10-11"), LocalDate.parse("2019-12-30"), BigDecimal.valueOf(3.44));
        ExchangeRateDTO exchangeRate2 = new ExchangeRateDTO(LocalDate.parse("2019-09-11"), LocalDate.parse("2019-10-10"), BigDecimal.valueOf(4.44));
        receivedExchangeRates.add(exchangeRate1);
        receivedExchangeRates.add(exchangeRate2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.withRootName("receivedExchangeRates").writeValueAsString(receivedExchangeRates);

        //WHEN-THEN
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedMessage = "Exchange rates imported successfully.";

        assertTrue(message.contains(expectedMessage));
    }

    @Test
    void addExchangeRates_givenListWithOverlapDateCollision_thenException() throws Exception {
        //GIVEN
        String url = "/exchange";
        List<ExchangeRateDTO> receivedExchangeRates = new ArrayList<>();
        ExchangeRateDTO exchangeRate1 = new ExchangeRateDTO(LocalDate.parse("2019-10-11"), LocalDate.parse("2019-12-30"), BigDecimal.valueOf(3.44));
        ExchangeRateDTO exchangeRate2 = new ExchangeRateDTO(LocalDate.parse("2019-09-11"), LocalDate.parse("2019-11-30"), BigDecimal.valueOf(4.44));
        receivedExchangeRates.add(exchangeRate1);
        receivedExchangeRates.add(exchangeRate2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.withRootName("receivedExchangeRates").writeValueAsString(receivedExchangeRates);

        //WHEN-THEN
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedErrorMessage = "Date collision found in provided file. Correct date ranges to import the file.";

        assertTrue(message.contains(expectedErrorMessage));
    }

    @Test
    void addExchangeRates_givenListWithSurroundingDateCollision_thenException() throws Exception {
        //GIVEN
        String url = "/exchange";
        List<ExchangeRateDTO> receivedExchangeRates = new ArrayList<>();
        ExchangeRateDTO exchangeRate1 = new ExchangeRateDTO(LocalDate.parse("2019-10-11"), LocalDate.parse("2019-12-30"), BigDecimal.valueOf(3.44));
        ExchangeRateDTO exchangeRate2 = new ExchangeRateDTO(LocalDate.parse("2019-09-11"), LocalDate.parse("2020-01-30"), BigDecimal.valueOf(4.44));
        receivedExchangeRates.add(exchangeRate1);
        receivedExchangeRates.add(exchangeRate2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.withRootName("receivedExchangeRates").writeValueAsString(receivedExchangeRates);

        //WHEN-THEN
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedErrorMessage = "Date collision found in provided file. Correct date ranges to import the file.";

        assertTrue(message.contains(expectedErrorMessage));
    }

    @Test
    void addExchangeRates_givenExchangeRateWithNotExistingCurrency_thenException() throws Exception {
        //GIVEN
        String url = "/exchange";
        List<ExchangeRateDTO> receivedExchangeRates = new ArrayList<>();
        ExchangeRateDTO exchangeRate = new ExchangeRateDTO(LocalDate.parse("2019-10-11"), LocalDate.parse("2019-12-30"), BigDecimal.valueOf(3.44));
        exchangeRate.setCurrencyFrom("USD");
        receivedExchangeRates.add(exchangeRate);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.withRootName("receivedExchangeRates").writeValueAsString(receivedExchangeRates);

        //WHEN-THEN
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedErrorMessage = "Provided currency: USD is missing in currency table. Add it to currency table to successfully import exchange rate for it.";

        assertTrue(message.contains(expectedErrorMessage));
    }

    @Test
    void addExchangeRates_givenExchangeRateWithoutEndDate_thenException() throws Exception {
        //GIVEN
        String url = "/exchange";
        List<ExchangeRateDTO> receivedExchangeRates = new ArrayList<>();
        ExchangeRateDTO exchangeRate = new ExchangeRateDTO(LocalDate.parse("2019-10-11"), null, BigDecimal.valueOf(3.44));
        receivedExchangeRates.add(exchangeRate);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.withRootName("receivedExchangeRates").writeValueAsString(receivedExchangeRates);

        //WHEN-THEN
        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andReturn();

        String message = result.getResponse().getContentAsString();
        String expectedErrorMessage = "Date in exchange rate range is missing. Please check if all exchange ranges have set start and end date.";

        assertTrue(message.contains(expectedErrorMessage));
    }
}
