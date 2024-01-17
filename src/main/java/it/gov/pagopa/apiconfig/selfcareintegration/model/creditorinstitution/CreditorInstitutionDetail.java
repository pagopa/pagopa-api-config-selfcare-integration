package it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Constants;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * Creditor Institutions
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditorInstitutionDetail {

    @JsonProperty("business_name")
    @Schema(example = "Comune di Roma", description = "The business name of the creditor institution", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private String businessName;

    @JsonProperty("creditor_institution_code")
    @Schema(example = "02438750586", description = "The fiscal code of the creditor institution", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String creditorInstitutionCode;

    @JsonProperty("cbill_code")
    @Schema(example = "APNEY", description = "The CBill code of the creditor institution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String cbillCode;

    @JsonProperty("broker_business_name")
    @Schema(example = "Regione Lazio", description = "The business name of the broker associated to creditor institution by defined station", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotNull
    private String brokerBusinessName;

    @JsonProperty("broker_code")
    @Schema(example = "80143490581", description = "The fiscal code of the broker associated to creditor institution by defined station", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String brokerCode;

    @JsonProperty("station_code")
    @Schema(example = "80143490581_01", description = "The code of the station that permits to associate a creditor institution to a broker", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String stationCode;

    @JsonProperty("station_state")
    @Schema(description = "The flag that define if the station is active or inactive", defaultValue = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Boolean stationState;

    @JsonProperty("station_version")
    @Schema(example = "2", description = "The version of the station. It can be either 1 or 2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long stationVersion;

    @JsonProperty("aux_digit")
    @Schema(example = "3", description = "The value of the AUX digit field that can be set in payments' IUV", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long auxDigit;

    @JsonProperty("segregation_code")
    @Schema(example = "05", description = "The value of the segregation code that can be set in payments' IUV in order to use this station", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String segregationCode;

    @JsonProperty("application_code")
    @Schema(example = "02", description = "The value of the application code that can be set in payments' IUV in order to use this station", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String applicationCode;

    @JsonProperty("broadcast")
    @Schema(description = "The flag that define if the station is made for broadcast operations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean broadcast;

    @JsonProperty("activation_date")
    @JsonFormat(pattern = Constants.DATE_TIME_WITHOUT_SECONDS_FORMAT)
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(example = "2021-01-01T12:00Z", description = "The activation date of the station", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private OffsetDateTime activationDate;
}
