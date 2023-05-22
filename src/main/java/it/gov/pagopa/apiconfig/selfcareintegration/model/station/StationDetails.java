package it.gov.pagopa.apiconfig.selfcareintegration.model.station;

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
  @Schema(description = "Ip address of the station")
  private String ip;

  @ToString.Exclude
  @JsonProperty("new_password")
  @Schema(description = "New password of the station")
  private String newPassword;

  @ToString.Exclude
  @JsonProperty("password")
  @Schema(description = "Password of the station")
  private String password;

  @Min(1)
  @Max(65535)
  @JsonProperty("port")
  @Schema(description = "Port address of the station")
  @NotNull
  private Long porta;

  @JsonProperty("protocol")
  @Schema(description = "Protocol associated to the station")
  @NotNull
  private Protocol protocollo;

  @JsonProperty("redirect_ip")
  @Schema(description = "Redirect ip address of the station")
  private String redirectIp;

  @JsonProperty("redirect_path")
  @Schema(description = "Redirect path of the station")
  private String redirectPath;

  @Min(1)
  @Max(65535)
  @JsonProperty("redirect_port")
  @Schema(description = "Redirect port address of the station")
  private Long redirectPorta;

  @JsonProperty("redirect_query_string")
  @Schema(description = "Redirect query string of the station")
  private String redirectQueryString;

  @JsonProperty("redirect_protocol")
  @Schema(description = "Redirect protocol associated to the station")
  private Protocol redirectProtocollo;

  @JsonProperty("service")
  private String servizio;

  @JsonProperty("pof_service")
  private String servizioPof;

  @JsonProperty("broker_details")
  @Schema(description = "Details of the intermediate EC of the station")
  @NotBlank
  private BrokerDetails intermediarioPa;

  @JsonProperty("protocol_4mod")
  @Schema(description = "Protocol 4mod associated to the station")
  private Protocol protocollo4Mod;

  @JsonProperty("ip_4mod")
  @Schema(description = "Ip address 4mod associated to the station")
  private String ip4Mod;

  @Min(1)
  @Max(65535)
  @JsonProperty("port_4mod")
  @Schema(description = "Port address 4mod associated to the station")
  private Long porta4Mod;

  @JsonProperty("service_4mod")
  private String servizio4Mod;

  @JsonProperty("proxy_enabled")
  @Schema(description = "Parameter to inspect if the proxy has been enabled for this station")
  private Boolean proxyEnabled;

  @JsonProperty("proxy_host")
  @Schema(description = "Proxy host")
  private String proxyHost;

  @Min(1)
  @Max(65535)
  @JsonProperty("proxy_port")
  @Schema(description = "Proxy port address")
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

  @JsonIgnore
  @Schema(description = "Intermediate EC broker id")
  private Long brokerObjId;

  @JsonProperty("invio_rt_istantaneo")
  @Schema(description = "Parameter useful to find out if the instantaneous rt has been enabled")
  private Boolean invioRtIstantaneo;

  @JsonProperty("target_host")
  @Schema(description = "Target address of the station")
  private String targetHost;

  @JsonProperty("target_port")
  @Schema(description = "Port address target associated to the station")
  private Long targetPort;

  @JsonProperty("target_path")
  @Schema(description = "Target path of the station")
  private String targetPath;

  @JsonProperty("target_host_pof")
  @Schema(description = "Pof address associated to the station")
  private String targetHostPof;

  @JsonProperty("target_port_pof")
  @Schema(description = "Port address pof associated to the station")
  private Long targetPortPof;

  @JsonProperty("target_path_pof")
  @Schema(description = "Pof path associated to the station")
  private String targetPathPof;

  @Min(1)
  @Max(2)
  @NotNull
  @Schema(description = "Primitive number version", allowableValues = {"1", "2"})
  @JsonProperty("primitive_version")
  private Integer versionePrimitive;
}
