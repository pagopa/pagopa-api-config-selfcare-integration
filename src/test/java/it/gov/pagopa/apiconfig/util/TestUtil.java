package it.gov.pagopa.apiconfig.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.apiconfig.selfcareintegration.model.PageInfo;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCode;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCodeList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetails;
import it.gov.pagopa.apiconfig.starter.entity.Canali;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPsp;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    when(page.getSize()).thenReturn(limit);
    when(page.getContent()).thenReturn(content);
    when(page.stream()).thenReturn(content.stream());
    return page;
  }

  public static StationDetailsList getMockStationDetailsList()
      throws IOException {
    List<StationDetails> stationDetails = List.of(getMockStationDetails());
    return StationDetailsList.builder()
        .stationsDetailsList(stationDetails)
        .pageInfo(getMockPageInfo(0, 1, 10, stationDetails.size()))
        .build();
  }

  public static CIAssociatedCodeList getMockApplicationCodesList()
      throws IOException {
    return CIAssociatedCodeList.builder()
        .usedCodes(List.of(getMockUsedApplicationCodesList()))
        .unusedCodes(List.of(getMockUnusedApplicationCodesList()))
        .build();
  }

  public static CIAssociatedCodeList getMockSegregationCodesList()
      throws IOException {
    return CIAssociatedCodeList.builder()
        .usedCodes(List.of(getMockUsedSegregationCodesList()))
        .unusedCodes(List.of(getMockUnusedSegregationCodesList()))
        .build();
  }

  public static ChannelDetailsList getMockChannelDetailsList()
      throws IOException {
    List<ChannelDetails> channelDetails = List.of(getMockChannelDetails());
    return ChannelDetailsList.builder()
        .channelsDetailsList(channelDetails)
        .pageInfo(getMockPageInfo(0, 1, 10, channelDetails.size()))
        .build();
  }

  public static PageInfo getMockPageInfo(int page, int totalPages, int limit, int size) {
    return PageInfo.builder()
        .page(0)
        .limit(limit)
        .totalPages(totalPages)
        .itemsFound(size)
        .build();
  }

  public static StationDetails getMockStationDetails() throws IOException {
    return getMockRequest("request/get_station_details_ok1.json", StationDetails.class);
  }

  public static CIAssociatedCode getMockUnusedApplicationCodesList() throws IOException {
    return getMockRequest("request/get_application_codes_ok1.json", CIAssociatedCode.class);
  }

  public static CIAssociatedCode getMockUsedApplicationCodesList() throws IOException {
    return getMockRequest("request/get_application_codes_ok2.json", CIAssociatedCode.class);
  }

  public static CIAssociatedCode getMockUsedSegregationCodesList() throws IOException {
    return getMockRequest("request/get_segregation_codes_ok2.json", CIAssociatedCode.class);
  }

  public static CIAssociatedCode getMockUnusedSegregationCodesList() throws IOException {
    return getMockRequest("request/get_segregation_codes_ok1.json", CIAssociatedCode.class);
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

  public static <T> T getMockRequest(String requestPath, Class<T> clazz) throws IOException {
    String mockChannel = readJsonFromFile(requestPath);
    return new ObjectMapper().readValue(mockChannel, clazz);
  }

  /**
   * @param object to map into the Json string
   * @return object as Json string
   * @throws JsonProcessingException if there is an error during the parsing of the object
   */
  public String toJson(Object object) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(object);
  }

  /**
   * @param relativePath Path from source root of the json file
   * @return the Json string read from the file
   * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable
   *     byte sequence is read
   */
  public String readJsonFromFile(String relativePath) throws IOException {
    ClassLoader classLoader = TestUtil.class.getClassLoader();
    File file = new File(Objects.requireNonNull(classLoader.getResource(relativePath)).getPath());
    return Files.readString(file.toPath());
  }
}
