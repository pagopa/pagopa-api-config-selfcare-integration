package it.gov.pagopa.apiconfig.controller;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.service.CreditorInstitutionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockCreditorInstitutionStationDetailsList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class CreditorInstitutionsControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private CreditorInstitutionsService creditorInstitutionsService;

  @BeforeEach
  void setUp() throws IOException {
    when(creditorInstitutionsService.getStationsDetailsFromCreditorInstitution("1234"))
        .thenReturn(getMockCreditorInstitutionStationDetailsList());
  }

  @ParameterizedTest
  @CsvSource({
    "/creditorinstitutions/1234/stationsdetails",
  })
  void testGets(String url) throws Exception {
    mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}
