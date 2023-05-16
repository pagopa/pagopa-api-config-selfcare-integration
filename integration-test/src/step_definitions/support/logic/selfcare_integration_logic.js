const assert = require("assert");
const {
    readBroker,
    readCreditorInstitution,
    readStation
} = require("../clients/api_config_client");
const {
    getStationsByBroker,
    getStationsByCreditorInstitution
} = require("../clients/selfcare_integration_client.js");
const { debugLog } = require("../utility/helpers");


async function assertStationIncludedInResponse(stationId, response) {
    console.log(` - ..`);
    // TODO
    debugLog(``);
}

async function retrieveBroker(bundle) {
    console.log(` - Given an existing broker`);
    bundle.brokerId = process.env.valid_broker;
    retrieveBrokerExecuteAPICall(bundle);    
}

async function retrieveBrokerWithNoStation(bundle) {
    bundle.brokerId = process.env.valid_broker_with_no_station;
    console.log(` - Given an existing broker with no stations related`);
    retrieveBrokerExecuteAPICall(bundle);    
}

async function retrieveNonExistingBroker(bundle) {
    console.log(` - Given a non-existing broker`);
    bundle.brokerId = process.env.invalid_broker;
    let response = await readBroker(bundle.brokerId);
    debugLog(`Broker retrieving API invocation returned HTTP status code: ${response.status} with body: ${JSON.stringify(response.data)}`);
    assert.strictEqual(response.status, 404);    
}

async function retrieveCreditorInstitution(bundle) {
    console.log(` - a creditor institution`);
    bundle.creditorInstitutionId = process.env.valid_ci;    
    retrieveCIExecuteAPICall(bundle);
}

async function retrieveCreditorInstitutionWithNoStation(bundle) {
    console.log(` - Given an existing creditor institution with no stations related`);
    bundle.creditorInstitutionId = process.env.valid_ci_with_no_station;
    retrieveCIExecuteAPICall(bundle);
}

async function retrieveNonExistingCreditorInstitution(bundle) {
    console.log(` - Given a non-existing creditor institution`);
    bundle.creditorInstitutionId = process.env.invalid_ci;
    let response = await readCreditorInstitution(bundle.creditorInstitutionId);
    debugLog(`Creditor institution retrieving API invocation returned HTTP status code: ${response.status} with body: ${JSON.stringify(response.data)}`);
    assert.strictEqual(response.status, 404);    
}

async function retrieveStationRelatedToBroker(bundle) {
    console.log(` - Given a station related to broker`);
    bundle.stationId = process.env.station_related_to_broker;
    retrieveStationExecuteAPICall(bundle);
}

async function retrieveStationRelatedToCI(bundle) {
    console.log(` - Given a station related to creditor institution`);
    bundle.stationId = process.env.station_related_to_ci;
    retrieveStationExecuteAPICall(bundle);
}


async function retrieveStationsByBroker(bundle) {
    console.log(` - When the client requests the list of stations related to the broker`);
    bundle.response = await getStationsByBroker(bundle.brokerId, bundle.limit, bundle.pageNumber);
    debugLog(`Station retrieving by broker API invocation returned HTTP status code: ${bundle.response.status} with body: ${JSON. stringify(bundle.response.data)}`);
}

async function retrieveStationsByCreditorInstitution(bundle) {
    console.log(` - When the client requests the list of stations related to the creditor institution`);
    bundle.response = await getStationsByCreditorInstitution(bundle.creditorInstitutionId);
    debugLog(`Station retrieving by creditor institution API invocation returned HTTP status code: ${bundle.response.status} with body: ${JSON. stringify(bundle.response.data)}`);
}

async function retrieveBrokerExecuteAPICall(bundle) {
    let response = await readBroker(bundle.brokerId);
    debugLog(`Broker retrieving API invocation returned HTTP status code: ${response.status} with body: ${JSON.stringify(response.data)}`);
    assert.strictEqual(response.status, 200);
    bundle.broker = response.data;
}

async function retrieveCIExecuteAPICall(bundle) {
    let response = await readCreditorInstitution(bundle.creditorInstitutionId);
    debugLog(`Creditor institution retrieving API invocation returned HTTP status code: ${response.status} with body: ${JSON.stringify(response.data)}`);
    assert.strictEqual(response.status, 200);
    bundle.creditorInstitution = response.data;
}

async function retrieveStationExecuteAPICall(bundle) {
    let response = await readStation(bundle.stationId);
    debugLog(`Station retrieving API invocation returned HTTP status code: ${response.status} with body: ${JSON.stringify(response.data)}`);
    assert.strictEqual(response.status, 200);
    bundle.station = response.data;
}



module.exports = {
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
}