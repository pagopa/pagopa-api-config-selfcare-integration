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

    String pspCode;

    @JsonProperty("ABI")
    List<String> abi = new ArrayList<>();

    @JsonProperty("BIC")
    List<String> bic = new ArrayList<>();


    public PspDuplicated(String taxCode, String pspCode){
        this.taxCode = taxCode;
        this.pspCode = pspCode;
        abi = new ArrayList<>();
        bic = new ArrayList<>();
    }
}
