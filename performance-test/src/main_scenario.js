import http from 'k6/http';
import {check} from 'k6';
import {SharedArray} from 'k6/data';

export let options = JSON.parse(open(__ENV.TEST_TYPE));

// read configuration
// note: SharedArray can currently only be constructed inside init code
// according to https://k6.io/docs/javascript-api/k6-data/sharedarray
const varsArray = new SharedArray('vars', function () {
  return JSON.parse(open(`./${__ENV.VARS}`)).environment;
});
// workaround to use shared array (only array should be used)
const vars = varsArray[0];
const app_host = `${vars.app_host}`;
const subkey = `${__ENV.sub_key}`;

const brokerId = `${vars.id_broker}`;
const brokersPspId = `${vars.id_broker_psp}`;
const stationId = `${vars.id_station}`;
const channelId = `${vars.id_channel}`;
const creditorInstitutionId = `${vars.id_ci}`;


export function setup() {
  // Before All
  // setup code (once)
  // The setup code runs, setting up the test environment (optional) and generating data
  // used to reuse code for the same VU

}

function precondition() {
  // no pre conditions
}

function postcondition() {

  // Delete the new entity created
}

export default function () {

  // Initialize response variable
  let response = '';

  // Initialize parameter constants
  const params = {
    headers: {
      'Content-Type': 'application/json',
      "Ocp-Apim-Subscription-Key": subkey,
    },
  };

  // starting the execution
  precondition();

  // Testing: brokers/:brokerId/stations?page=0&limit=50
  response = http.get(`${app_host}/brokers/${brokerId}/stations?page=0&limit=50`, params);
  check(response, { 'check status is 200': (resp) => resp.status === 200 });

  // Testing: brokers/:brokerId/stations?page=0&limit=50&stationId=:stationId
  response = http.get(`${app_host}/brokers/${brokerId}/stations?page=0&limit=50&stationId=${stationId}`, params);
  check(response, { 'check status is 200': (resp) => resp.status === 200 });

  // Testing: creditorinstitutions/:creditorInstitutionId/stations?page=0&limit=50
  response = http.get(`${app_host}/creditorinstitutions/${creditorInstitutionId}/stations?page=0&limit=50`, params);
  check(response, { 'check status is 200': (response) => response.status === 200 });

  // Testing: brokerPsps/:brokerPspId/channels?page=0&limit=50
  response = http.get(`${app_host}/brokerspsp/${brokersPspId}/channels?page=0&limit=50`, params);
  check(response, { 'check status is 200': (resp) => resp.status === 200 });

  // Testing: brokerPsps/:brokerPspId/channels?page=0&limit=50&channelId=:channelId
  response = http.get(`${app_host}/brokerspsp/${brokersPspId}/channels?page=0&limit=50&channelId=${channelId}`, params);
  check(response, { 'check status is 200': (response) => response.status === 200 });

  // Testing: creditorinstitutions/:creditorInstitutionId/applicationcodes
  response = http.get(`${app_host}/creditorinstitutions/${creditorInstitutionId}/applicationcodes`, params);
  check(response, { 'check status is 200': (response) => response.status === 200 });

  // Testing: creditorinstitutions/:creditorInstitutionId/applicationcodes?showUsedCodes=false
  response = http.get(`${app_host}/creditorinstitutions/${creditorInstitutionId}/applicationcodes?showUsedCodes=false`, params);
  check(response, { 'check status is 200': (response) => response.status === 200 });

  // ending the execution
  postcondition();

}

export function teardown(data) {
  // After All
  // teardown code
}
