package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.Application;
import it.gov.pagopa.apiconfig.selfcareintegration.model.PageInfo;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbansList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedIbanMasterRepository;
import it.gov.pagopa.apiconfig.starter.entity.IbanMaster;
import it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockIbanMaster;
import static it.gov.pagopa.apiconfig.selfcareintegration.util.TestUtil.getMockIbanMaster2;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class)
class IbanServiceTest {

    @MockBean
    private ExtendedIbanMasterRepository extendedIbanMasterRepository;

    @Mock
    private Pageable pageable;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @InjectMocks
    private IbansService ibansService;


    @BeforeEach
    void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void getIbans_singleCI_200() throws IOException, JSONException {

        List<String> ciList = List.of("168480242");
        Page<IbanMaster> page = TestUtil.mockPage(Lists.newArrayList(getMockIbanMaster()), 10, 0);

        when(extendedIbanMasterRepository.findAllByPa_idDominioIn(anyList(), any(Pageable.class)))
                .thenReturn(page);

        IbansList result = ibansService.getIbans(ciList, pageable);
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.readJsonFromFile("response/get_ibans_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getIbans_multipleCIOnlyOneExists_200() throws IOException, JSONException {

        List<String> ciList = List.of("168480242", "99999999990");
        Page<IbanMaster> page = TestUtil.mockPage(Lists.newArrayList(getMockIbanMaster()), 10, 0);

        when(extendedIbanMasterRepository.findAllByPa_idDominioIn(anyList(), any(Pageable.class)))
                .thenReturn(page);

        IbansList result = ibansService.getIbans(ciList, pageable);
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.readJsonFromFile("response/get_ibans_ok1.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getIbans_multipleCI_200() throws IOException, JSONException {

        List<String> ciList = List.of("168480242", "99999999999");
        List<IbanMaster> ibanMaster = getMockIbanMaster2();
        Pa pa = Pa.builder()
                .objId(100L)
                .idDominio("99999999999")
                .enabled(true)
                .ragioneSociale("Comune di Ventimiglia")
                .indirizzoDomicilioFiscale("Via Roma 1")
                .capDomicilioFiscale("23456")
                .siglaProvinciaDomicilioFiscale("VE")
                .comuneDomicilioFiscale("Bassano del Grappa")
                .denominazioneDomicilioFiscale("Via Roma 1")
                .description("Bassano del Grappa")
                .build();
        ibanMaster.get(0).getIban().setPa(pa);
        Page<IbanMaster> page = TestUtil.mockPage(Lists.newArrayList(ibanMaster), 10, 0);

        when(extendedIbanMasterRepository.findAllByPa_idDominioIn(anyList(), any(Pageable.class)))
                .thenReturn(page);

        IbansList result = ibansService.getIbans(ciList, pageable);
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.readJsonFromFile("response/get_ibans_ok2.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getIbans_noIban_200() throws IOException, JSONException {

        List<String> ciList = List.of("99999999990");
        Page<IbanMaster> page = TestUtil.mockPage(List.of(), 10, 0);
        IbansList pageInfoOnlyResult = IbansList.builder()
                .ibans(List.of())
                .pageInfo(PageInfo.builder().page(0).limit(10).totalPages(0).totalItems(0L).itemsFound(0).build())
                .build();

        when(extendedIbanMasterRepository.findAllByPa_idDominioIn(anyList(), any(Pageable.class)))
                .thenReturn(page);

        IbansList result = ibansService.getIbans(ciList, pageable);
        String actual = TestUtil.toJson(result);
        String expected = TestUtil.toJson(pageInfoOnlyResult);
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    void getIbans_invalidParameters_400() {
        assertThrows(ConstraintViolationException.class, () -> ibansService.getIbans(List.of(), pageable));
        assertThrows(ConstraintViolationException.class, () -> ibansService.getIbans(List.of("168480242", "99999999999"), null));
    }
}
