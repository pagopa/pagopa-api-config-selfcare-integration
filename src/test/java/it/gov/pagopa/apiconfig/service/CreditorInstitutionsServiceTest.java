package it.gov.pagopa.apiconfig.service;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionStationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.service.CreditorInstitutionsService;
import it.gov.pagopa.apiconfig.starter.repository.CodifichePaRepository;
import it.gov.pagopa.apiconfig.starter.repository.CodificheRepository;
import it.gov.pagopa.apiconfig.starter.repository.IbanValidiPerPaRepository;
import it.gov.pagopa.apiconfig.starter.repository.PaRepository;
import it.gov.pagopa.apiconfig.starter.repository.PaStazionePaRepository;
import it.gov.pagopa.apiconfig.starter.repository.StazioniRepository;
import it.gov.pagopa.apiconfig.util.TestUtil;
import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.io.IOException;
import java.util.Optional;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockPa;
import static it.gov.pagopa.apiconfig.util.TestUtil.getMockPaStazionePa;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
class CreditorInstitutionsServiceTest {

  @MockBean private PaRepository paRepository;

  @MockBean private PaStazionePaRepository paStazionePaRepository;

  @Autowired @InjectMocks private CreditorInstitutionsService creditorInstitutionsService;

  @Test
  void getStationsDetailsCI() throws IOException, JSONException {
    when(paRepository.findByIdDominio("1234")).thenReturn(Optional.of(getMockPa()));
    when(paStazionePaRepository.findAllByFkPa(anyLong()))
        .thenReturn(Lists.newArrayList(getMockPaStazionePa()));

    CreditorInstitutionStationDetailsList result =
        creditorInstitutionsService.getStationsDetailsFromCreditorInstitution("1234");
    String actual = TestUtil.toJson(result);
    String expected =
        TestUtil.readJsonFromFile("response/get_creditorinstitution_stations_details_ok1.json");
    JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
  }
}
