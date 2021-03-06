package com.gcml.devices.base;

import java.util.List;

public class DetectionDataBean {
    private int DetectionType;
    private List<DetectionData> detectionDataList;

    public DetectionDataBean() {
    }

    public DetectionDataBean(int detectionType, List<DetectionData> detectionDataList) {
        DetectionType = detectionType;
        this.detectionDataList = detectionDataList;
    }

    public int getDetectionType() {
        return DetectionType;
    }

    public void setDetectionType(int detectionType) {
        DetectionType = detectionType;
    }

    public List<DetectionData> getDetectionDataList() {
        return detectionDataList;
    }

    public void setDetectionDataList(List<DetectionData> detectionDataList) {
        this.detectionDataList = detectionDataList;
    }
}
