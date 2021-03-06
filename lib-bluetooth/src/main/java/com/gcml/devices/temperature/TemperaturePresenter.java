package com.gcml.devices.temperature;

import com.gcml.devices.BluetoothStore;
import com.gcml.devices.base.BaseBluetooth;
import com.gcml.devices.base.BluetoothType;
import com.gcml.devices.base.BluetoothBean;
import com.gcml.devices.base.IBluetoothView;
import com.gcml.devices.utils.T;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;

import java.util.HashMap;
import java.util.UUID;

public class TemperaturePresenter extends BaseBluetooth {
    private static final String AILIKANG_SERVICE = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String AILIKANG_NOTIFY = "0000ffb2-0000-1000-8000-00805f9b34fb";

    private static final String FUDAKANG_SERVICE = "0000fc00-0000-1000-8000-00805f9b34fb";
    private static final String FUDAKANG_NOTIFY = "0000fca1-0000-1000-8000-00805f9b34fb";

    private static final String MEIDILIAN_SERVICE = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String MEIDILIAN_NOTIFY = "0000ffb2-0000-1000-8000-00805f9b34fb";

    private static final String SELF_SERVICE = "00001910-0000-1000-8000-00805f9b34fb";
    private static final String SELF_NOTIFY = "0000fff2-0000-1000-8000-00805f9b34fb";

    public TemperaturePresenter(IBluetoothView owner, BluetoothBean brandMenu) {
        super(owner,brandMenu);
        //开始搜索
        start(BluetoothType.BLUETOOTH_TYPE_BLE, brandMenu.getBluetoothAddress(), brandMenu.getBluetoothName());
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        baseView.updateData("initialization", "0.00");
        if (name.startsWith("AET-WD")) {
            handleAilikang(address);
            return;
        }
        if (name.startsWith("ClinkBlood")) {
            handleFudakang(address);
            return;
        }
        if (name.startsWith("MEDXING-IRT")) {
            handleMeidilian(address);
            return;
        }
        if (name.startsWith("FSRKB-EWQ01")) {
            handleSelf(address);
            return;
        }
        baseView.updateState("未兼容该设备:" + name + ":::" + address);
    }


    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected(String address) {

    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return null;
    }

    private void handleSelf(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(SELF_SERVICE), UUID.fromString(SELF_NOTIFY), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if (bytes.length > 6) {
                    int data = bytes[6] & 0xff;
                    if (data < 44) {
                        T.showShort("测量温度不正常");
                        return;
                    }
                    double v = (data - 44.0) % 10;
                    double result = (30.0 + (data - 44) / 10 + v / 10);
                    baseView.updateData(result + "");
                }
            }

            @Override
            public void onResponse(int i) {

            }
        });
    }

    private void handleMeidilian(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(MEIDILIAN_SERVICE), UUID.fromString(MEIDILIAN_NOTIFY), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if (bytes[2]==0x06) {
                    float result = ((float) (bytes[7] << 8) + (float) (bytes[6] & 0xff)) / 10;
                    if (result < 50) {
                        baseView.updateData(result + "");
                    }
                }
            }

            @Override
            public void onResponse(int i) {

            }
        });
    }

    private void handleFudakang(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(FUDAKANG_SERVICE), UUID.fromString(FUDAKANG_NOTIFY), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if (bytes.length == 8 && bytes[4] != 85) {
                    byte[] bytes1 = new byte[8];
                    System.arraycopy(bytes, 0, bytes1, 0, 8);
                    float result = ((int) (((bytes1[4] << 8) + (float) (bytes1[5] & 0xff)) / 10)) / 10.0f;
                    baseView.updateData(result + "");
                }
            }

            @Override
            public void onResponse(int i) {

            }
        });
    }

    private void handleAilikang(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(AILIKANG_SERVICE), UUID.fromString(AILIKANG_NOTIFY), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                float result;
                if (bytes.length == 13) {
                    result = bytes[9] + bytes[10] / 10 / 10.0f;
                } else {
                    result = 0.0f;
                }
                baseView.updateData(result + "");
            }

            @Override
            public void onResponse(int i) {

            }
        });
    }

}
