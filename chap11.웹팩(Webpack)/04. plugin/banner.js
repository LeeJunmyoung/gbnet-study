const childProcess = require('child_process');

module.exports = function banner() {
  const commit = childProcess.execSync('git rev-parse --short HEAD')
  const user = childProcess.execSync('git config user.name')
  const log = childProcess.execSync('git log --pretty=format:"%h - %an, %ar : %s" -5')
  const date = new Date().toLocaleString();
  
  return (
    `commitVersion: ${commit}` +
    `Build Date: ${date}\n` +
    `Author: ${user} \n` +
    `log history \n` +
    `${log}`
  );
}