package it.gov.pagopa.apiconfig.selfcareintegration.model;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfo {

  @JsonProperty("page")
  @Schema(description = "Page number")
  @PositiveOrZero
  Integer page;

  @JsonProperty("limit")
  @Schema(description = "Required number of items per page")
  @Positive
  Integer limit;

  @JsonProperty("items_found")
  @Schema(
      description = "Number of items found. (The last page may have fewer elements than required)")
  @PositiveOrZero
  Integer itemsFound;

  @JsonProperty("total_pages")
  @Schema(description = "Total number of pages")
  @PositiveOrZero
  Integer totalPages;
}
