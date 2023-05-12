const { get } = require("../utility/axios_common");
const { debugLog } = require("../utility/helpers");

const api_host = process.env.API_HOST;

function getStationsByBroker(brokerId, limit, pageNumber) {
    const host = `${api_host}/brokers/${brokerId}/stations&limit=${limit}&pageNumber=${pageNumber}`;
    debugLog(`Calling endpoint: [${host}]`);
    return get(host, {
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}

function getStationsByCreditorInstitution(creditorInstitutionCode) {
    const host = `${api_host}/creditorinstitutions/${creditorInstitutionCode}/stationsdetails`;
    debugLog(`Calling endpoint: [${host}]`);
    return get(host, {
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}



module.exports = {
    getStationsByBroker,
    getStationsByCreditorInstitution
}