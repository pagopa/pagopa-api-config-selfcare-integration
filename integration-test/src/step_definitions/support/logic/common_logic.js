const assert = require("assert");

async function assertEmptyList(response) {
    console.log(` - -> the client receives an empty list of flows..`);
    assert.ok(response.data.length === 0);
}

async function assertErrorMessage(response) {
    console.log(` - -> the client receives an error message..`);
    assert.ok(response.data !== null && response.data !== undefined);
}

async function assertNonEmptyList(response) {
    console.log(` - -> the client receives a non-empty list of flows..`);
    assert.ok(response.data.length > 0);
}

async function assertStatusCode(response, statusCode) {
    console.log(` - -> the client receives status code [${statusCode}]..`);
    assert.strictEqual(response.status, statusCode);
}

module.exports = {
    assertEmptyList,
    assertErrorMessage,
    assertNonEmptyList,
    assertStatusCode,
}
