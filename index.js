import {NativeModules} from 'react-native';

var RNBroadCast = {
    sendBroadCast: function(action,json) {
        NativeModules.RNBroadCast.sendEvent(action,JSON.stringify(json));
    },
    sync: function () {
        NativeModules.RNBroadCast.receiveSystemEvent(true);
    },
    unSync: function () {
        NativeModules.RNBroadCast.receiveSystemEvent(false);
    },
    on: function (action) {
        NativeModules.RNBroadCast.receiveEvent(action);
    },
    remove: function (action) {
        NativeModules.RNBroadCast.removeEvent(action);
    }
};

export default RNBroadCast;
