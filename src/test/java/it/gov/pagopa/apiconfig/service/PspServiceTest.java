package it.gov.pagopa.apiconfig.service;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedChannelRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.service.BrokerPSPsService;
import it.gov.pagopa.apiconfig.selfcareintegration.service.PspService;
import it.gov.pagopa.apiconfig.starter.entity.Canali;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPsp;
import it.gov.pagopa.apiconfig.starter.repository.IntermediariPspRepository;
import it.gov.pagopa.apiconfig.util.TestUtil;
import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.TimeZone;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockChannel;
import static it.gov.pagopa.apiconfig.util.TestUtil.getMockPSPBroker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class)
class PspServiceTest {

  @MockBean private ExtendedChannelRepository channelRepository;

  @MockBean private IntermediariPspRepository brokerPspRepository;

  @Mock private Pageable pageable;

  @Autowired
  @InjectMocks
  private PspService pspService;

  @BeforeEach
  void setup() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @Test
  void getChannelByFiscalCode_200() throws IOException, JSONException {
    Page<Canali> page = TestUtil.mockPage(Lists.newArrayList(getMockChannel()), 10, 0);

    when(channelRepository.findAllByPspFiscalCode(anyString(), any(Pageable.class)))
        .thenReturn(page);

    var result = pspService.getChannelByFiscalCode("CF70000000001",  pageable);
    String actual = TestUtil.toJson(result);
    String expected = TestUtil.readJsonFromFile("response/get_broker_channels_details_ok1.json");
    JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
  }

}
