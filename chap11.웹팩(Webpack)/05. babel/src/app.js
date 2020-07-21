"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.AsyncFunction = AsyncFunction;
exports.PromiseFunction = PromiseFunction;
exports.alertMsg = exports.consoleMsg = void 0;

require("regenerator-runtime/runtime");

require("core-js/modules/es6.promise");

require("core-js/modules/es6.object.to-string");

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () { var self = this, args = arguments; return new Promise(function (resolve, reject) { var gen = fn.apply(self, args); function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

var consoleMsg = function consoleMsg(msg) {
  return console.log("### this is console : ".concat(msg));
};

exports.consoleMsg = consoleMsg;

var alertMsg = function alertMsg(msg) {
  return console.log("### this is alert : ".concat(msg));
};

exports.alertMsg = alertMsg;

function AsyncFunction() {
  return _AsyncFunction.apply(this, arguments);
}

function _AsyncFunction() {
  _AsyncFunction = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee() {
    return regeneratorRuntime.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            console.log('async');

          case 1:
          case "end":
            return _context.stop();
        }
      }
    }, _callee);
  }));
  return _AsyncFunction.apply(this, arguments);
}

function PromiseFunction() {
  return new Promise(function (resolve) {
    console.log('promise');
    resolve();
  });
}

AsyncFunction(); // async

PromiseFunction(); // promise

alertMsg('test');
consoleMsg('test');
