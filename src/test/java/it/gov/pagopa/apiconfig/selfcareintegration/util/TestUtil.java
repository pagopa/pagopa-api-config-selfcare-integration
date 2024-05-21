package it.gov.pagopa.apiconfig.selfcareintegration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.gov.pagopa.apiconfig.selfcareintegration.model.PageInfo;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.AvailableCodes;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCode;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCodeList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetail;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbanDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbanLabel;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbansList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.starter.entity.*;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.when;

@UtilityClass
public class TestUtil {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> Page<T> mockPage(List<T> content, int limit, int pageNumber) {
        @SuppressWarnings("unchecked")
        Page<T> page = Mockito.mock(Page.class);
        when(page.getTotalPages()).thenReturn((int) Math.ceil((double) content.size() / limit));
        when(page.getNumberOfElements()).thenReturn(content.size());
        when(page.getNumber()).thenReturn(pageNumber);
        when(page.getTotalElements()).thenReturn((long) content.size());
        when(page.getSize()).thenReturn(limit);
        when(page.getContent()).thenReturn(content);
        when(page.stream()).thenReturn(content.stream());
        return page;
    }

    public static StationDetailsList getMockStationDetailsList() throws IOException {
        List<StationDetails> stationDetails = List.of(getMockStationDetails());
        return StationDetailsList.builder()
                .stationsDetailsList(stationDetails)
                .pageInfo(getMockPageInfo(1, 10, stationDetails.size()))
                .build();
    }

    public static CreditorInstitutionDetails getMockCreditorInstitutionDetails() throws IOException {
        List<CreditorInstitutionDetail> creditorInstitutionDetails = List.of(getMockCreditorInstitutionDetail());
        return CreditorInstitutionDetails.builder()
                .creditorInstitutions(creditorInstitutionDetails)
                .pageInfo(getMockPageInfo(1, 10, creditorInstitutionDetails.size()))
                .build();
    }

    public static CIAssociatedCodeList getMockApplicationCodesList() throws IOException {
        return CIAssociatedCodeList.builder()
                .usedCodes(List.of(getMockUsedApplicationCodesList()))
                .unusedCodes(List.of(getMockUnusedApplicationCodesList()))
                .build();
    }

    public static ChannelDetailsList getMockChannelDetailsList() throws IOException {
        List<ChannelDetails> channelDetails = List.of(getMockChannelDetails());
        return ChannelDetailsList.builder()
                .channelsDetailsList(channelDetails)
                .pageInfo(getMockPageInfo(1, 10, channelDetails.size()))
                .build();
    }

    public static PageInfo getMockPageInfo(int totalPages, int limit, int size) {
        return PageInfo.builder().page(0).limit(limit).totalPages(totalPages).itemsFound(size).build();
    }

    public static StationDetails getMockStationDetails() throws IOException {
        return getMockRequest("request/get_station_details_ok1.json", StationDetails.class);
    }

    public static CreditorInstitutionDetail getMockCreditorInstitutionDetail() throws IOException {
        return getMockRequest("request/get_creditor_institution_detail_ok1.json", CreditorInstitutionDetail.class);
    }

    public static CIAssociatedCode getMockUnusedApplicationCodesList() throws IOException {
        return getMockRequest("request/get_application_codes_ok1.json", CIAssociatedCode.class);
    }

    public static CIAssociatedCode getMockUsedApplicationCodesList() throws IOException {
        return getMockRequest("request/get_application_codes_ok2.json", CIAssociatedCode.class);
    }

    public static AvailableCodes getMockUsedSegregationCodesList() throws IOException {
        return getMockRequest("response/get_creditorinstitution_segregationcodes_ok2.json", AvailableCodes.class);
    }

    public static ChannelDetails getMockChannelDetails() throws IOException {
        return getMockRequest("request/get_channel_details_ok1.json", ChannelDetails.class);
    }

    public static Pa getMockPa() throws IOException {
        return getMockRequest("request/get_pa_ok1.json", Pa.class);
    }

    public static Stazioni getMockStazioni() throws IOException {
        return getMockRequest("request/get_station_ok1.json", Stazioni.class);
    }

