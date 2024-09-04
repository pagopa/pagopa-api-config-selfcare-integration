package it.gov.pagopa.apiconfig.selfcareintegration.model.station;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.gov.pagopa.apiconfig.selfcareintegration.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

/** Station */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {
  @JsonProperty("station_code")
  @Schema(example = "1234567890100", description = "Unique code to identify the station")
  @NotBlank
  @Size(max = 35)
  private String idStazione;

  @JsonProperty("enabled")
  @Schema(
      description = "Parameter to find out whether or not the station has been enabled",
      defaultValue = "true")
  @NotNull
  private Boolean enabled;

  @JsonProperty("broker_description")
  @Schema(description = "A description of the intermediate EC", example = "Regione Lazio")
  private String brokerDescription;

  @Min(1)
  @Max(2)
  @JsonProperty("version")
  @Schema(description = "The version of the station")
  @NotNull
  private Long versione;

  @Schema(description = "Describe the station connection's type, true synchronous, false asynchronous")
  @JsonProperty("is_connection_sync")
  private Boolean isConnectionSync;

  @JsonProperty("create_date")
  @Schema(description = "Station creation date")
  @JsonFormat(pattern = Constants.DateTimeFormat.DATE_TIME_FORMAT)
  @JsonSerialize(using = OffsetDateTimeSerializer.class)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createDate;

}
