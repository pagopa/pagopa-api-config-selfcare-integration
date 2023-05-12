const { Before, Given, Then, When } = require('@cucumber/cucumber');
const {
    assertEmptyList,
    assertErrorMessage,
    assertNonEmptyList,
    assertStatusCode
} = require('./logic/common_logic');
const { executeHealthCheckForAPIConfig } = require('./logic/health_checks_logic');
const {
  assertStationIncludedInResponse,

  retrieveBroker,
  retrieveBrokerWithNoStation,
  retrieveNonExistingBroker,
  retrieveCreditorInstitution,
  retrieveCreditorInstitutionWithNoStation,
  retrieveNonExistingCreditorInstitution,
  retrieveStationsByBroker,
  retrieveStationsByCreditorInstitution,
  retrieveStationRelatedToBroker,
  retrieveStationRelatedToCI
} = require('./logic/selfcare_integration_logic');

let bundle = {
  brokerId: undefined,
  creditorInstitutionId: undefined,
  stationId: undefined,
  broker: undefined,
  creditorInstitution: undefined,
  station: undefined,
  response: undefined,
  limit: 100,
  pageNumber: 0
}

/*
 *  'Given' precondition for health checks on various services.
 */
Given('APIConfig service running', () => executeHealthCheckForAPIConfig());

/*
 *  'Given' precondition for validating the entities to be used.
 */
Given('an existing broker', () => retrieveBroker(bundle));
Given('an existing broker with no stations related', () => retrieveBrokerWithNoStation(bundle));
Given('a non-existing broker', () => retrieveNonExistingBroker(bundle));
Given('an existing creditor institution', () => retrieveCreditorInstitution(bundle));
Given('an existing creditor institution with no stations related', () => retrieveCreditorInstitutionWithNoStation(bundle));
Given('a non-existing creditor institution', () => retrieveNonExistingCreditorInstitution(bundle));
Given('a station related to broker', () => retrieveStationRelatedToBroker(bundle));
Given('a station related to creditor institution', () => retrieveStationRelatedToCI(bundle));

/*
 *  'When' clauses for executing actions.
 */
When('the client requests the list of stations related to the broker', () => retrieveStationsByBroker(bundle));
When('the client requests the list of stations related to the creditor institution', () => retrieveStationsByCreditorInstitution(bundle));

/*
 *  'Then' clauses for assering retrieved data
 */
Then('the client receives status code {int}', (statusCode) => assertStatusCode(bundle.response, statusCode));
Then('the client receives a non-empty list', () => assertNonEmptyList(bundle.response));
Then('the client receives an empty list', () => assertEmptyList(bundle.response));
Then('the client receives an error message', () => assertErrorMessage(bundle.response));
Then('the station is included in the result list', () => assertStationIncludedInResponse(bundle.stationId, bundle.response));


Before(function(scenario) {
    const header = `| Starting scenario "${scenario.pickle.name}" |`;
    let h = "-".repeat(header.length);
    console.log(`\n${h}`);
    console.log(`${header}`);
    console.log(`${h}`);
});
