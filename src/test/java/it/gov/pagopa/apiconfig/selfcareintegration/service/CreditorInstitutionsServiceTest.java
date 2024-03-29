package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.config.MappingsConfiguration;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppError;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCodeList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionInfo;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedCreditorInstitutionStationRepository;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.repository.PaRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil;
import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CreditorInstitutionsService.class, MappingsConfiguration.class})
class CreditorInstitutionsServiceTest {

    @MockBean
    private PaRepository paRepository;

    @MockBean
    private ExtendedCreditorInstitutionStationRepository ciStationRepository;

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

        when(paRepository.findByIdDominio("1234")).thenReturn(Optional.of(getMockPa()));
        when(ciStationRepository.findByFkPa(anyLong())).thenReturn(stations);

        CIAssociatedCodeList result =
                creditorInstitutionsService.getApplicationCodesFromCreditorInstitution("1234", false);
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

        when(paRepository.findByIdDominio("1234")).thenReturn(Optional.of(getMockPa()));
        when(ciStationRepository.findByFkPa(anyLong())).thenReturn(stations);

        CIAssociatedCodeList result =
                creditorInstitutionsService.getApplicationCodesFromCreditorInstitution("1234", true);
        String actual = TestUtil.toJson(result);
        String expected =
                TestUtil.readJsonFromFile("response/get_creditorinstitution_applicationcodes_ok2.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getApplicationCodes_404() {
        when(paRepository.findByIdDominio("12345")).thenReturn(Optional.empty());
        try {
            creditorInstitutionsService.getApplicationCodesFromCreditorInstitution("12345", false);
        } catch (AppException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getSegregationCodes_noUsedIncluded_200() throws IOException, JSONException {
        PaStazionePa stationWithoutSegregationCode = getMockPaStazionePa();
        stationWithoutSegregationCode.setSegregazione(null);
        stationWithoutSegregationCode.getFkStazione().setIdStazione("nosegcodestation");
        List<PaStazionePa> stations = List.of(getMockPaStazionePa(), stationWithoutSegregationCode);

        when(paRepository.findByIdDominio("1234")).thenReturn(Optional.of(getMockPa()));
        when(ciStationRepository.findByFkPa(anyLong())).thenReturn(stations);

        CIAssociatedCodeList result =
                creditorInstitutionsService.getSegregationCodesFromCreditorInstitution("1234", false, null);
        String actual = TestUtil.toJson(result);
        String expected =
                TestUtil.readJsonFromFile("response/get_creditorinstitution_segregationcodes_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getSegregationCodes_usedIncluded_200() throws IOException, JSONException {
        PaStazionePa stationWithoutSegregationCode = getMockPaStazionePa();
        stationWithoutSegregationCode.setSegregazione(null);
        stationWithoutSegregationCode.getFkStazione().setIdStazione("nosegcodestation");
        List<PaStazionePa> stations = List.of(getMockPaStazionePa(), stationWithoutSegregationCode);

        when(paRepository.findByIdDominio("1234")).thenReturn(Optional.of(getMockPa()));
        when(ciStationRepository.findByFkPa(anyLong())).thenReturn(stations);

        CIAssociatedCodeList result =
                creditorInstitutionsService.getSegregationCodesFromCreditorInstitution("1234", true, null);
        String actual = TestUtil.toJson(result);
        String expected =
                TestUtil.readJsonFromFile("response/get_creditorinstitution_segregationcodes_ok2.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getSegregationCodes_filterByService_200() throws IOException, JSONException {
        PaStazionePa stationWithoutSegregationCode = getMockPaStazionePa();
        stationWithoutSegregationCode.setSegregazione(null);
        stationWithoutSegregationCode.getFkStazione().setIdStazione("nosegcodestation");

        PaStazionePa stationMock = getMockPaStazionePa();
        stationMock.getFkStazione().setServizio("mockedService");
        stationMock.getFkStazione().setIdStazione("fakestation");
        stationMock.setSegregazione(15L);

        List<PaStazionePa> stations = List.of(stationMock, stationWithoutSegregationCode);
        when(paRepository.findByIdDominio("1234")).thenReturn(Optional.of(getMockPa()));
        when(ciStationRepository.findByFkPa(anyLong())).thenReturn(stations);

        CIAssociatedCodeList result =
                creditorInstitutionsService.getSegregationCodesFromCreditorInstitution(
                        "1234", true, "mockedser");
        String actual = TestUtil.toJson(result);
        String expected =
                TestUtil.readJsonFromFile("response/get_creditorinstitution_segregationcodes_ok3.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);

        // check if lower case check works
        result =
                creditorInstitutionsService.getSegregationCodesFromCreditorInstitution(
                        "1234", true, "MOCKEDSERVICE");
        actual = TestUtil.toJson(result);
        expected =
                TestUtil.readJsonFromFile("response/get_creditorinstitution_segregationcodes_ok3.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);

        // check if null service are ignored
        stationMock.getFkStazione().setServizio(null);
        stations = List.of(stationMock, stationWithoutSegregationCode);
        when(ciStationRepository.findByFkPa(anyLong())).thenReturn(stations);
        result =
                creditorInstitutionsService.getSegregationCodesFromCreditorInstitution(
                        "1234", true, "MOCKEDSERVICE");
        actual = TestUtil.toJson(result);
        expected =
                TestUtil.readJsonFromFile("response/get_creditorinstitution_segregationcodes_ok4.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getSegregationCodes_404() {
        when(paRepository.findByIdDominio("12345")).thenReturn(Optional.empty());
        try {
            creditorInstitutionsService.getSegregationCodesFromCreditorInstitution("12345", false, null);
        } catch (AppException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
        } catch (Exception e) {
            fail();
        }
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
        assertEquals(pa.getIdDominio(), ci.getCreditorInstitutionCode());
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
}
