const { get } = require("../utility/axios_common");
const { debugLog } = require("../utility/helpers");

const app_host = process.env.APP_HOST;

function getChannelsByPSPBroker(pspBrokerId, limit, pageNumber) {
    const host = `${app_host}/brokerspsp/${pspBrokerId}/channels?limit=${limit}&page=${pageNumber}`;
    debugLog(`Calling endpoint: [${host}]`);
    return get(host, {
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}

function getStationsByBroker(brokerId, limit, pageNumber) {
    const host = `${app_host}/brokers/${brokerId}/stations?limit=${limit}&page=${pageNumber}`;
    debugLog(`Calling endpoint: [${host}]`);
    return get(host, {
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}

function getStationsByCreditorInstitution(creditorInstitutionCode, limit, pageNumber) {
    const host = `${app_host}/creditorinstitutions/${creditorInstitutionCode}/stations?limit=${limit}&page=${pageNumber}`;
    debugLog(`Calling endpoint: [${host}]`);
    return get(host, {
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}

function getApplicationCodesByCreditorInstitution(creditorInstitutionCode, showUsedCodes) {
    const host = `${app_host}/creditorinstitutions/${creditorInstitutionCode}/applicationcodes?showUsedCodes=${showUsedCodes}`;
    debugLog(`Calling endpoint: [${host}]`);
    return get(host, {
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}

function getSegregationCodesByCreditorInstitution(creditorInstitutionCode, showUsedCodes, stationSubstring) {
    let host = ``;
    if (stationSubstring === undefined) {
        host = `${app_host}/creditorinstitutions/${creditorInstitutionCode}/segregationcodes?showUsedCodes=${showUsedCodes}`;
    } else {
        host = `${app_host}/creditorinstitutions/${creditorInstitutionCode}/segregationcodes?showUsedCodes=${showUsedCodes}&service=${stationSubstring}`;
    }
    debugLog(`Calling endpoint: [${host}]`);
    return get(host, {
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}

module.exports = {
    getChannelsByPSPBroker,
    getStationsByBroker,
    getStationsByCreditorInstitution,
    getApplicationCodesByCreditorInstitution,
    getSegregationCodesByCreditorInstitution
}
