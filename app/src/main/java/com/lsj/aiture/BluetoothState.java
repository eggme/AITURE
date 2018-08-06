package com.lsj.aiture;

/**
 * Created by kyyet on 2018-08-06.
 */

public class BluetoothState {

    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;

    public static final int BLUETOOTH_MESSAGE_CHANGE = 1;
    public static final int BLUETOOTH_MESSAGE_READ = 2;
    public static final int BLUETOOTH_MESSAGE_WRITE = 3;
    public static final int BLUETOOTH_MESSAGE_DEVICE_NAME = 4;
    public static final int BLUETOOTH_MESSAGE_TOAST = 5;

    public static String DEVICE_NAME = "device_name";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    public static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;

}
