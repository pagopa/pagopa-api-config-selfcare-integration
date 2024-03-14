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
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbansList;
import it.gov.pagopa.apiconfig.selfcareintegration.service.IbansService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(path = "/ibans")
@Tag(name = "Ibans", description = "Everything about IBANs")
@Validated
public class IbanController {

  private final IbansService ibansService;

    public IbanController(IbansService ibansService) {
        this.ibansService = ibansService;
    }

    /**
   * GET / : Get all ibans
   *
   * @return OK. (status code 200) or Not Found (status code 404) or Service unavailable (status
   *     code 500)
   */
  @Operation(
    summary = "Get the paginated list of all IBANs, filtering by creditor institution",
    security = {
      @SecurityRequirement(name = "ApiKey"),
      @SecurityRequirement(name = "Authorization")
    },
    tags = {
      "Ibans",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "OK",
        content =
            @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = IbansList.class)
            )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(schema = @Schema())
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = @Content(schema = @Schema())
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Not Found",
        content = @Content(schema = @Schema(implementation = ProblemJson.class))
      ),
      @ApiResponse(
        responseCode = "429",
        description = "Too many requests",
        content = @Content(schema = @Schema())
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Service unavailable",
        content =
            @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ProblemJson.class)
            )
      )
    }
  )
  @PostMapping(
    value = "",
    produces = {MediaType.APPLICATION_JSON_VALUE}
  )
  @Cacheable(value = "getIbans")
  public ResponseEntity<IbansList> getIbans(
      @RequestBody @NotEmpty List<String> creditorInstitutions,
      @Valid
          @Parameter(
            description = "The number of elements to be included in the page.",
            required = true
          )
          @RequestParam(required = false, defaultValue = "10")
          @Positive
          @Max(1000)
          Integer limit,
      @Parameter(description = "The index of the page, starting from 0.", required = true)
          @Valid
          @Min(0)
          @RequestParam(required = false, defaultValue = "0")
          Integer page) {
    return ResponseEntity.ok(
        ibansService.getIbans(
            creditorInstitutions,
            PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "fkPa", "objId"))));
  }
}
