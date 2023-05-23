Feature: All about API Config Selfcare integration workflow - Retrieve channels by PSP broker

  Background:
    Given APIConfig service running

  Scenario: Existing channel related to PSP broker
    Given an existing PSP broker
    And a channel related to PSP broker
    When the client requests the list of channels related to the PSP broker
    Then the client receives status code 200
    And the client receives a non-empty list of channels
    And the channel is included in the result list

  Scenario: No existing channel related to PSP broker
    Given an existing PSP broker with no channels related
    When the client requests the list of channels related to the PSP broker
    Then the client receives status code 200
    And the client receives an empty list of channels

  Scenario: Non-existing PSP broker
    Given a non-existing PSP broker
    When the client requests the list of channels related to the PSP broker
    Then the client receives status code 404
    And the client receives an error message
