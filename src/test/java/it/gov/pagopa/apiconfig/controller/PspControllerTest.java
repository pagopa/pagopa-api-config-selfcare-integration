package it.gov.pagopa.apiconfig.controller;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.service.PspService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static it.gov.pagopa.apiconfig.util.TestUtil.getMockChannelDetailsList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class PspControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PspService pspService;

    @BeforeEach
    void setup() throws IOException {
        when(pspService.getChannelByFiscalCode(
                "CF70000000001", PageRequest.of(0, 10)))
                .thenReturn(getMockChannelDetailsList());
    }

    @ParameterizedTest
    @CsvSource({
            "/payment-service-providers/CF70000000001/channels?limit=10&page=0",
    })
    void getChannelByFiscalCode(String url) throws Exception {
        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
