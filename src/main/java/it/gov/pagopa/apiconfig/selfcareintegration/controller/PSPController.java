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
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.service.PSPsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@RestController()
@RequestMapping(path = "/payment-service-providers")
@Tag(name = "PSP", description = "Everything about PSP")
@Validated
public class PSPController {

    @Autowired
    private PSPsService pspService;


    @Operation(summary = "Get PSP broker's channel list",
            security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")},
            tags = {"PSP",})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChannelDetailsList.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ProblemJson.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Service unavailable", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemJson.class)))})
    @GetMapping(value = "/{pspFiscalCode}/channels",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ChannelDetailsList> getChannelByFiscalCode(
            @Parameter(description = "The fiscal code of the PSP.", required = true)
            @PathVariable("pspFiscalCode")
            String pspFiscalCode,
            @Valid
            @Parameter(description = "The number of elements to be included in the page.", required = true)
            @RequestParam(required = false, defaultValue = "10")
            @Positive @Max(999)
            Integer limit,
            @Valid
            @Parameter(description = "The index of the page, starting from 0.", required = true)
            @Min(0)
            @RequestParam(required = false, defaultValue = "0")
            Integer page) {
        return ResponseEntity.ok(pspService.getChannelByFiscalCode(pspFiscalCode, PageRequest.of(page, limit)));
    }
}
