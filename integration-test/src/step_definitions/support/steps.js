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
  checkNonExistingBroker,
  checkNonExistingCreditorInstitution,
  retrieveBroker,
  retrieveCreditorInstitution,
  retrieveStationsByBroker,
  retrieveStationsByCreditorInstitution
} = require('./logic/selfcare_integration_test');

/*
 *  'Given' precondition for health checks on various services.
 */
Given('APIConfig service running', () => executeHealthCheckForAPIConfig());

/*
 *  'Given' precondition for validating the entities to be used.
 */
Given('a broker with id {string}', (brokerId) => retrieveBroker(bundle, brokerID));
Given('a broker with id {string} with no stations related', (brokerId) => retrieveBroker(bundle, brokerID));
Given('no broker with id {string}', (brokerId) => checkNonExistingBroker(brokerID));
Given('a creditor institution with id {string}', (creditorInstitutionId) => retrieveCreditorInstitution(bundle, creditorInstitutionId));
Given('a creditor institution with id {string} with no stations related', (creditorInstitutionId) => retrieveCreditorInstitution(bundle, creditorInstitutionId));
Given('no creditor institution with id {string}', (creditorInstitutionId) => checkNonExistingCreditorInstitution(creditorInstitutionId));

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
Then('the station is included in the result list', () => assertStationIncludedInResponse(bundle.response));


Before(function(scenario) {
    const header = `| Starting scenario "${scenario.pickle.name}" |`;
    let h = "-".repeat(header.length);
    console.log(`\n${h}`);
    console.log(`${header}`);
    console.log(`${h}`);
});