    public static List<PaStazionePa> getMockPaStazionePa(Boolean enabledStation) {
        List<PaStazionePa> paStazionePa = new ArrayList<>();
        PaStazionePa enabled = PaStazionePa.builder()
                .pa(Pa.builder()
                        .ragioneSociale("Comune di Roma")
                        .idDominio("02438750586")
                        .cbill("APNEY")
                        .build())
                .fkStazione(
                        Stazioni.builder()
                                .intermediarioPa(
                                        IntermediariPa.builder()
                                                .codiceIntermediario("Regione Lazio")
                                                .idIntermediarioPa("80143490581")
                                                .build()
                                )
                                .idStazione("80143490581_01")
                                .enabled(true)
                                .versione(2L)
                                .build()
                )
                .segregazione(11L)
                .auxDigit(3L)
                .broadcast(false)
                .build();
        PaStazionePa disabled = PaStazionePa.builder()
                .pa(Pa.builder()
                        .ragioneSociale("Comune di Roma")
                        .idDominio("02438750586")
                        .cbill("APNEY")
                        .build())
                .fkStazione(
                        Stazioni.builder()
                                .intermediarioPa(
                                        IntermediariPa.builder()
                                                .codiceIntermediario("Regione Lazio")
                                                .idIntermediarioPa("80143490581")
                                                .build()
                                )
                                .idStazione("80143490581_02")
                                .enabled(false)
                                .versione(2L)
                                .build()
                )
                .segregazione(2L)
                .auxDigit(3L)
                .broadcast(false)
                .build();

        if (enabledStation == null) {
            paStazionePa.add(enabled);
            paStazionePa.add(disabled);
        } else if (enabledStation) {
            paStazionePa.add(enabled);
        } else {
            paStazionePa.add(disabled);
        }
        return paStazionePa;
    }

    public static Canali getMockChannel() throws IOException {
        return getMockRequest("request/get_channel_ok1.json", Canali.class);
    }

    public static Canali getMockChannelMapping() throws IOException {
        return getMockRequest("request/get_channel_ok2.json", Canali.class);
    }

    public static IntermediariPa getMockBroker() throws IOException {
        return getMockRequest("request/get_broker_ok1.json", IntermediariPa.class);
    }

    public static IntermediariPsp getMockPSPBroker() throws IOException {
        return getMockRequest("request/get_broker_ok2.json", IntermediariPsp.class);
    }

    public static PaStazionePa getMockPaStazionePa() throws IOException {
        return PaStazionePa.builder()
                .objId(1L)
                .pa(getMockPa())
                .fkPa(getMockPa().getObjId())
                .fkStazione(getMockStazioni())
                .broadcast(false)
                .auxDigit(1L)
                .progressivo(2L)
                .quartoModello(true)
                .segregazione(3L)
                .build();
    }

