package it.gov.pagopa.apiconfig.service;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockChannel;
import static it.gov.pagopa.apiconfig.util.TestUtil.getMockPSPBroker;
import static it.gov.pagopa.apiconfig.util.TestUtil.getMockStazioni;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedChannelRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.service.BrokerPSPsService;
import it.gov.pagopa.apiconfig.starter.entity.Canali;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPsp;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.IntermediariPspRepository;
import it.gov.pagopa.apiconfig.util.TestUtil;
import java.io.IOException;
import java.util.Optional;
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

@SpringBootTest(classes = Application.class)
class BrokerPSPsServiceTest {

  @MockBean private ExtendedChannelRepository channelRepository;

  @MockBean private IntermediariPspRepository brokerPspRepository;

  @Mock private Pageable pageable;

  @Autowired @InjectMocks private BrokerPSPsService brokerPspsService;

  @Test
  void getStationsDetailsCI_withChannelId_200() throws IOException, JSONException {

    IntermediariPsp mockedBroker = getMockPSPBroker();
    Page<Canali> page = TestUtil.mockPage(Lists.newArrayList(getMockChannel()), 10, 0);

    when(brokerPspRepository.findByIdIntermediarioPsp("LU30726739")).thenReturn(Optional.of(mockedBroker));
    when(channelRepository.findAllByFiltersOrderById(anyLong(), anyString(), any())).thenReturn(page);

    ChannelDetailsList result = brokerPspsService.getChannelDetailsFromPSPBroker("LU30726739", "LU30726739_02", pageable);
    String actual = TestUtil.toJson(result);
    String expected = TestUtil.readJsonFromFile("response/get_broker_channels_details_ok1.json");
    JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
  }

  @Test
  void getStationsDetailsCI_withoutChannelId_200() throws IOException, JSONException {

    Page<Canali> page = TestUtil.mockPage(Lists.newArrayList(getMockChannel()), 10, 0);

    when(brokerPspRepository.findByIdIntermediarioPsp("LU30726739")).thenReturn(Optional.of(getMockPSPBroker()));
    when(channelRepository.findAllByFiltersOrderById(anyLong(), any())).thenReturn(page);

    ChannelDetailsList result = brokerPspsService.getChannelDetailsFromPSPBroker("LU30726739", null, pageable);
    String actual = TestUtil.toJson(result);
    String expected = TestUtil.readJsonFromFile("response/get_broker_channels_details_ok1.json");
    JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
  }

  @Test
  void getStationsDetailsCI_noBrokerFound_404() throws IOException, JSONException {
    when(brokerPspRepository.findByIdIntermediarioPsp("LU30726739")).thenReturn(Optional.empty());
    try {
      brokerPspsService.getChannelDetailsFromPSPBroker("LU30726739", "LU30726739_02", pageable);
    } catch (AppException e) {
      assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void getStationsDetailsCI_withChannelId_404() throws IOException, JSONException {
    when(brokerPspRepository.findByIdIntermediarioPsp("LU30726739")).thenReturn(Optional.of(getMockPSPBroker()));
    when(channelRepository.findAllByFiltersOrderById(anyLong(), anyString(), any())).thenReturn(new PageImpl<>(Lists.newArrayList()));
    try {
      brokerPspsService.getChannelDetailsFromPSPBroker("LU30726739", "LU30726739_02", pageable);
    } catch (AppException e) {
      assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void getStationsDetailsCI_withoutChannelId_404() throws IOException, JSONException {
    when(brokerPspRepository.findByIdIntermediarioPsp("LU30726739")).thenReturn(Optional.of(getMockPSPBroker()));
    when(channelRepository.findAllByFiltersOrderById(anyLong(), any())).thenReturn(new PageImpl<>(Lists.newArrayList()));
    try {
      brokerPspsService.getChannelDetailsFromPSPBroker("LU30726739", null, pageable);
    } catch (AppException e) {
      assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
    } catch (Exception e) {
      fail();
    }
  }
}
