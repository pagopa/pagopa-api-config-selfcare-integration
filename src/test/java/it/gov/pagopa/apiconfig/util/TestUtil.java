package it.gov.pagopa.apiconfig.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.BrokerDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionStationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.Protocol;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.StationDetails;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import lombok.experimental.UtilityClass;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.when;

@UtilityClass
public class TestUtil {
  public static CreditorInstitutionStationDetailsList getMockCreditorInstitutionStationDetailsList() {
    return CreditorInstitutionStationDetailsList.builder()
        .stationsDetailsList(List.of(getMockStationDetails()))
        .build();
  }

  public static StationDetails getMockStationDetails() {
    return StationDetails.builder()
        .idStazione("1234")
        .ip("1.1.1.1")
        .porta(80L)
        .protocollo(Protocol.HTTP)
        .password("pass")
        .timeoutA(1L)
        .timeoutB(1L)
        .timeoutC(1L)
        .versione(1L)
        .enabled(true)
        .intermediarioPa(getMockBrokerDetails())
        .flagOnline(true)
        .ip4Mod("2.2.2.2")
        .servizio("/api")
        .numThread(2L)
        .invioRtIstantaneo(false)
        .versionePrimitive(1)
        .build();
  }

  public static BrokerDetails getMockBrokerDetails() {
    return BrokerDetails.builder()
        .idIntermediarioPa("4321")
        .codiceIntermediario("Regione Veneto")
        .enabled(false)
        .faultBeanEsteso(false)
        .build();
  }

  public static Pa getMockPa() {
    return Pa.builder()
        .objId(1L)
        .idDominio("00168480242")
        .enabled(true)
        .ragioneSociale("Comune di Bassano del Grappa")
        .capDomicilioFiscale("00111")
        .pagamentoPressoPsp(true)
        .rendicontazioneFtp(false)
        .rendicontazioneZip(false)
        .build();
  }

  public static PaStazionePa getMockPaStazionePa() {
    return PaStazionePa.builder()
        .objId(1L)
        .pa(getMockPa())
        .fkPa(getMockPa().getObjId())
        .fkStazione(getMockStazioni().getObjId())
        .stazione(getMockStazioni())
        .broadcast(false)
        .auxDigit(1L)
        .progressivo(2L)
        .quartoModello(true)
        .segregazione(3L)
        .build();
  }

  public static Stazioni getMockStazioni() {
    return Stazioni.builder()
        .objId(2L)
        .idStazione("80007580279_01")
        .enabled(true)
        .versione(1L)
        .ip("NodoDeiPagamentiDellaPATest.sia.eu")
        .password("password")
        .porta(80L)
        .redirectIp("paygov.collaudo.regione.veneto.it")
        .redirectPath("nodo-regionale-fesp/paaInviaRispostaPagamento.html")
        .redirectPort(443L)
        .servizio("openspcoop/PD/RT6TPDREGVENETO")
        .rtEnabled(true)
        .servizioPof("openspcoop/PD/CCP6TPDREGVENETO")
        .intermediarioPa(getMockIntermediariePa())
        .fkIntermediarioPa(1L)
        .redirectProtocollo("HTTPS")
        .proxyEnabled(true)
        .proxyHost("10.101.1.95")
        .proxyPort(8080L)
        .numThread(2L)
        .timeoutA(15L)
        .timeoutB(30L)
        .timeoutC(120L)
        .flagOnline(true)
        .protocollo("HTTPS")
        .protocollo4Mod("HTTPS")
        .invioRtIstantaneo(false)
        .targetHost("localhost")
        .targetPort(443L)
        .targetPath("/")
        .targetHostPof("localhost")
        .targetPortPof(443L)
        .targetPathPof("/")
        .versionePrimitive(1)
        .build();
  }

  public static IntermediariPa getMockIntermediariePa() {
    return IntermediariPa.builder()
        .objId(1L)
        .codiceIntermediario("Regione Lazio")
        .enabled(true)
        .faultBeanEsteso(true)
        .idIntermediarioPa("1234")
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
