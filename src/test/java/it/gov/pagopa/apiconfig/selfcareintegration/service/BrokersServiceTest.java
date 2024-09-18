package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.config.MappingsConfiguration;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionStationSegregationCodesList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.ICIStationRelation;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedCreditorInstitutionStationRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.IntermediariPaRepository;
import it.gov.pagopa.apiconfig.starter.repository.PaStazionePaRepository;
import it.gov.pagopa.apiconfig.starter.repository.StazioniRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockBroker;
import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockPaStazionePa;
import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockStazioni;
import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.mockPage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {MappingsConfiguration.class, BrokersService.class})
class BrokersServiceTest {

    private static final String BROKER_CODE = "1234";
    private static final String CI_TAX_CODE = "ciTaxCode";
    private static final String STATION_ID = "80007580279_01";
    private final Pageable pageable = PageRequest.of(0, 10);

    @MockBean
    private StazioniRepository stazioniRepository;

    @MockBean
    private IntermediariPaRepository intermediariPaRepository;

    @MockBean
    private ExtendedCreditorInstitutionStationRepository paStazionePaRepository;

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
        when(stazioniRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        StationDetailsList result =
                brokersService.getStationsDetailsFromBroker(BROKER_CODE, STATION_ID, "ciTaxCode", pageable);
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.readJsonFromFile("response/get_broker_stations_details_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);

        verify(intermediariPaRepository).findByIdIntermediarioPa(BROKER_CODE);
        verify(stazioniRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getStationsDetailsCI_noBrokerFound_404() {
        when(intermediariPaRepository.findByIdIntermediarioPa(BROKER_CODE)).thenReturn(Optional.empty());

        AppException e = assertThrows(AppException.class, () ->
                brokersService.getStationsDetailsFromBroker(BROKER_CODE, STATION_ID, null, pageable));

        assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());

        verify(intermediariPaRepository).findByIdIntermediarioPa(BROKER_CODE);
        verify(stazioniRepository, never()).findAll(any(Specification.class), any(Pageable.class));
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
        System.out.println(actual);
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getCreditorInstitutionsSegregationCodeAssociatedToBrokerSuccess() {
        ICIStationRelation iciStationRelationMock1 = mock(ICIStationRelation.class);
        ICIStationRelation iciStationRelationMock2 = mock(ICIStationRelation.class);
        when(paStazionePaRepository.findAllByBrokerTaxCode(BROKER_CODE))
                .thenReturn(List.of(iciStationRelationMock1, iciStationRelationMock2));
        when(iciStationRelationMock1.getIdDominio()).thenReturn(CI_TAX_CODE);
        when(iciStationRelationMock1.getSegregazione()).thenReturn(3L);
        when(iciStationRelationMock2.getIdDominio()).thenReturn(CI_TAX_CODE);
        when(iciStationRelationMock1.getSegregazione()).thenReturn(19L);

        CreditorInstitutionStationSegregationCodesList result =
                assertDoesNotThrow(() -> brokersService.getCreditorInstitutionsSegregationCodeAssociatedToBroker(BROKER_CODE));

        assertNotNull(result);
        assertNotNull(result.getCiStationCodes());
        assertEquals(1, result.getCiStationCodes().size());
        assertEquals(CI_TAX_CODE, result.getCiStationCodes().get(0).getCiTaxCode());
        assertEquals(2, result.getCiStationCodes().get(0).getSegregationCodes().size());
    }

    @Test
    void getCreditorInstitutionsSegregationCodeAssociatedToBrokerSuccessNoResult() {
        when(paStazionePaRepository.findAllByBrokerTaxCode(BROKER_CODE))
                .thenReturn(Collections.emptyList());

        CreditorInstitutionStationSegregationCodesList result =
                assertDoesNotThrow(() -> brokersService.getCreditorInstitutionsSegregationCodeAssociatedToBroker(BROKER_CODE));

        assertNotNull(result);
        assertNotNull(result.getCiStationCodes());
        assertTrue(result.getCiStationCodes().isEmpty());
    }
}
