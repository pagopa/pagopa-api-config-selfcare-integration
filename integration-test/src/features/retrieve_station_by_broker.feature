Feature: All about API Config Selfcare integration workflow - Retrieve stations by broker

  Background:
    Given APIConfig service running

  Scenario: Existent station related to broker
    Given a broker with id "XX"
    And a station with id "YY" related to broker "XX"
    When the client requests the list of stations related to the broker
    Then the client receives status code 200
    And the client receives a non-empty list
    And the station is included in the result list

  Scenario: No existent station related to broker
    Given a broker with id "XY" with no stations related
    When the client requests the list of stations related to the broker
    Then the client receives status code 200
    And the client receives an empty list

  Scenario: Non-existent broker
    Given no broker with id "XZ"
    When the client requests the list of stations related to the broker
    Then the client receives status code 404
    And the client receives an error message
