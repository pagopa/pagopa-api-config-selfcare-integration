package it.gov.pagopa.apiconfig.selfcareintegration.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.gov.pagopa.apiconfig.selfcareintegration.model.AppInfo;
import it.gov.pagopa.apiconfig.selfcareintegration.model.ProblemJson;
import it.gov.pagopa.apiconfig.selfcareintegration.model.PspDuplicated;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedPspRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.service.HealthCheckService;
import it.gov.pagopa.apiconfig.starter.repository.PspRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Validated
public class HomeController {

    @Value("${server.servlet.context-path}")
    String basePath;

    @Value("${info.application.name}")
    private String name;

    @Value("${info.application.version}")
    private String version;

    @Value("${info.properties.environment}")
    private String environment;

    @Autowired
    HealthCheckService healthCheckService;

    @Autowired
    ExtendedPspRepository pspRepository;

    /**
     * @return redirect to Swagger page documentation
     */
    @Hidden
    @GetMapping("")
    public RedirectView home() {
        if (!basePath.endsWith("/")) {
            basePath += "/";
        }
        return new RedirectView(basePath + "swagger-ui.html");
    }

    /**
     * Health Check
     *
     * @return ok
     */
    @Operation(
            summary = "Return OK if application is started",
            security = {
                    @SecurityRequirement(name = "ApiKey"),
                    @SecurityRequirement(name = "Authorization")
            },
            tags = {"Home"})
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AppInfo.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemJson.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema())),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(schema = @Schema())),
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
    @GetMapping("/info")
    public ResponseEntity<AppInfo> healthCheck() {

        AppInfo info =
                AppInfo.builder()
                        .name(name)
                        .version(version)
                        .environment(environment)
                        .dbConnection(healthCheckService.checkDatabaseConnection() ? "up" : "down")
                        .build();
        return ResponseEntity.status(HttpStatus.OK).body(info);
    }


    @Operation(summary = "Export PSP duplicated",
            security = {@SecurityRequirement(name = "ApiKey"), @SecurityRequirement(name = "Authorization")},
            tags = {"Home"})
    @GetMapping("/export_psp_duplicated")
    public Collection<PspDuplicated> getPspDuplicated() {
        return pspRepository.findAllByEnabled(true)
                .parallelStream()
                .map(elem -> {
                    var builder = new PspDuplicated(elem.getCodiceFiscale());
                    if (elem.getIdPsp().startsWith("ABI")) {
                        builder.getAbi().add(elem.getIdPsp());
                    } else {
                        builder.getBic().add(elem.getIdPsp());
                    }
                    return builder;
                })
                .collect(Collectors.toMap(
                        PspDuplicated::getTaxCode,
                        Function.identity(),
                        (sum1, sum2) -> {
                            var builder = new PspDuplicated(sum1.getTaxCode());
                            builder.getAbi().addAll(sum1.getAbi());
                            builder.getAbi().addAll(sum2.getAbi());

                            builder.getBic().addAll(sum1.getBic());
                            builder.getBic().addAll(sum2.getBic());
                            return builder;
                        }
                )).values();
    }
}
