package it.gov.pagopa.apiconfig.controller;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.service.IbansService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockIbanList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class IbanControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IbansService ibansService;
    @BeforeEach
    void setup() throws IOException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        when(ibansService.getIbans(List.of("168480242"), PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "fkPa", "objId"))))
                .thenReturn(getMockIbanList());
    }

    @ParameterizedTest
    @CsvSource({
            "/ibans?limit=10&page=0&ci_list=168480242",
    })
    void testGetWithCIs(String url) throws Exception {
        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @CsvSource({
            "/ibans?limit=10&page=0",
    })
    void testGetWithoutCIs(String url) throws Exception {
        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
