package it.gov.pagopa.apiconfig.selfcareintegration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class PspDuplicated {

    @JsonProperty("CF")
    String taxCode;

    @JsonProperty("ABI")
    List<String> abi = new ArrayList<>();

    @JsonProperty("BIC")
    List<String> bic = new ArrayList<>();


    public PspDuplicated(String taxCode){
        this.taxCode = taxCode;
        abi = new ArrayList<>();
        bic = new ArrayList<>();
    }
}
