Feature: All about API Config Selfcare integration workflow - Retrieve stations by creditor institution

  Background:
    Given APIConfig service running

  Scenario: Existing station related to creditor institution
    Given an existing creditor institution
    And a station related to creditor institution
    When the client requests the list of stations related to the creditor institution
    Then the client receives status code 200
    And the client receives a non-empty list
    And the station is included in the result list

  Scenario: No existing station related to creditor institution
    Given an existing creditor institution with no stations related
    When the client requests the list of stations related to the creditor institution
    Then the client receives status code 200
    And the client receives an empty list

  Scenario: Non-existing creditor institution
    Given a non-existing creditor institution
    When the client requests the list of stations related to the creditor institution
    Then the client receives status code 404
    And the client receives an error message
