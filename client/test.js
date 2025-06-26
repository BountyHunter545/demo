const { spawn } = require('child_process');
const path = require('path');
const http = require('http');
const puppeteer = require('puppeteer');
const fs = require('fs');

async function waitForServer(port) {
  for (let i = 0; i < 20; i++) {
    try {
      await new Promise((resolve, reject) => {
        const req = http.get({ host: 'localhost', port }, res => {
          res.destroy();
          resolve();
        });
        req.on('error', reject);
      });
      return;
    } catch (e) {
      await new Promise(r => setTimeout(r, 500));
    }
  }
  throw new Error('Server not responding');
}

async function run() {
  const serverJar = path.join('..', 'server', 'target', 'todo-server-0.0.1-SNAPSHOT.jar');
  const server = spawn('java', ['-jar', serverJar], { stdio: 'inherit' });
  await waitForServer(8080);

  const client = spawn('node', ['server.js'], { stdio: 'inherit' });
  await waitForServer(3000);

  const browser = await puppeteer.launch();
  const page = await browser.newPage();
  await page.goto('http://localhost:3000');

  await page.type('input', 'task');
  await page.click('button');
  await page.waitForSelector('li');

  if (!fs.existsSync('results')) fs.mkdirSync('results');
  await page.screenshot({ path: path.join('results', 'todo.png') });

  await browser.close();
  client.kill();
  server.kill();
}

run().catch(err => {
  console.error(err);
  process.exit(1);
});
