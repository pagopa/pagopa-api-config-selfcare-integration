package it.gov.pagopa.apiconfig.selfcareintegration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionInfo;
import it.gov.pagopa.apiconfig.selfcareintegration.service.CreditorInstitutionsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockApplicationCodesList;
import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockStationDetailsList;
import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockUsedSegregationCodesList;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class CreditorInstitutionsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreditorInstitutionsService creditorInstitutionsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @CsvSource({
            "/creditorinstitutions/1234/stations?limit=50&page=0",
    })
    void testGetStations(String url) throws Exception {
        when(creditorInstitutionsService.getStationsDetailsFromCreditorInstitution(
                "1234", PageRequest.of(0, 50)))
                .thenReturn(getMockStationDetailsList());
        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @CsvSource({
            "/creditorinstitutions/1234/applicationcodes",
    })
    void testGetApplicationCodes(String url) throws Exception {
        when(creditorInstitutionsService.getApplicationCodesFromCreditorInstitution(
                anyString(), anyBoolean()))
                .thenReturn(getMockApplicationCodesList());
        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @CsvSource({
            "/creditorinstitutions/1234/segregationcodes",
    })
    void testGetSegregationCodes(String url) throws Exception {
        when(creditorInstitutionsService.getAvailableCISegregationCodes(anyString(), anyString()))
                .thenReturn(getMockUsedSegregationCodesList());
        mvc.perform(get(url)
                        .param("targetCITaxCode", "targetCITaxCode")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getCreditorInstitutionsTest() throws Exception {
        List<String> taxCodeList = Collections.singletonList("00168480242");

        when(creditorInstitutionsService.getCreditorInstitutionInfoList(taxCodeList))
                .thenReturn(Collections.singletonList(new CreditorInstitutionInfo()));

        mvc.perform(get("/creditorinstitutions/")
                        .param("taxCodeList", taxCodeList.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
