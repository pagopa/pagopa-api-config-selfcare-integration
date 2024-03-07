package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.IntermediariPaRepository;
import it.gov.pagopa.apiconfig.starter.repository.PaStazionePaRepository;
import it.gov.pagopa.apiconfig.starter.repository.StazioniRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil;
import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;
import java.util.TimeZone;

import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class)
class BrokersServiceTest {

    private final Pageable pageable = PageRequest.of(0, 10);
    @MockBean
    private StazioniRepository stazioniRepository;
    @MockBean
    private IntermediariPaRepository intermediariPaRepository;
    @MockBean
    private PaStazionePaRepository paStazionePaRepository;
    @Autowired
    @InjectMocks
    private BrokersService brokersService;

    @BeforeEach
    void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void getStationsDetailsCI_withStationId_200() throws IOException, JSONException {

        IntermediariPa mockedBroker = getMockBroker();
        Page<Stazioni> page = TestUtil.mockPage(Lists.newArrayList(getMockStazioni()), 10, 0);

        when(intermediariPaRepository.findByIdIntermediarioPa("1234"))
                .thenReturn(Optional.of(mockedBroker));
        when(stazioniRepository.findAllByFiltersOrderById(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(page);

        StationDetailsList result =
                brokersService.getStationsDetailsFromBroker("1234", "80007580279_01", pageable);
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.readJsonFromFile("response/get_broker_stations_details_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getStationsDetailsCI_withoutStationId_200() throws IOException, JSONException {

        Page<Stazioni> page = TestUtil.mockPage(Lists.newArrayList(getMockStazioni()), 10, 0);

        when(intermediariPaRepository.findByIdIntermediarioPa("1234"))
                .thenReturn(Optional.of(getMockBroker()));
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
        when(intermediariPaRepository.findByIdIntermediarioPa("1234"))
                .thenReturn(Optional.of(getMockBroker()));
        when(stazioniRepository.findAllByFiltersOrderById(anyLong(), anyString(), any()))
                .thenReturn(new PageImpl<>(Lists.newArrayList()));
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
        when(intermediariPaRepository.findByIdIntermediarioPa("1234"))
                .thenReturn(Optional.of(getMockBroker()));
        when(stazioniRepository.findAllByFiltersOrderById(anyLong(), any()))
                .thenReturn(new PageImpl<>(Lists.newArrayList()));
        try {
            brokersService.getStationsDetailsFromBroker("1234", null, pageable);
        } catch (AppException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
        } catch (Exception e) {
            fail();
        }
    }


    @ParameterizedTest
    @CsvSource({
            "-",
            "true",
            "false",
    })
    void getCreditorInstitutionsAssociatedToBroker_200(String enabledStation) throws IOException, JSONException {

        Boolean isStationEnabled = "-".equals(enabledStation) ? null : Boolean.parseBoolean(enabledStation);
        int fileIndex = isStationEnabled == null ? 1 : (isStationEnabled ? 2 : 3);

        Page<PaStazionePa> page = TestUtil.mockPage(getMockPaStazionePa(isStationEnabled), 10, 0);

        when(paStazionePaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        CreditorInstitutionDetails result =
                brokersService.getCreditorInstitutionsAssociatedToBroker("1234", isStationEnabled, pageable);
        String actual = TestUtil.toJson(result);

        String expected = TestUtil.readJsonFromFile("response/get_creditor_institution_details_ok" + fileIndex + ".json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }
}
