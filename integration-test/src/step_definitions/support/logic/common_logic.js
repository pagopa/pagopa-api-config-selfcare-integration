const assert = require("assert");

async function assertEmptyList(response) {
    console.log(` - Then the client receives an empty list..`);
    assert.ok(response.length === 0);
}

async function assertErrorMessage(response) {
    console.log(` - Then the client receives an error message..`);
    assert.ok(response !== null && response !== undefined);
}

async function assertNonEmptyList(response) {
    console.log(` - Then the client receives a non-empty list..`);
    assert.ok(response.length > 0);
}

async function assertStatusCode(response, statusCode) {
    console.log(` - Then the client receives status code [${statusCode}]..`);
    assert.strictEqual(response.status, statusCode);
}

async function assertUndefined(response, field) {
    console.log(` - the client does not receives the field [${field}]..`);
    assert.ok(response[field] === undefined);
}
assertUndefined

module.exports = {
    assertEmptyList,
    assertErrorMessage,
    assertNonEmptyList,
    assertStatusCode,
    assertUndefined
}
