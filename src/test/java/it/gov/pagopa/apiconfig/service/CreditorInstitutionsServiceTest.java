package it.gov.pagopa.apiconfig.service;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedCreditorInstitutionStationRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.service.CreditorInstitutionsService;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.repository.PaRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.Optional;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockPa;
import static it.gov.pagopa.apiconfig.util.TestUtil.getMockPaStazionePa;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
class CreditorInstitutionsServiceTest {

  @MockBean private PaRepository paRepository;

  @MockBean private ExtendedCreditorInstitutionStationRepository ciStationRepository;

  @Autowired @InjectMocks private CreditorInstitutionsService creditorInstitutionsService;

  private Pageable pageable = PageRequest.of(0, 10);

  @Test
  void getStationsDetailsCI_200() throws IOException, JSONException {
    Page<PaStazionePa> page = TestUtil.mockPage(Lists.newArrayList(getMockPaStazionePa()), 10, 0);

    when(paRepository.findByIdDominio("1234")).thenReturn(Optional.of(getMockPa()));
    when(ciStationRepository.findByFkPa(anyLong(), any(Pageable.class))).thenReturn(page);

    StationDetailsList result =
        creditorInstitutionsService.getStationsDetailsFromCreditorInstitution("1234", pageable);
    String actual = TestUtil.toJson(result);
    String expected =
        TestUtil.readJsonFromFile("response/get_creditorinstitution_stations_details_ok1.json");
    JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
  }

  @Test
  void getStationsDetailsCI_404() throws IOException, JSONException {
    when(paRepository.findByIdDominio("12345")).thenReturn(Optional.empty());
    try {
      creditorInstitutionsService.getStationsDetailsFromCreditorInstitution("12345", pageable);
    } catch (AppException e) {
      assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
    } catch (Exception e) {
      fail();
    }
  }
}
