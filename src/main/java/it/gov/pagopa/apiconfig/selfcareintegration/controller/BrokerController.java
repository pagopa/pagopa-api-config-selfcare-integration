package it.gov.pagopa.apiconfig.selfcareintegration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.apiconfig.selfcareintegration.model.ProblemJson;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.service.BrokersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@RestController()
@RequestMapping(path = "/brokers")
@Tag(name = "Brokers", description = "Everything about brokers")
@Validated
public class BrokerController {

  @Autowired private BrokersService brokersService;

  /**
   * GET /{brokerId}/stations : Get broker stations
   *
   * @param brokerId broker identifier. (required)
   * @return OK. (status code 200) or Not Found (status code 404) or Service unavailable (status
   *     code 500)
   */
  @Operation(
      summary = "Get broker's station list",
      security = {
          @SecurityRequirement(name = "ApiKey"),
          @SecurityRequirement(name = "Authorization")
      },
      tags = {
          "Brokers",
      })
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "OK",
              content =
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = StationDetailsList.class))),
          @ApiResponse(
              responseCode = "401",
              description = "Unauthorized",
              content = @Content(schema = @Schema())),
          @ApiResponse(
              responseCode = "403",
              description = "Forbidden",
              content = @Content(schema = @Schema())),
          @ApiResponse(
              responseCode = "404",
              description = "Not Found",
              content = @Content(schema = @Schema(implementation = ProblemJson.class))),
          @ApiResponse(
              responseCode = "429",
              description = "Too many requests",
              content = @Content(schema = @Schema())),
          @ApiResponse(
              responseCode = "500",
              description = "Service unavailable",
              content =
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ProblemJson.class)))
      })
  @GetMapping(
      value = "/{brokerId}/stations",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<StationDetailsList> getStationsDetailsFromBroker(
      @Parameter(description = "The identifier of the broker.", required = true)
      @PathVariable("brokerId")
      String brokerId,
      @Parameter(description = "The identifier of the station.")
      @RequestParam(required = false) String stationId,
      @Valid
      @Parameter(description = "The number of elements to be included in the page.", required = true)
      @RequestParam(required = false, defaultValue = "10")
      @Positive
      @Max(999) Integer limit,
      @Valid
      @Parameter(description = "The index of the page, starting from 0.", required = true)
      @Min(0)
      @RequestParam(required = false, defaultValue = "0") Integer page) {
    return ResponseEntity.ok(brokersService.getStationsDetailsFromBroker(brokerId, stationId, PageRequest.of(page, limit)));
  }

}