    public static List<IbanMaster> getMockIbanMaster() {
        Pa pa = Pa.builder()
                .objId(100L)
                .idDominio("168480242")
                .enabled(true)
                .ragioneSociale("Comune di Bassano del Grappa")
                .indirizzoDomicilioFiscale("Via Roma 1")
                .capDomicilioFiscale("23456")
                .siglaProvinciaDomicilioFiscale("VE")
                .comuneDomicilioFiscale("Bassano del Grappa")
                .denominazioneDomicilioFiscale("Via Roma 1")
                .description("Bassano del Grappa")
                .build();
        return List.of(
                IbanMaster.builder()
                        .objId(1L)
                        .ibanStatus(IbanMaster.IbanStatus.NA)
                        .insertedDate(Timestamp.valueOf("2023-01-01 12:00:00.000"))
                        .validityDate(Timestamp.valueOf("2023-01-01 12:00:00.000"))
                        .description("Cassa comunale Bassano del Grappa")
                        .iban(Iban.builder()
                                .objId(10L)
                                .iban("IT01X02933019297465283757")
                                .fiscalCode("168480242")
                                .dueDate(Timestamp.valueOf("2035-12-31 23:59:59.999"))
                                .description("iban description")
                                .build()
                        )
                        .ibanAttributesMasters(List.of())
                        .pa(pa)
                        .build(),
                IbanMaster.builder()
                        .objId(2L)
                        .ibanStatus(IbanMaster.IbanStatus.NA)
                        .insertedDate(Timestamp.valueOf("2023-01-01 12:00:00.000"))
                        .validityDate(Timestamp.valueOf("2023-01-01 12:00:00.000"))
                        .description("Cassa comunale Bassano del Grappa - CUP e StandIn")
                        .iban(Iban.builder()
                                .objId(11L)
                                .iban("IT01X02933019297465283758")
                                .fiscalCode("168480242")
                                .dueDate(Timestamp.valueOf("2035-12-31 23:59:59.999"))
                                .description("iban description")
                                .build()
                        )
                        .ibanAttributesMasters(List.of(
                                IbanAttributeMaster.builder()
                                        .objId(1000L)
                                        .ibanAttribute(
                                                IbanAttribute.builder()
                                                        .attributeName("0201138TS")
                                                        .attributeDescription("CUP")
                                                        .build()
                                        )
                                        .build(),
                                IbanAttributeMaster.builder()
                                        .objId(1001L)
                                        .ibanAttribute(
                                                IbanAttribute.builder()
                                                        .attributeName("ACA")
                                                        .attributeDescription("StandIn")
                                                        .build()
                                        )
                                        .build()
                        ))
                        .pa(pa)
                        .build()
        );
    }

    public static List<IbanMaster> getMockIbanMaster2() {
        Pa pa = Pa.builder()
                .objId(100L)
                .idDominio("168480242")
                .enabled(true)
                .ragioneSociale("Comune di Bassano del Grappa")
                .indirizzoDomicilioFiscale("Via Roma 1")
                .capDomicilioFiscale("23456")
                .siglaProvinciaDomicilioFiscale("VE")
                .comuneDomicilioFiscale("Bassano del Grappa")
                .denominazioneDomicilioFiscale("Via Roma 1")
                .description("Bassano del Grappa")
                .build();
        return List.of(
                IbanMaster.builder()
                        .objId(3L)
                        .ibanStatus(IbanMaster.IbanStatus.NA)
                        .insertedDate(Timestamp.valueOf("2023-02-01 12:00:00.000"))
                        .validityDate(Timestamp.valueOf("2023-02-01 12:00:00.000"))
                        .description("Cassa comunale Ventimiglia")
                        .iban(Iban.builder()
                                .objId(12L)
                                .iban("IT01X02933019297465283888")
                                .fiscalCode("99999999999")
                                .dueDate(Timestamp.valueOf("2025-12-31 23:59:59.999"))
                                .description("iban description")
                                .build()
                        )
                        .ibanAttributesMasters(List.of())
                        .pa(Pa.builder()
                                .objId(102L)
                                .idDominio("99999999999")
                                .enabled(true)
                                .ragioneSociale("Comune di Ventimiglia")
                                .indirizzoDomicilioFiscale("Via Palermo 1")
                                .capDomicilioFiscale("78901")
                                .siglaProvinciaDomicilioFiscale("GE")
                                .comuneDomicilioFiscale("Ventimiglia")
                                .denominazioneDomicilioFiscale("Via Palermo 1")
                                .description("Ventimiglia")
                                .build()
                        )
                        .build(),
                IbanMaster.builder()
                        .objId(1L)
                        .ibanStatus(IbanMaster.IbanStatus.NA)
                        .insertedDate(Timestamp.valueOf("2023-01-01 12:00:00.000"))
                        .validityDate(Timestamp.valueOf("2023-01-01 12:00:00.000"))
                        .description("Cassa comunale Bassano del Grappa")
                        .iban(Iban.builder()
                                .objId(10L)
                                .iban("IT01X02933019297465283757")
                                .fiscalCode("168480242")
                                .dueDate(Timestamp.valueOf("2035-12-31 23:59:59.999"))
                                .description("iban description")
                                .build()
                        )
                        .ibanAttributesMasters(List.of())
                        .pa(pa)
                        .build(),
                IbanMaster.builder()
                        .objId(2L)
                        .ibanStatus(IbanMaster.IbanStatus.NA)
                        .insertedDate(Timestamp.valueOf("2023-01-01 12:00:00.000"))
                        .validityDate(Timestamp.valueOf("2023-01-01 12:00:00.000"))
                        .description("Cassa comunale Bassano del Grappa - CUP e StandIn")
                        .iban(Iban.builder()
                                .objId(11L)
                                .iban("IT01X02933019297465283758")
                                .fiscalCode("168480242")
                                .dueDate(Timestamp.valueOf("2035-12-31 23:59:59.999"))
                                .description("iban description")
                                .build()
                        )
                        .ibanAttributesMasters(List.of(
                                IbanAttributeMaster.builder()
                                        .objId(1000L)
                                        .ibanAttribute(
                                                IbanAttribute.builder()
                                                        .attributeName("0201138TS")
                                                        .attributeDescription("CUP")
                                                        .build()
                                        )
                                        .build(),
                                IbanAttributeMaster.builder()
                                        .objId(1001L)
                                        .ibanAttribute(
                                                IbanAttribute.builder()
                                                        .attributeName("ACA")
                                                        .attributeDescription("StandIn")
                                                        .build()
                                        )
                                        .build()
                        ))
                        .pa(pa)
                        .build()
        );
    }

