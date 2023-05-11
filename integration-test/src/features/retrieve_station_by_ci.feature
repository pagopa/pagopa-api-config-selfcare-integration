Feature: All about API Config Selfcare integration workflow - Retrieve stations by creditor institution

  Background:
    Given APIConfig service running

  Scenario: Existent station related to creditor institution
    Given a creditor institution with id "XX"
    And a station with id "YY" related to creditor institution "XX"
    When the client requests the list of stations related to the creditor institution
    Then the client receives status code 200
    And the client receives a non-empty list
    And the station is included in the result list

  Scenario: No existent station related to creditor institution
    Given a creditor institution with id "XY" with no stations related
    When the client requests the list of stations related to the creditor institution
    Then the client receives status code 200
    And the client receives an empty list

  Scenario: Nom-existent creditor institution
    Given no creditor institution with id "XZ"
    When the client requests the list of stations related to the creditor institution
    Then the client receives status code 404
    And the client receives an error message
