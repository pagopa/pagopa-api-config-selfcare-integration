package it.gov.pagopa.apiconfig.selfcareintegration.model.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.apiconfig.selfcareintegration.model.PageInfo;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Channels
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelDetailsList {

    @JsonProperty("channels")
    @NotNull
    @Schema(description = "List of stations associated to the same entity")
    private List<ChannelDetails> channelsDetailsList;

    @JsonProperty("page_info")
    @Schema()
    @NotNull
    @Valid
    private PageInfo pageInfo;
}
