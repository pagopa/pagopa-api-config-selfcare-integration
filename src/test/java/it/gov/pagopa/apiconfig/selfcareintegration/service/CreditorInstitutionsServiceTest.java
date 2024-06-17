package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.config.MappingsConfiguration;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppError;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.AvailableCodes;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCodeList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionInfo;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedCreditorInstitutionStationRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.PaRepository;
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
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockPa;
import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockPaStazionePa;
import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockStazioni;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CreditorInstitutionsService.class, MappingsConfiguration.class})
class CreditorInstitutionsServiceTest {

    private static final String CI_TAX_CODE = "1234";
    private static final String TARGET_CI_TAX_CODE = "targetCITaxCode";
    private static final String STATION_CODE = "stationCode";
    private static final String RAGIONE_SOCIALE = "pa";

    @MockBean
    private PaRepository paRepository;

    @MockBean
    private ExtendedCreditorInstitutionStationRepository ciStationRepository;

    @MockBean
    private StazioniRepository stationRepository;

    @Autowired
    private CreditorInstitutionsService creditorInstitutionsService;

    private final Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void getStationsDetailsCI_200() throws IOException, JSONException {
        Page<PaStazionePa> page = TestUtil.mockPage(Lists.newArrayList(getMockPaStazionePa()), 10, 0);

        when(paRepository.findByIdDominio(CI_TAX_CODE)).thenReturn(Optional.of(getMockPa()));
        when(ciStationRepository.findByFkPa(anyLong(), any(Pageable.class))).thenReturn(page);

        StationDetailsList result =
                creditorInstitutionsService.getStationsDetailsFromCreditorInstitution(CI_TAX_CODE, pageable);
        String actual = TestUtil.toJson(result);
        String expected =
                TestUtil.readJsonFromFile("response/get_creditorinstitution_stations_details_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getStationsDetailsCI_404() {
        when(paRepository.findByIdDominio("12345")).thenReturn(Optional.empty());
        try {
            creditorInstitutionsService.getStationsDetailsFromCreditorInstitution("12345", pageable);
        } catch (AppException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getApplicationCodes_noUsedIncluded_200() throws IOException, JSONException {
        PaStazionePa stationWithoutApplicationCode = getMockPaStazionePa();
        stationWithoutApplicationCode.setProgressivo(null);
        stationWithoutApplicationCode.getFkStazione().setIdStazione("noappcodestation");
        List<PaStazionePa> stations = List.of(getMockPaStazionePa(), stationWithoutApplicationCode);

        when(paRepository.findByIdDominio(CI_TAX_CODE)).thenReturn(Optional.of(getMockPa()));
        when(ciStationRepository.findByFkPa(anyLong())).thenReturn(stations);

        CIAssociatedCodeList result =
                creditorInstitutionsService.getApplicationCodesFromCreditorInstitution(CI_TAX_CODE, false);
        String actual = TestUtil.toJson(result);
        String expected =
                TestUtil.readJsonFromFile("response/get_creditorinstitution_applicationcodes_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getApplicationCodes_usedIncluded_200() throws IOException, JSONException {
        PaStazionePa stationWithoutApplicationCode = getMockPaStazionePa();
        stationWithoutApplicationCode.setProgressivo(null);
        stationWithoutApplicationCode.getFkStazione().setIdStazione("noappcodestation");
        List<PaStazionePa> stations = List.of(getMockPaStazionePa(), stationWithoutApplicationCode);

        when(paRepository.findByIdDominio(CI_TAX_CODE)).thenReturn(Optional.of(getMockPa()));
        when(ciStationRepository.findByFkPa(anyLong())).thenReturn(stations);

        CIAssociatedCodeList result =
                creditorInstitutionsService.getApplicationCodesFromCreditorInstitution(CI_TAX_CODE, true);
        String actual = TestUtil.toJson(result);
        String expected =
                TestUtil.readJsonFromFile("response/get_creditorinstitution_applicationcodes_ok2.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getApplicationCodes_404() {
        when(paRepository.findByIdDominio("12345")).thenReturn(Optional.empty());

        AppException e = assertThrows(AppException.class, () ->
                creditorInstitutionsService.getApplicationCodesFromCreditorInstitution(CI_TAX_CODE, false)
        );

        assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
    }

    @ParameterizedTest
    @CsvSource({
            "'02438750586', '49'",
            "'00493410583', '96'",
            "'01484460587', '97'",
    })
    void getSegregationCodesReservedSuccess(String ciTaxCode, String resultCode) throws IOException {
        List<PaStazionePa> stations = List.of(getMockPaStazionePa());

        when(paRepository.findByIdDominio(TARGET_CI_TAX_CODE)).thenReturn(Optional.of(getMockPa()));
        when(ciStationRepository.findByFkPa(anyLong())).thenReturn(stations);

        AvailableCodes result = assertDoesNotThrow(() ->
                creditorInstitutionsService.getAvailableCISegregationCodes(ciTaxCode, TARGET_CI_TAX_CODE)
        );

        assertNotNull(result);
        assertNotNull(result.getAvailableCodeList());
        assertEquals(1, result.getAvailableCodeList().size());
        assertEquals(resultCode, result.getAvailableCodeList().get(0));
    }

    @Test
    void getSegregationCodesSuccess() throws IOException, JSONException {
        PaStazionePa stationWithoutSegregationCode = getMockPaStazionePa();
        stationWithoutSegregationCode.setSegregazione(null);
        stationWithoutSegregationCode.getFkStazione().setIdStazione("nosegcodestation");
        List<PaStazionePa> stations = List.of(getMockPaStazionePa(), stationWithoutSegregationCode);

        when(paRepository.findByIdDominio(TARGET_CI_TAX_CODE)).thenReturn(Optional.of(getMockPa()));
        when(ciStationRepository.findByFkPa(anyLong())).thenReturn(stations);

        AvailableCodes result = assertDoesNotThrow(() ->
                creditorInstitutionsService.getAvailableCISegregationCodes(CI_TAX_CODE, TARGET_CI_TAX_CODE)
        );

        assertNotNull(result);
        assertNotNull(result.getAvailableCodeList());
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.readJsonFromFile("response/get_creditorinstitution_segregationcodes_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getSegregationCodes_404() {
        when(paRepository.findByIdDominio("12345")).thenReturn(Optional.empty());

        AppException e = assertThrows(AppException.class, () ->
                creditorInstitutionsService.getAvailableCISegregationCodes(CI_TAX_CODE, TARGET_CI_TAX_CODE)
        );

        assertNotNull(e);
        assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
    }

    @Test
    void getCreditorInstitutionInfoListSuccess() throws IOException {
        Pa pa = getMockPa();
        List<String> taxCodes = Collections.singletonList("168480242");
        when(paRepository.findByIdDominioIn(taxCodes)).thenReturn(Optional.of(Collections.singletonList(pa)));

        List<CreditorInstitutionInfo> result = creditorInstitutionsService.getCreditorInstitutionInfoList(taxCodes);

        assertNotNull(result);
        assertEquals(1, result.size());
        CreditorInstitutionInfo ci = result.get(0);
        assertEquals(pa.getIdDominio(), ci.getCiTaxCode());
        assertEquals(pa.getRagioneSociale(), ci.getBusinessName());
    }

    @Test
    void getCreditorInstitutionInfoListFailNotFound() {
        List<String> taxCodes = Collections.singletonList("168480242");
        when(paRepository.findByIdDominioIn(taxCodes)).thenReturn(Optional.empty());

        AppException e = assertThrows(AppException.class, () -> creditorInstitutionsService.getCreditorInstitutionInfoList(taxCodes));

        assertNotNull(e);
        assertEquals(AppError.MULTIPLE_CREDITOR_INSTITUTIONS_NOT_FOUND.title, e.getTitle());
        assertEquals(AppError.MULTIPLE_CREDITOR_INSTITUTIONS_NOT_FOUND.httpStatus, e.getHttpStatus());
    }

    @Test
    void getCreditorInstitutionInfoListFailInternalServerError() throws IOException {
        Pa pa = getMockPa();
        List<String> taxCodes = new ArrayList<>();
        taxCodes.add("168480242");
        taxCodes.add("168480243");
        when(paRepository.findByIdDominioIn(taxCodes)).thenReturn(Optional.of(Collections.singletonList(pa)));

        AppException e = assertThrows(AppException.class, () -> creditorInstitutionsService.getCreditorInstitutionInfoList(taxCodes));

        assertNotNull(e);
        assertEquals(AppError.INTERNAL_SERVER_ERROR.title, e.getTitle());
        assertEquals(AppError.INTERNAL_SERVER_ERROR.httpStatus, e.getHttpStatus());
    }

    @Test
    void getStationCreditorInstitutionsSuccess() throws IOException {
        Stazioni stazioni = getMockStazioni();
        PaStazionePa paStazionePa = getMockPaStazionePa();
        Pa pa = getMockPa();
        List<String> ciTaxCodes = List.of(CI_TAX_CODE, pa.getIdDominio());
        List<Pa> paList = List.of(pa, Pa.builder().idDominio(CI_TAX_CODE).ragioneSociale(RAGIONE_SOCIALE).build());

        when(stationRepository.findByIdStazione(STATION_CODE)).thenReturn(Optional.of(stazioni));
        when(paRepository.findByIdDominioIn(ciTaxCodes)).thenReturn(Optional.of(paList));
        when(ciStationRepository.findByFkStazioneAndPaIn(stazioni, paList)).thenReturn(Collections.singletonList(paStazionePa));

        List<CreditorInstitutionInfo> result = assertDoesNotThrow(() ->
                creditorInstitutionsService.getStationCreditorInstitutions(STATION_CODE, ciTaxCodes));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RAGIONE_SOCIALE, result.get(0).getBusinessName());
        assertEquals(CI_TAX_CODE, result.get(0).getCiTaxCode());
    }

    @Test
    void getStationCreditorInstitutionsFailStationNotFound() {
        List<String> ciTaxCodes = Collections.singletonList(CI_TAX_CODE);

        when(stationRepository.findByIdStazione(STATION_CODE)).thenReturn(Optional.empty());

        AppException e = assertThrows(AppException.class, () -> creditorInstitutionsService.getStationCreditorInstitutions(STATION_CODE, ciTaxCodes));

        assertNotNull(e);
        assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());

        verify(paRepository, never()).findByIdDominioIn(anyList());
        verify(ciStationRepository, never()).findByFkStazioneAndPaIn(any(), anyList());
    }

    @Test
    void getStationCreditorInstitutionsFailPaNotFound() throws IOException {
        List<String> ciTaxCodes = Collections.singletonList(CI_TAX_CODE);
        Stazioni stazioni = getMockStazioni();

        when(stationRepository.findByIdStazione(STATION_CODE)).thenReturn(Optional.of(stazioni));
        when(paRepository.findByIdDominioIn(ciTaxCodes)).thenReturn(Optional.empty());

        AppException e = assertThrows(AppException.class, () -> creditorInstitutionsService.getStationCreditorInstitutions(STATION_CODE, ciTaxCodes));

        assertNotNull(e);
        assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());

        verify(ciStationRepository, never()).findByFkStazioneAndPaIn(any(), anyList());
    }
}
