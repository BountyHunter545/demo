const assert = require('assert');
const { spawn } = require('child_process');
const path = require('path');

async function run() {
  const server = spawn('java', ['-cp', path.join('..', 'server', 'target', 'test-classes'), 'com.example.todo.Application']);
  await new Promise(r => setTimeout(r, 500));
  try {
    let res = await fetch('http://localhost:8080/api/todos');
    assert.strictEqual(res.status, 200);
    const list = await res.json();
    console.log('Fetched list', list);
  } finally {
    server.kill();
  }
}

run().catch(err => { console.error(err); process.exit(1); });
