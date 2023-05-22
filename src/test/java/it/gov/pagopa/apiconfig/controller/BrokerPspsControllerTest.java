package it.gov.pagopa.apiconfig.controller;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockChannelDetailsList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.service.BrokerPSPsService;
import java.io.IOException;
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

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class BrokerPspsControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private BrokerPSPsService brokerPspsService;

  @BeforeEach
  void setup() throws IOException {
    when(brokerPspsService.getChannelDetailsFromPSPBroker("LU30726739", null, PageRequest.of(0, 10))).thenReturn(getMockChannelDetailsList());
    when(brokerPspsService.getChannelDetailsFromPSPBroker("LU30726739", "LU30726739_02", PageRequest.of(0, 10))).thenReturn(getMockChannelDetailsList());
  }

  @ParameterizedTest
  @CsvSource({
    "/brokerPsps/LU30726739/channels?limit=10&page=0",
  })
  void testGetWithoutChannelId(String url) throws Exception {
    mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @ParameterizedTest
  @CsvSource({
      "/brokerPsps/LU30726739/channels?limit=10&page=0&channelId=LU30726739_02",
  })
  void testGetWithChannelId(String url) throws Exception {
    mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}
