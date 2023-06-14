Feature: All about API Config Selfcare integration workflow - Retrieve segregation codes by creditor institution

  Background:
    Given APIConfig service running

  Scenario: Show used and unused segregation codes for creditor institution
    Given an existing creditor institution
    And a station related to creditor institution
    When the client requests the list of segregation codes related to the creditor institution
    Then the client receives status code 200
    And the client receives a non-empty list of unused codes
    And the client receives a non-empty list of used codes

  Scenario: Show only unused segregation codes for creditor institution
    Given an existing creditor institution
    And a station related to creditor institution
    When the client requests the list of segregation codes related to the creditor institution without used ones
    Then the client receives status code 200
    And the client receives a non-empty list of unused codes
    And the client does not receives the field "used"

  Scenario: Show used and unused segregation codes for creditor institution with no station
    Given an existing creditor institution with no stations related
    When the client requests the list of segregation codes related to the creditor institution
    Then the client receives status code 200
    And the client receives a non-empty list of unused codes
    And the client receives an empty list of used codes

  Scenario: Show service-filtered used and unused segregation codes for creditor institution
    Given an existing creditor institution
    And a station related to creditor institution
    When the client requests the list of segregation codes related to the creditor institution filtering by service
    Then the client receives status code 200
    And the client receives a non-empty list of unused codes
    And the client receives a non-empty list of used codes

  Scenario: Non-existing creditor institution
    Given a non-existing creditor institution
    When the client requests the list of segregation codes related to the creditor institution
    Then the client receives status code 404
    And the client receives an error message
