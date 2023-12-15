package it.gov.pagopa.apiconfig.selfcareintegration.model.iban;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Constants;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Iban detail
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IbanDetails {

    @JsonProperty("ci_fiscal_code")
    private String creditorInstitutionFiscalCode;

    @JsonProperty("ci_name")
    private String creditorInstitutionName;

    @JsonProperty("iban")
    private String iban;

    @JsonProperty("inserted_date")
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private OffsetDateTime insertedDate;

    @JsonProperty("validity_date")
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private OffsetDateTime validityDate;

    @JsonProperty("due_date")
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private OffsetDateTime dueDate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("owner_fiscal_code")
    private String ownerFiscalCode;

    @JsonProperty("labels")
    private List<IbanLabel> labels;
}
