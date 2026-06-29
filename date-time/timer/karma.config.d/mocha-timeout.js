// Raise Mocha's per-test timeout (default 2s) so time-based tests (timers, expiry) survive on the
// karma/mocha browser runner.
config.client = config.client || {};
config.client.mocha = Object.assign({}, config.client.mocha, { timeout: 300000 });
