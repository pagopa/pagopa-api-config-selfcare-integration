Feature: All about API Config Selfcare integration workflow - Retrieve stations by broker

  Background:
    Given APIConfig service running

  Scenario: Existing station related to broker
    Given an existing broker
    And a station related to broker
    When the client requests the list of stations related to the broker
    Then the client receives status code 200
    And the client receives a non-empty list of stations
    And the station is included in the result list

  Scenario: No existing station related to broker
    Given an existing broker with no stations related
    When the client requests the list of stations related to the broker
    Then the client receives status code 200
    And the client receives an empty list of stations

  Scenario: Non-existing broker
    Given a non-existing broker
    When the client requests the list of stations related to the broker
    Then the client receives status code 404
    And the client receives an error message
