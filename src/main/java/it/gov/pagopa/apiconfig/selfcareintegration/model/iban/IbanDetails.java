package it.gov.pagopa.apiconfig.selfcareintegration.model.iban;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("creditor_institution")
    private String creditorInstitution;

    @JsonProperty("iban")
    private String iban;

    @JsonProperty("inserted_date")
    private OffsetDateTime insertedDate;

    @JsonProperty("validity_date")
    private OffsetDateTime validityDate;

    @JsonProperty("due_date")
    private OffsetDateTime dueDate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("owner_fiscal_code")
    private String ownerFiscalCode;

    @JsonProperty("labels")
    private List<IbanLabel> labels;
}
