package com.gcml.devices.bloodsugar;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothDevice;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.text.TextUtils;

import com.gcml.devices.BluetoothStore;
import com.gcml.devices.R;
import com.gcml.devices.base.IBluetoothView;
import com.gcml.devices.utils.BluetoothConstants;
import com.gcml.devices.utils.SPUtil;
import com.vivachek.ble.sdk.outer.BleManager;
import com.vivachek.ble.sdk.outer.constant.BleActionType;
import com.vivachek.ble.sdk.outer.constant.BleConnectState;
import com.vivachek.ble.sdk.outer.constant.BleMeasureToastType;
import com.vivachek.ble.sdk.outer.entity.BaseGlucoseEntity;
import com.vivachek.ble.sdk.outer.interfaces.OnBleListener;

import java.util.List;

public class BloodsugarGlucWellPresenter implements LifecycleObserver, OnBleListener {
    private SupportActivity activity;
    private IBluetoothView baseView;
    private String name;
    private String address;

    @SuppressLint("RestrictedApi")
    public BloodsugarGlucWellPresenter(SupportActivity activity, IBluetoothView baseView, String name, String address) {
        this.activity = activity;
        this.baseView = baseView;
        this.name = name;
        this.address = address;
        this.activity.getLifecycle().addObserver(this);
        connect();
    }

    private void connect() {
        BleManager.getInstance().connect(activity, address);
        BleManager.getInstance().setOnBleListener(this);
    }


    @Override
    public void onCheckBleCallback(int i) {

    }

    @Override
    public void onStartScan() {

    }

    @Override
    public void onBleScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {

    }

    @Override
    public void onBleScanFailure(int i) {

    }

    @Override
    public void onStoppedScan() {

    }

    @Override
    public void onBleConnectCallback(String s, int connectState) {
        if (TextUtils.isEmpty(s))
            return;
        switch (connectState) {
            case BleConnectState.CONNECT_SUCCESS:
                // 蓝牙连接设备成功
                baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_connected));
                baseView.updateData("initialization", "0.00");
                SPUtil.put(BluetoothConstants.SP.SP_SAVE_BLOODSUGAR, name + "," + address);
                BleManager.getInstance().sendGetSnCommond();
                break;

            case BleConnectState.DISCONNECTED:
                // 已断开设备的蓝牙连接
                if (((Fragment) baseView).isAdded()) {
                    baseView.updateState(BluetoothStore.getString(R.string.bluetooth_device_disconnected));
                }
                break;

            case BleConnectState.CONNECT_FAILURE:
                // 蓝牙连接设备失败

                break;
            default:
                break;
        }
    }
    @Override
    public void onBleSnCallback(String s, String s1) {
        BleManager.getInstance().sendGetUnitCommond();
    }

    @Override
    public void onBleUnitCallback(String s, String s1) {
        BleManager.getInstance().sendSetTimeCommond();
    }

    @Override
    public void onBleTimeCallback(String s, int i) {
        BleManager.getInstance().sendRealtimeMeasureCommond();
    }

    @Override
    public void onBleRealTimeMeasureToastCallback(String s, int measureToastType) {
        switch (measureToastType) {
            case BleMeasureToastType.LAST_TIME_MEASURE:
                break;

            case BleMeasureToastType.INSERTED_TEST_STRIP:
                break;

            case BleMeasureToastType.WAITING_COLLECTION_BLOOD:
                break;

            case BleMeasureToastType.FINISH_COLLECTION_BLOOD:
                break;

            case BleMeasureToastType.MEASURE_ERROR:
                break;
            default:
                break;
        }
    }

    @Override
    public void onBleGlucoseDataCallback(String s, List<BaseGlucoseEntity> list, int actionType) {
        if (list == null || list.isEmpty()) {
            return;
        }
        switch (actionType) {
            case BleActionType.ACTION_REALTIME:
                //实时数据
                break;

            case BleActionType.ACTION_HISTORY:
                //历史数据
                break;
            default:
                break;
        }

        BaseGlucoseEntity baseGlucoseEntity = list.get(0);
        float result = baseGlucoseEntity.getValue();//+ baseGlucoseEntity.getMeasureUnit();
        baseView.updateData(String.format("%.2f", result));

    }

    @SuppressLint("RestrictedApi")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (activity!=null){
            BleManager.getInstance().destroy(activity);
            activity.getLifecycle().removeObserver(this);
        }
        activity=null;
    }

}
