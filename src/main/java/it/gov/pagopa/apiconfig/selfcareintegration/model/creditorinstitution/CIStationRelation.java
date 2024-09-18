package it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CIStationRelation {

    private String ciTaxCode;
    private String segregationCode;

}