package it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Model that represent the name and tax code of a creditor institution
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditorInstitutionInfo {

    @JsonProperty("business_name")
    @Schema(example = "Comune di Roma", description = "The business name of the creditor institution", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private String businessName;

    @JsonProperty("creditor_institution_code")
    @Schema(example = "02438750586", description = "The tax code of the creditor institution", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String creditorInstitutionCode;
}
