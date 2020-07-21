let consoleMsg = msg => console.log(`### this is console : ${msg}`);
const alertMsg = msg => console.log(`### this is alert : ${msg}`);


async function AsyncFunction() {
    console.log('async');
}

function PromiseFunction() {
    return new Promise((resolve) => {
        console.log('promise')
        resolve();
    });
}

AsyncFunction();
// async

PromiseFunction();
// promise

alertMsg('test');

consoleMsg('test');

export {consoleMsg, alertMsg, AsyncFunction, PromiseFunction};