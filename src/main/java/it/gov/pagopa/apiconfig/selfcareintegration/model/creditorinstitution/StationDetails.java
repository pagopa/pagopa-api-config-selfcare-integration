package it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/** StationDetails */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationDetails extends Station {

  @JsonProperty("ip")
  private String ip;

  @ToString.Exclude
  @JsonProperty("new_password")
  private String newPassword;

  @ToString.Exclude
  @JsonProperty("password")
  private String password;

  @Min(1)
  @Max(65535)
  @JsonProperty("port")
  @NotNull
  private Long porta;

  @JsonProperty("protocol")
  @NotNull
  private Protocol protocollo;

  @JsonProperty("redirect_ip")
  private String redirectIp;

  @JsonProperty("redirect_path")
  private String redirectPath;

  @Min(1)
  @Max(65535)
  @JsonProperty("redirect_port")
  private Long redirectPort;

  @JsonProperty("redirect_query_string")
  private String redirectQueryString;

  @JsonProperty("redirect_protocol")
  private Protocol redirectProtocollo;

  @JsonProperty("service")
  private String servizio;

  @JsonProperty("pof_service")
  private String servizioPof;

  @JsonProperty("broker_details")
  @NotBlank
  private BrokerDetails intermediarioPa;

  @JsonProperty("protocol_4mod")
  private Protocol protocollo4Mod;

  @JsonProperty("ip_4mod")
  private String ip4Mod;

  @Min(1)
  @Max(65535)
  @JsonProperty("port_4mod")
  private Long porta4Mod;

  @JsonProperty("service_4mod")
  private String servizio4Mod;

  @JsonProperty("proxy_enabled")
  private Boolean proxyEnabled;

  @JsonProperty("proxy_host")
  private String proxyHost;

  @Min(1)
  @Max(65535)
  @JsonProperty("proxy_port")
  private Long proxyPort;

  @JsonProperty("proxy_username")
  private String proxyUsername;

  @ToString.Exclude
  @JsonProperty("proxy_password")
  private String proxyPassword;

  @Min(1)
  @JsonProperty("thread_number")
  @NotNull
  private Long numThread;

  @Min(0)
  @JsonProperty("timeout_a")
  @NotNull
  private Long timeoutA = 15L;

  @Min(0)
  @JsonProperty("timeout_b")
  @NotNull
  private Long timeoutB = 30L;

  @Min(0)
  @JsonProperty("timeout_c")
  @NotNull
  private Long timeoutC = 120L;

  @JsonProperty("flag_online")
  private Boolean flagOnline;

  @JsonIgnore private Long brokerObjId;

  @JsonProperty("invio_rt_istantaneo")
  private Boolean invioRtIstantaneo;

  @JsonProperty("target_host")
  private String targetHost;

  @JsonProperty("target_port")
  private Long targetPort;

  @JsonProperty("target_path")
  private String targetPath;

  @JsonProperty("target_host_pof")
  private String targetHostPof;

  @JsonProperty("target_port_pof")
  private Long targetPortPof;

  @JsonProperty("target_path_pof")
  private String targetPathPof;

  @Min(1)
  @Max(2)
  @NotNull
  @Schema(description = "Primitive number version")
  @JsonProperty("primitive_version")
  private Integer versionePrimitive;
}
