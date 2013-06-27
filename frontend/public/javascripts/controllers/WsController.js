define([
  'Logger',
  'util/WsException'
], function(Logger, WsException) {
  var hostname;

  var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;

  var socket;

  var connectionInitialized;

  var listeners = {
    onOpen: {},
    onMessage: {},
    onClose: {},
    onError: {}
  };

  var WsWrapper = function() {
  };

  WsWrapper.prototype.connect = function(host) {
    if(connectionInitialized) {
      throw new WsException("socket already connected");
    }
    hostname = host;
    socket = new WS(hostname);
    connectionInitialized = true;
    initializeWebsocket();
  };

  var initializeWebsocket = function() {
    var events = {
      onopen:    "onOpen",
      onmessage: "onMessage",
      onerror:   "onError",
      onclose:   "onClose"
    };
    for(var wsEvent in events) {
      var eventName = events[wsEvent];
      socket[wsEvent] = initEventFunction(eventName);
    }
  };

  var initEventFunction = function(eventName) {
    return function(evt) {
      for(var i in listeners[eventName]) {
        listeners[eventName][i](evt);
      }
    };
  };

  var checkListenerType = function(type) {
    if(!(type in listeners)) {
      throw new WsException("event " + type + " does not exist");
    }
  };

  WsWrapper.prototype.getHostName = function() {
    return hostname;
  };

  WsWrapper.prototype.addListener = function(type, name, callback) {
    checkListenerType(type);
    if(name in listeners[type]) {
      throw new WsException("listener " + name + " already defined for " + type + " event");
    }
    listeners[type][name] = callback;
  };

  WsWrapper.prototype.removeListener = function(type, name) {
    checkListenerType(type);
    if(!(name in listeners[type])) {
      throw new WsException("listener " + name + " not defined for " + type + " event");
    }
    delete listeners[type][name];
  };

  WsWrapper.prototype.addOnOpenListener = function(name, callback) {
    this.addListener("onOpen", name, callback);
  };

  WsWrapper.prototype.addOnMessageListener = function(name, callback) {
    this.addListener("onMessage", name, callback);
  };

  WsWrapper.prototype.addOnCloseListener = function(name, callback) {
    this.addListener("onClose", name, callback);
  };

  WsWrapper.prototype.addOnErrorListener = function(name, callback) {
    this.addListener("onError", name, callback);
  };

  WsWrapper.prototype.removeOnOpenListener = function(name) {
    this.removeListener("onOpen", name);
  };

  WsWrapper.prototype.removeOnMessageListener = function(name) {
    this.removeListener("onMessage", name);
  };

  WsWrapper.prototype.removeOnCloseListener = function(name) {
    this.removeListener("onClose", name);
  };

  WsWrapper.prototype.removeOnErrorListener = function(name) {
    this.removeListener("onError", name);
  };

  WsWrapper.prototype.close = function() {
    if(!connectionInitialized) {
      throw new WsException("socket not opened");
    }
    socket.close();
    connectionInitialized = false;
  };

  WsWrapper.prototype.send = function(message) {
    if(!connectionInitialized) {
      throw new WsException("socket not opened");
    }
    socket.send(message);
  };

  return WsWrapper;
});
