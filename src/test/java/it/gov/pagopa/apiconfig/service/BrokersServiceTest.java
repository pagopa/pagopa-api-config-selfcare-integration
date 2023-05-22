package it.gov.pagopa.apiconfig.service;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.service.BrokersService;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.IntermediariPaRepository;
import it.gov.pagopa.apiconfig.starter.repository.StazioniRepository;
import it.gov.pagopa.apiconfig.util.TestUtil;
import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.Optional;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockBroker;
import static it.gov.pagopa.apiconfig.util.TestUtil.getMockStazioni;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class)
class BrokersServiceTest {

  @MockBean private StazioniRepository stazioniRepository;

  @MockBean private IntermediariPaRepository intermediariPaRepository;

  @Mock private Pageable pageable;

  @Autowired @InjectMocks private BrokersService brokersService;

  @Test
  void getStationsDetailsCI_withStationId_200() throws IOException, JSONException {

    IntermediariPa mockedBroker = getMockBroker();
    Page<Stazioni> page = new PageImpl<>(Lists.newArrayList(getMockStazioni()));

    when(intermediariPaRepository.findByIdIntermediarioPa("1234")).thenReturn(Optional.of(mockedBroker));
    when(stazioniRepository.findAllByFiltersOrderById(anyLong(), anyString(), any())).thenReturn(page);

    StationDetailsList result = brokersService.getStationsDetailsFromBroker("1234", "80007580279_01", pageable);
    String actual = TestUtil.toJson(result);
    String expected = TestUtil.readJsonFromFile("response/get_broker_stations_details_ok1.json");
    JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
  }

  @Test
  void getStationsDetailsCI_withoutStationId_200() throws IOException, JSONException {

    Page<Stazioni> page = new PageImpl<>(Lists.newArrayList(getMockStazioni()));

    when(intermediariPaRepository.findByIdIntermediarioPa("1234")).thenReturn(Optional.of(getMockBroker()));
    when(stazioniRepository.findAllByFiltersOrderById(anyLong(), any())).thenReturn(page);

    StationDetailsList result = brokersService.getStationsDetailsFromBroker("1234", null, pageable);
    String actual = TestUtil.toJson(result);
    String expected = TestUtil.readJsonFromFile("response/get_broker_stations_details_ok1.json");
    JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
  }

  @Test
  void getStationsDetailsCI_noBrokerFound_404() throws IOException, JSONException {
    when(intermediariPaRepository.findByIdIntermediarioPa("1234")).thenReturn(Optional.empty());
    try {
      brokersService.getStationsDetailsFromBroker("1234", "80007580279_01", pageable);
    } catch (AppException e) {
      assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void getStationsDetailsCI_withStationId_404() throws IOException, JSONException {
    when(intermediariPaRepository.findByIdIntermediarioPa("1234")).thenReturn(Optional.of(getMockBroker()));
    when(stazioniRepository.findAllByFiltersOrderById(anyLong(), anyString(), any())).thenReturn(new PageImpl<>(Lists.newArrayList()));
    try {
      brokersService.getStationsDetailsFromBroker("1234", "80007580279_01", pageable);
    } catch (AppException e) {
      assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void getStationsDetailsCI_withoutStationId_404() throws IOException, JSONException {
    when(intermediariPaRepository.findByIdIntermediarioPa("1234")).thenReturn(Optional.of(getMockBroker()));
    when(stazioniRepository.findAllByFiltersOrderById(anyLong(), any())).thenReturn(new PageImpl<>(Lists.newArrayList()));
    try {
      brokersService.getStationsDetailsFromBroker("1234", null, pageable);
    } catch (AppException e) {
      assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
    } catch (Exception e) {
      fail();
    }
  }
}
