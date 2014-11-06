package com.gesaracino.gcm.entity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Gerardo Saracino on 06/11/2014.
 */
public class Message {
    @NotNull
    private List<Long> deviceRegistrationId;

    @NotNull
    private String text;

    public List<Long> getDeviceRegistrationId() {
        return deviceRegistrationId;
    }

    public void setDeviceRegistrationId(List<Long> deviceRegistrationId) {
        this.deviceRegistrationId = deviceRegistrationId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "deviceRegistrationId=" + deviceRegistrationId +
                ", text='" + text + '\'' +
                '}';
    }
}