    public static IbansList getMockIbanList() {
        return IbansList.builder()
                .ibans(List.of(
                        IbanDetails.builder()
                                .creditorInstitutionFiscalCode("168480242")
                                .creditorInstitutionName("Comune di Bassano del Grappa")
                                .insertedDate(OffsetDateTime.ofInstant(Instant.ofEpochMilli(1672570800000L), ZoneId.of("UTC")))
                                .validityDate(OffsetDateTime.ofInstant(Instant.ofEpochMilli(1672570800000L), ZoneId.of("UTC")))
                                .dueDate(OffsetDateTime.ofInstant(Instant.ofEpochMilli(2082754799000L), ZoneId.of("UTC")))
                                .description("Cassa comunale Bassano del Grappa")
                                .ownerFiscalCode("168480242")
                                .labels(List.of())
                                .build(),
                        IbanDetails.builder()
                                .creditorInstitutionFiscalCode("168480242")
                                .creditorInstitutionName("Comune di Bassano del Grappa")
                                .insertedDate(OffsetDateTime.ofInstant(Instant.ofEpochMilli(1672570800000L), ZoneId.of("UTC")))
                                .validityDate(OffsetDateTime.ofInstant(Instant.ofEpochMilli(1672570800000L), ZoneId.of("UTC")))
                                .dueDate(OffsetDateTime.ofInstant(Instant.ofEpochMilli(2082754799000L), ZoneId.of("UTC")))
                                .description("Cassa comunale Bassano del Grappa - CUP e StandIn")
                                .ownerFiscalCode("168480242")
                                .labels(List.of(
                                        IbanLabel.builder().name("0201138TS").description("CUP").build(),
                                        IbanLabel.builder().name("ACA").description("StandIn").build()
                                ))
                                .build()
                ))
                .pageInfo(PageInfo.builder().page(0).limit(10).totalPages(1).itemsFound(2).totalItems(2L).build())
                .build();
    }

    public static <T> T getMockRequest(String requestPath, Class<T> clazz) throws IOException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        String mockData = readJsonFromFile(requestPath);
        return mapper.readValue(mockData, clazz);
    }

    /**
     * @param object to map into the Json string
     * @return object as Json string
     * @throws JsonProcessingException if there is an error during the parsing of the object
     */
    public String toJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        return mapper.writeValueAsString(object);
    }

    /**
     * @param relativePath Path from source root of the json file
     * @return the Json string read from the file
     * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable
     *                     byte sequence is read
     */
    public String readJsonFromFile(String relativePath) throws IOException {
        ClassLoader classLoader = TestUtil.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(relativePath)).getPath());
        return Files.readString(file.toPath());
    }
}
