package it.gov.pagopa.apiconfig.selfcareintegration.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enum defining the application errors
 */
@Getter
public enum AppError {
    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Something was wrong: %s"),
    CREDITOR_INSTITUTION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "Creditor Institution not found",
            "No Creditor Institution found with code: %s"),
    MULTIPLE_CREDITOR_INSTITUTIONS_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "Creditor Institutions not found",
            "No Creditor Institutions found with codes: %s"),
    STATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Station not found", "No Station found with code: %s"),
    PA_NOT_FOUND(HttpStatus.NOT_FOUND, "PA not found", "No PA found with code: %s"),


    BROKER_NOT_FOUND(HttpStatus.NOT_FOUND, "Broker not found", "No Broker found with code: %s"),
    UNKNOWN(null, null, null);

    public final HttpStatus httpStatus;
    public final String title;
    public final String details;

    AppError(HttpStatus httpStatus, String title, String details) {
        this.httpStatus = httpStatus;
        this.title = title;
        this.details = details;
    }
}
