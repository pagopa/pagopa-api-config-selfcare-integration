const { get } = require("../utility/axios_common");
const { debugLog } = require("../utility/helpers");

const api_config_host = process.env.APICONFIG_HOST;

function apiConfigHealthCheck() {    
    const host = `${api_config_host}/info`;
    debugLog(`Calling endpoint: [${host}]`);
    return get(host, {
        headers: {
            "Host": process.env.host_header,
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}

function readBroker(brokerId) {
    const host = `${api_config_host}/brokers/${brokerId}`;
    debugLog(`Calling endpoint: [${host}]`);  
    return get(host, {
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}

function readCreditorInstitution(organizationFiscalCode) {
    const host = `${api_config_host}/creditorinstitutions/${organizationFiscalCode}`;
    debugLog(`Calling endpoint: [${host}]`);
    return get(host, {
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}

function readStation(stationId, organizationFiscalCode) {   
    const host = `${api_config_host}/stations/${stationId}`;
    debugLog(`Calling endpoint: [${host}]`);     
    return get(host, {
        headers: {
            "Ocp-Apim-Subscription-Key": process.env.SUBKEY
        }
    })
}

module.exports = {
    apiConfigHealthCheck,
    readBroker,
    readCreditorInstitution,
    readStation
}
