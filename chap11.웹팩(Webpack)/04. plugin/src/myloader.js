module.exports = function myloader (content) {
    console.log('##### myloader가 동작함\n');
    console.log();
    console.log(content);
    console.log();
    return content;
};