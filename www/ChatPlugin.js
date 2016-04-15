var exec = require('cordova/exec');

exports.showBar = function(success, error, messageHandler, focusHandler) {
  exec(function(response) {
    switch (response.action) {
      case 'message':
        messageHandler(response.message);
        break;
      case 'focus':
        focusHandler();
        break;
      default:
        success();
    }
  }, error, 'ChatPlugin', 'showBar', []);
};

exports.hideBar = function(success, error) {
  exec(success, error, 'ChatPlugin', 'hideBar', []);
};

exports.hideKeyboard = function(success, error) {
  exec(success, error, 'ChatPlugin', 'hideKeyboard', []);
};
