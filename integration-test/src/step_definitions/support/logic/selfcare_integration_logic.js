const assert = require("assert");
const {
    readBroker,
    readChannel,
    readCreditorInstitution,
    readPSPBroker,
    readStation
} = require("../clients/api_config_client");
const {
    getChannelsByPSPBroker,
    getStationsByBroker,
    getStationsByCreditorInstitution,
    getApplicationCodesByCreditorInstitution
} = require("../clients/selfcare_integration_client.js");
const { debugLog } = require("../utility/helpers");


async function assertChannelIncludedInResponse(channelId, response) {
    console.log(` - Then the channel is included in the result list`);
    let channels = response.data?.channels;
    let exists = false;
    let channelCount = channels.length;
    for (i = 0; i < channelCount && !exists; i ++) {
      exists = channelId === channels[i].channel_code;
    }
    assert.strictEqual(true, exists);
}

async function assertStationIncludedInResponse(stationId, response) {
    console.log(` - Then the station is included in the result list`);
    let stations = response.data?.stations;
    let exists = false;
    let stationCount = stations.length;
    for (i = 0; i < stationCount && !exists; i ++) {
      exists = stationId === stations[i].station_code;
    }
    assert.strictEqual(true, exists);
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


async function retrievePSPBroker(bundle) {
    console.log(` - Given an existing PSP broker`);
    bundle.pspBrokerId = process.env.valid_pspbroker;
    retrievePSPBrokerExecuteAPICall(bundle);    
}

async function retrievePSPBrokerWithNoChannel(bundle) {
    bundle.pspBrokerId = process.env.valid_pspbroker_with_no_channel;
    console.log(` - Given an existing PSP broker with no channel related`);
    retrievePSPBrokerExecuteAPICall(bundle);    
}

async function retrieveNonExistingPSPBroker(bundle) {
    console.log(` - Given a non-existing PSP broker`);
    bundle.pspBrokerId = process.env.invalid_pspbroker;
    let response = await readPSPBroker(bundle.pspBrokerId);
    debugLog(`Broker retrieving API invocation returned HTTP status code: ${response.status} with body: ${JSON.stringify(response.data)}`);
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

async function retrieveChannelRelatedToPSPBroker(bundle) {
    console.log(` - Given a channel related to PSP broker`);
    bundle.channelId = process.env.channel_related_to_pspbroker;
    let response = await readChannel(bundle.channelId);
    debugLog(`Channel retrieving API invocation returned HTTP status code: ${response.status} with body: ${JSON.stringify(response.data)}`);
    assert.strictEqual(response.status, 200);
    bundle.channel = response.data;
}

async function retrieveStationsByBroker(bundle) {
    console.log(` - When the client requests the list of stations related to the broker`);
    bundle.response = await getStationsByBroker(bundle.brokerId, bundle.limit, bundle.pageNumber);
    debugLog(`Station retrieving by broker API invocation returned HTTP status code: ${bundle.response.status} with body: ${JSON. stringify(bundle.response.data)}`);
}

async function retrieveStationsByCreditorInstitution(bundle) {
    console.log(` - When the client requests the list of stations related to the creditor institution`);
    bundle.response = await getStationsByCreditorInstitution(bundle.creditorInstitutionId, bundle.limit, bundle.pageNumber);
    debugLog(`Station retrieving by creditor institution API invocation returned HTTP status code: ${bundle.response.status} with body: ${JSON. stringify(bundle.response.data)}`);
}

async function retrieveChannelsByPSPBroker(bundle) {
    console.log(` - When the client requests the list of channels related to the PSP broker`);
    bundle.response = await getChannelsByPSPBroker(bundle.pspBrokerId, bundle.limit, bundle.pageNumber);
    debugLog(`Channel retrieving by PSP broker API invocation returned HTTP status code: ${bundle.response.status} with body: ${JSON. stringify(bundle.response.data)}`);
}

async function retrieveApplicationCodesByCreditorInstitution(bundle, showUsedCodes) {
    console.log(` - When the client requests the list of application codes related to the creditor institution`);
    bundle.response = await getApplicationCodesByCreditorInstitution(bundle.creditorInstitutionId, showUsedCodes);
    debugLog(`Channel retrieving by PSP broker API invocation returned HTTP status code: ${bundle.response.status} with body: ${JSON. stringify(bundle.response.data)}`);
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

async function retrievePSPBrokerExecuteAPICall(bundle) {
    let response = await readPSPBroker(bundle.pspBrokerId);
    debugLog(`PSP Broker retrieving API invocation returned HTTP status code: ${response.status} with body: ${JSON.stringify(response.data)}`);
    assert.strictEqual(response.status, 200);
    bundle.pspBroker = response.data;
}

async function retrieveStationExecuteAPICall(bundle) {
    let response = await readStation(bundle.stationId);
    debugLog(`Station retrieving API invocation returned HTTP status code: ${response.status} with body: ${JSON.stringify(response.data)}`);
    assert.strictEqual(response.status, 200);
    bundle.station = response.data;
}



module.exports = {
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
    retrieveApplicationCodesByCreditorInstitution,
    retrieveStationRelatedToBroker,
    retrieveStationRelatedToCI,
    retrieveChannelRelatedToPSPBroker
}
