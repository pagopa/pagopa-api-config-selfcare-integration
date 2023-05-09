package it.gov.pagopa.apiconfig.controller;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.service.BrokersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockBrokerStationDetailsList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class BrokersControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private BrokersService brokersService;

  @BeforeEach
  void setup() throws IOException {
    when(brokersService.getStationsDetailsFromBroker("1234", null, PageRequest.of(0, 50))).thenReturn(getMockBrokerStationDetailsList());
    when(brokersService.getStationsDetailsFromBroker("1234", "80007580279_01", PageRequest.of(0, 50))).thenReturn(getMockBrokerStationDetailsList());
  }

  @ParameterizedTest
  @CsvSource({
    "/brokers/1234/stations?limit=50&pageNumber=0",
  })
  void testGetWithoutStationId(String url) throws Exception {
    mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @ParameterizedTest
  @CsvSource({
      "/brokers/1234/stations?stationId=80007580279_01&limit=50&pageNumber=0",
  })
  void testGetWithStationId(String url) throws Exception {
    mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}
