const assert = require("assert");
const { apiConfigHealthCheck } = require("../clients/api_config_client");
const { debugLog } = require("../utility/helpers");


async function executeHealthCheckForAPIConfig() {
    console.log(" - Given APIConfig service running...");
    const response = await apiConfigHealthCheck();
    debugLog(`APIConfig Health check API invocation returned HTTP status code: ${response?.status}`);
    assert.strictEqual(response.status, 200);
}

module.exports = {
    executeHealthCheckForAPIConfig
}
