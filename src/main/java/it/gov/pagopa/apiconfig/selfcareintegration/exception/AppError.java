package it.gov.pagopa.apiconfig.selfcareintegration.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum AppError {
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
      "Something was wrong"),
  CREDITOR_INSTITUTION_NOT_FOUND(
      HttpStatus.NOT_FOUND,
      "Creditor Institution not found",
      "No Creditor Institution found with code: %s");

  public final HttpStatus httpStatus;
  public final String title;
  public final String details;


  AppError(HttpStatus httpStatus, String title, String details) {
    this.httpStatus = httpStatus;
    this.title = title;
    this.details = details;
  }
}


