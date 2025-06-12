package com.caac.weeklyreport.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PhoneInfo {
    private String phoneNumber;

    @JsonProperty("purePhoneNumber")
    private String purePhoneNumber;

    @JsonProperty("countryCode")
    private String countryCode;

    private Watermark watermark;

    // 构造函数、getter和setter方法
    public PhoneInfo() {}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPurePhoneNumber() {
        return purePhoneNumber;
    }

    public void setPurePhoneNumber(String purePhoneNumber) {
        this.purePhoneNumber = purePhoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Watermark getWatermark() {
        return watermark;
    }

    public void setWatermark(Watermark watermark) {
        this.watermark = watermark;
    }

    @Override
    public String toString() {
        return "PhoneInfo{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", purePhoneNumber='" + purePhoneNumber + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", watermark=" + watermark +
                '}';
    }

    // 水印内部类
    public static class Watermark {
        private long timestamp;
        private String appid;

        // 构造函数、getter和setter方法
        public Watermark() {}

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        @Override
        public String toString() {
            return "Watermark{" +
                    "timestamp=" + timestamp +
                    ", appid='" + appid + '\'' +
                    '}';
        }
    }
}


