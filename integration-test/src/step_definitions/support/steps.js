const { Before, Given, Then, When } = require('@cucumber/cucumber');
const {
    assertEmptyList,
    assertErrorMessage,
    assertNonEmptyList,
    assertStatusCode,
    assertUndefined
} = require('./logic/common_logic');
const { executeHealthCheckForAPIConfig } = require('./logic/health_checks_logic');
const {
  assertChannelIncludedInResponse,
  assertStationIncludedInResponse,
  retrieveBroker,
  retrieveBrokerWithNoStation,
  retrieveNonExistingBroker,
  retrieveCreditorInstitution,
  retrieveCreditorInstitutionWithNoStation,
  retrieveNonExistingCreditorInstitution,
  retrievePSPBroker,
  retrievePSPBrokerWithNoChannel,
  retrieveNonExistingPSPBroker,
  retrieveStationsByBroker,
  retrieveStationsByCreditorInstitution,
  retrieveChannelsByPSPBroker,
  retrieveStationRelatedToBroker,
  retrieveStationRelatedToCI,
  retrieveApplicationCodesByCreditorInstitution,
  retrieveSegregationCodesByCreditorInstitution,
  retrieveChannelRelatedToPSPBroker
} = require('./logic/selfcare_integration_logic');

let bundle = {
  brokerId: undefined,
  creditorInstitutionId: undefined,
  stationId: undefined,
  pspBrokerId: undefined,
  channelId: undefined,
  broker: undefined,
  creditorInstitution: undefined,
  station: undefined,
  pspBroker: undefined,
  channel: undefined,
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
Given('an existing PSP broker', () => retrievePSPBroker(bundle));
Given('an existing PSP broker with no channels related', () => retrievePSPBrokerWithNoChannel(bundle));
Given('a non-existing PSP broker', () => retrieveNonExistingPSPBroker(bundle));
Given('a station related to broker', () => retrieveStationRelatedToBroker(bundle));
Given('a station related to creditor institution', () => retrieveStationRelatedToCI(bundle));
Given('a channel related to PSP broker', () => retrieveChannelRelatedToPSPBroker(bundle));

/*
 *  'When' clauses for executing actions.
 */
When('the client requests the list of stations related to the broker', () => retrieveStationsByBroker(bundle));
When('the client requests the list of stations related to the creditor institution', () => retrieveStationsByCreditorInstitution(bundle));
When('the client requests the list of channels related to the PSP broker', () => retrieveChannelsByPSPBroker(bundle));
When('the client requests the list of application codes related to the creditor institution', () => retrieveApplicationCodesByCreditorInstitution(bundle, true));
When('the client requests the list of application codes related to the creditor institution without used ones', () => retrieveApplicationCodesByCreditorInstitution(bundle, false));
When('the client requests the list of segregation codes related to the creditor institution', () => retrieveSegregationCodesByCreditorInstitution(bundle, true, false));
When('the client requests the list of segregation codes related to the creditor institution without used ones', () => retrieveSegregationCodesByCreditorInstitution(bundle, false, false));
When('the client requests the list of segregation codes related to the creditor institution filtering by service', () => retrieveSegregationCodesByCreditorInstitution(bundle, true, true));

/*
 *  'Then' clauses for assering retrieved data
 */
Then('the client receives status code {int}', (statusCode) => assertStatusCode(bundle.response, statusCode));
Then('the client receives a non-empty list of stations', () => assertNonEmptyList(bundle.response?.data.stations));
Then('the client receives a non-empty list of channels', () => assertNonEmptyList(bundle.response?.data.channels));
Then('the client receives a non-empty list of unused codes', () => assertNonEmptyList(bundle.response?.data.unused));
Then('the client receives a non-empty list of used codes', () => assertNonEmptyList(bundle.response?.data.used));
Then('the client receives an empty list of stations', () => assertEmptyList(bundle.response?.data?.stations));
Then('the client receives an empty list of channels', () => assertEmptyList(bundle.response?.data?.channels));
Then('the client receives an empty list of used codes', () => assertEmptyList(bundle.response?.data.used));
Then('the client does not receives the field {string}', (fieldName) => assertUndefined(bundle.response?.data, fieldName));
Then('the client receives an error message', () => assertErrorMessage(bundle.response?.data));
Then('the station is included in the result list', () => assertStationIncludedInResponse(bundle.stationId, bundle.response));
Then('the channel is included in the result list', () => assertChannelIncludedInResponse(bundle.channelId, bundle.response));


Before(function(scenario) {
    const header = `| Starting scenario "${scenario.pickle.name}" |`;
    let h = "-".repeat(header.length);
    console.log(`\n${h}`);
    console.log(`${header}`);
    console.log(`${h}`);
});
