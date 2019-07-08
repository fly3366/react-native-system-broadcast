import {NativeModules} from 'react-native';

var RNBroadCast = {
    sendBroadCast: function(action,json) {
        NativeModules.RNBroadCast.sendEvent(action,JSON.stringify(json));
    },
    on: function() {
        NativeModules.RNBroadCast.receiveEvent(true);
    },
    off: function() {
        NativeModules.RNBroadCast.receiveEvent(false);
    }
};

export default RNBroadCast;
