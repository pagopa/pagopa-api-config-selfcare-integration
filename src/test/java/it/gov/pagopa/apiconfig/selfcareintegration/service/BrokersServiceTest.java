package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.config.MappingsConfiguration;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedStationRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.IntermediariPaRepository;
import it.gov.pagopa.apiconfig.starter.repository.PaStazionePaRepository;
import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;
import java.util.TimeZone;

import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockBroker;
import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockPaStazionePa;
import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockStazioni;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {MappingsConfiguration.class, BrokersService.class})
class BrokersServiceTest {

    private static final String BROKER_CODE = "1234";
    private static final String STATION_ID = "80007580279_01";
    private final Pageable pageable = PageRequest.of(0, 10);

    @MockBean
    private ExtendedStationRepository extendedStationRepository;

    @MockBean
    private IntermediariPaRepository intermediariPaRepository;

    @MockBean
    private PaStazionePaRepository paStazionePaRepository;

    @Autowired
    private BrokersService brokersService;

    @BeforeEach
    void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void getStationsDetailsCI_withStationIdAndCITaxCode_200() throws IOException, JSONException {
        IntermediariPa mockedBroker = getMockBroker();
        Page<Stazioni> page = TestUtil.mockPage(Lists.newArrayList(getMockStazioni()), 10, 0);

        when(intermediariPaRepository.findByIdIntermediarioPa(BROKER_CODE))
                .thenReturn(Optional.of(mockedBroker));
        when(extendedStationRepository
                .findAllFilteredByStationIdAndCITaxCodeOrderById(anyLong(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(page);

        StationDetailsList result =
                brokersService.getStationsDetailsFromBroker(BROKER_CODE, STATION_ID, "ciTaxCode", pageable);
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.readJsonFromFile("response/get_broker_stations_details_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);

        verify(intermediariPaRepository).findByIdIntermediarioPa(BROKER_CODE);
        verify(extendedStationRepository, never()).findAllByFiltersOrderById(anyLong(), any(Pageable.class));
        verify(extendedStationRepository, never()).findAllByFiltersOrderById(anyLong(), anyString(), any(Pageable.class));
        verify(extendedStationRepository, never())
                .findAllFilteredByCITaxCodeOrderById(anyLong(), anyString(), any(Pageable.class));
        verify(extendedStationRepository)
                .findAllFilteredByStationIdAndCITaxCodeOrderById(anyLong(), anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void getStationsDetailsCI_withoutStationIdAndCITaxCode_200() throws IOException, JSONException {
        Page<Stazioni> page = TestUtil.mockPage(Lists.newArrayList(getMockStazioni()), 10, 0);

        when(intermediariPaRepository.findByIdIntermediarioPa(BROKER_CODE))
                .thenReturn(Optional.of(getMockBroker()));
        when(extendedStationRepository.findAllByFiltersOrderById(anyLong(), any())).thenReturn(page);

        StationDetailsList result = brokersService.getStationsDetailsFromBroker(BROKER_CODE, null, null, pageable);
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.readJsonFromFile("response/get_broker_stations_details_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);

        verify(intermediariPaRepository).findByIdIntermediarioPa(BROKER_CODE);
        verify(extendedStationRepository).findAllByFiltersOrderById(anyLong(), any(Pageable.class));
        verify(extendedStationRepository, never()).findAllByFiltersOrderById(anyLong(), anyString(), any(Pageable.class));
        verify(extendedStationRepository, never())
                .findAllFilteredByCITaxCodeOrderById(anyLong(), anyString(), any(Pageable.class));
        verify(extendedStationRepository, never())
                .findAllFilteredByStationIdAndCITaxCodeOrderById(anyLong(), anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void getStationsDetailsCI_withoutStationIdAndWithCITaxCode_200() throws IOException, JSONException {
        Page<Stazioni> page = TestUtil.mockPage(Lists.newArrayList(getMockStazioni()), 10, 0);

        when(intermediariPaRepository.findByIdIntermediarioPa(BROKER_CODE))
                .thenReturn(Optional.of(getMockBroker()));
        when(extendedStationRepository.findAllFilteredByCITaxCodeOrderById(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(page);

        StationDetailsList result = brokersService.getStationsDetailsFromBroker(BROKER_CODE, null, "ciTaxCode", pageable);
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.readJsonFromFile("response/get_broker_stations_details_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);

        verify(intermediariPaRepository).findByIdIntermediarioPa(BROKER_CODE);
        verify(extendedStationRepository, never()).findAllByFiltersOrderById(anyLong(), any(Pageable.class));
        verify(extendedStationRepository, never()).findAllByFiltersOrderById(anyLong(), anyString(), any(Pageable.class));
        verify(extendedStationRepository)
                .findAllFilteredByCITaxCodeOrderById(anyLong(), anyString(), any(Pageable.class));
        verify(extendedStationRepository, never())
                .findAllFilteredByStationIdAndCITaxCodeOrderById(anyLong(), anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void getStationsDetailsCI_withStationIdAndWithoutCITaxCode_200() throws IOException, JSONException {
        IntermediariPa mockedBroker = getMockBroker();
        Page<Stazioni> page = TestUtil.mockPage(Lists.newArrayList(getMockStazioni()), 10, 0);

        when(intermediariPaRepository.findByIdIntermediarioPa(BROKER_CODE))
                .thenReturn(Optional.of(mockedBroker));
        when(extendedStationRepository.findAllByFiltersOrderById(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(page);

        StationDetailsList result =
                brokersService.getStationsDetailsFromBroker(BROKER_CODE, STATION_ID, null, pageable);
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.readJsonFromFile("response/get_broker_stations_details_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);

        verify(intermediariPaRepository).findByIdIntermediarioPa(BROKER_CODE);
        verify(extendedStationRepository, never()).findAllByFiltersOrderById(anyLong(), any(Pageable.class));
        verify(extendedStationRepository).findAllByFiltersOrderById(anyLong(), anyString(), any(Pageable.class));
        verify(extendedStationRepository, never())
                .findAllFilteredByCITaxCodeOrderById(anyLong(), anyString(), any(Pageable.class));
        verify(extendedStationRepository, never())
                .findAllFilteredByStationIdAndCITaxCodeOrderById(anyLong(), anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void getStationsDetailsCI_noBrokerFound_404() {
        when(intermediariPaRepository.findByIdIntermediarioPa(BROKER_CODE)).thenReturn(Optional.empty());

        AppException e = assertThrows(AppException.class, () ->
                brokersService.getStationsDetailsFromBroker(BROKER_CODE, STATION_ID, null, pageable));

        assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());

        verify(intermediariPaRepository).findByIdIntermediarioPa(BROKER_CODE);
        verify(extendedStationRepository, never()).findAllByFiltersOrderById(anyLong(), any(Pageable.class));
        verify(extendedStationRepository, never()).findAllByFiltersOrderById(anyLong(), anyString(), any(Pageable.class));
        verify(extendedStationRepository, never())
                .findAllFilteredByCITaxCodeOrderById(anyLong(), anyString(), any(Pageable.class));
        verify(extendedStationRepository, never())
                .findAllFilteredByStationIdAndCITaxCodeOrderById(anyLong(), anyString(), anyString(), any(Pageable.class));
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
                brokersService.getCreditorInstitutionsAssociatedToBroker(BROKER_CODE, isStationEnabled, pageable);
        String actual = TestUtil.toJson(result);

        String expected = TestUtil.readJsonFromFile("response/get_creditor_institution_details_ok" + fileIndex + ".json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }
}
