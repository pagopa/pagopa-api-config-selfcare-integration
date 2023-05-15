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
import it.gov.pagopa.apiconfig.selfcareintegration.service.CreditorInstitutionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/creditorinstitutions")
@Tag(name = "Creditor Institutions", description = "Everything about Creditor Institution")
@Validated
public class CreditorInstitutionController {

  @Autowired private CreditorInstitutionsService creditorInstitutionsService;

  /**
   * GET /{creditorInstitutionCode}/stationsdetails : Get creditor institution station
   *
   * @param creditorInstitutionCode station code. (required)
   * @return OK. (status code 200) or Not Found (status code 404) or Service unavailable (status
   *     code 500)
   */
  @Operation(
      summary = "Get creditor institution station list",
      security = {
          @SecurityRequirement(name = "ApiKey"),
          @SecurityRequirement(name = "Authorization")
      },
      tags = {
          "Creditor Institutions",
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
      value = "/{creditorInstitutionCode}/stationsdetails",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<StationDetailsList> getStationsDetailsFromCreditorInstitution(
      @Parameter(description = "Organization fiscal code, the fiscal code of the Organization.", required = true)
      @PathVariable("creditorInstitutionCode")
      String creditorInstitutionCode) {
    return ResponseEntity.ok(
        creditorInstitutionsService.getStationsDetailsFromCreditorInstitution(creditorInstitutionCode));
  }

}
