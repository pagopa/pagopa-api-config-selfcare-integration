package it.gov.pagopa.apiconfig.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.StationDetails;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class TestUtil {

  @Autowired
  private ObjectMapper objectMapper;
  public static StationDetailsList getMockStationDetailsList()
      throws IOException {
    return StationDetailsList.builder()
        .stationsDetailsList(List.of(getMockStationDetails()))
        .build();
  }

  public static StationDetails getMockStationDetails() throws IOException {
    String mockPa = readJsonFromFile("request/get_station_details_ok1.json");
    return new ObjectMapper().readValue(mockPa, StationDetails.class);
  }

  public static Pa getMockPa() throws IOException {
    String mockPa = readJsonFromFile("request/get_pa_ok1.json");
    return new ObjectMapper().readValue(mockPa, Pa.class);
  }

  public static Stazioni getMockStazioni() throws IOException {
    String mockStation = readJsonFromFile("request/get_station_ok1.json");
    return new ObjectMapper().readValue(mockStation, Stazioni.class);
  }

  public static IntermediariPa getMockBroker() throws IOException {
    String mockStation = readJsonFromFile("request/get_broker_ok1.json");
    return new ObjectMapper().readValue(mockStation, IntermediariPa.class);
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
