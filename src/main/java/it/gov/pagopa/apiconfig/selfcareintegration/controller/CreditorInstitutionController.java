package it.gov.pagopa.apiconfig.selfcareintegration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.apiconfig.selfcareintegration.model.ProblemJson;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.AvailableCodes;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCodeList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionInfo;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.service.CreditorInstitutionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@RestController()
@RequestMapping(path = "/creditorinstitutions")
@Tag(name = "Creditor Institutions", description = "Everything about Creditor Institution")
@Validated
public class CreditorInstitutionController {

    private final CreditorInstitutionsService creditorInstitutionsService;

    @Autowired
    public CreditorInstitutionController(CreditorInstitutionsService creditorInstitutionsService) {
        this.creditorInstitutionsService = creditorInstitutionsService;
    }

    /**
     * GET /{creditorInstitutionCode}/stationsdetails : Get creditor institution station
     *
     * @param creditorInstitutionCode station code. (required)
     * @return OK. (status code 200) or Not Found (status code 404) or Service unavailable (status
     * code 500)
     */
    @Operation(
            summary = "Get creditor institution station list",
            security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StationDetailsList.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))
    })
    @GetMapping(value = "/{creditorInstitutionCode}/stations", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StationDetailsList> getStationsDetailsFromCreditorInstitution(
            @Parameter(description = "Organization fiscal code, the fiscal code of the Organization.") @PathVariable("creditorInstitutionCode") String creditorInstitutionCode,
            @Parameter(description = "The number of elements to be included in the page.", required = true) @RequestParam(required = false, defaultValue = "10") @Positive @Max(999) Integer limit,
            @Parameter(description = "The index of the page, starting from 0.", required = true) @Min(0) @RequestParam(required = false, defaultValue = "0") Integer page
    ) {
        return ResponseEntity.ok(
                this.creditorInstitutionsService
                        .getStationsDetailsFromCreditorInstitution(creditorInstitutionCode, PageRequest.of(page, limit))
        );
    }

    /**
     * GET /stations/{station-code} : Get the list of creditor institutions associated to a station
     *
     * @param stationCode station code. (required)
     * @return OK. (status code 200) or Not Found (status code 404) or Service unavailable (status
     * code 500)
     */
    @Operation(
            summary = "Get the list of creditor institutions that can be associated to a station",
            security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CreditorInstitutionInfo.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))
    })
    @GetMapping(value = "/stations/{station-code}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<CreditorInstitutionInfo>> getStationCreditorInstitutions(
            @Parameter(description = "Station's code") @PathVariable("station-code") String stationCode,
            @Parameter(description = "List of Creditor Institution's tax code, restrict the research to this tax code list only") @RequestParam @Size(max = 10) List<String> ciTaxCodeList
    ) {
        return ResponseEntity.ok(this.creditorInstitutionsService.getStationCreditorInstitutions(stationCode, ciTaxCodeList));
    }

    /**
     * GET /{creditorInstitutionCode}/applicationcodes : Get creditor institution station
     *
     * @param creditorInstitutionCode station code. (required)
     * @param showUsedCodes           The flag that permits to show the codes already used
     * @return OK. (status code 200) or Not Found (status code 404) or Service unavailable (status
     * code 500)
     */
    @Operation(
            summary = "Get application code associations with creditor institution",
            security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CIAssociatedCodeList.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))
    })
    @GetMapping(value = "/{creditorInstitutionCode}/applicationcodes", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CIAssociatedCodeList> getApplicationCodesFromCreditorInstitution(
            @Parameter(description = "Organization fiscal code, the fiscal code of the Organization.") @PathVariable("creditorInstitutionCode") String creditorInstitutionCode,
            @Parameter(description = "The flag that permits to show the codes already used. Default: true") @RequestParam(required = false, defaultValue = "true") boolean showUsedCodes
    ) {
        return ResponseEntity.ok(
                creditorInstitutionsService
                        .getApplicationCodesFromCreditorInstitution(creditorInstitutionCode, showUsedCodes)
        );
    }

    /**
     * GET /{creditorInstitutionCode}/segregationcodes : Get creditor institution segregation codes
     *
     * @param ciTaxCode       Creditor institution's tax code that own the station
     * @param targetCITaxCode Tax code of the creditor institution that will be associated to the station
     * @return OK. (status code 200) or Not Found (status code 404) or Service unavailable (status
     * code 500)
     */
    @Operation(
            summary = "Get segregation code associations with creditor institution",
            security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AvailableCodes.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))
    })
    @GetMapping(value = "/{ci-tax-code}/segregationcodes", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AvailableCodes> getSegregationCodesFromCreditorInstitution(
            @Parameter(description = "Creditor institution's tax code that own the station") @PathVariable("ci-tax-code") String ciTaxCode,
            @Parameter(description = "Tax code of the creditor institution that will be associated to the station") @RequestParam @NotBlank String targetCITaxCode
    ) {
        return ResponseEntity.ok(this.creditorInstitutionsService.getAvailableCISegregationCodes(ciTaxCode, targetCITaxCode));
    }

    /**
     * Retrieve a list of creditor institution business names, given the provided list of creditor institution tax codes
     *
     * @param taxCodeList the list of creditor institution tax codes
     * @return a list of {@link CreditorInstitutionInfo}, containing the business name and tax code of creditor institution
     */
    @Operation(summary = "Get the list of creditor institution business names",
            description = "Return a list of business name and tax code of creditor institutions, given the provided list of creditor institution tax codes",
            security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CreditorInstitutionInfo.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Cacheable(value = "getCreditorInstitutionNamesFromTaxCodes")
    public ResponseEntity<List<CreditorInstitutionInfo>> getCreditorInstitutionNamesFromTaxCodes(
            @Parameter(description = "List of Creditor Institution's tax code") @RequestParam @Size(max = 10) List<String> taxCodeList
    ) {
        return ResponseEntity.ok(this.creditorInstitutionsService.getCreditorInstitutionInfoList(taxCodeList));
    }
}
