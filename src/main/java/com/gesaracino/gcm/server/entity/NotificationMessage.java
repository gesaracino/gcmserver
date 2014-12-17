package com.gesaracino.gcm.server.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by Gerardo Saracino on 06/11/2014.
 */
public class NotificationMessage {
    @NotNull
    @Size(min = 1)
    private List<Long> deviceRegistrationIds;

    @NotNull
    private String text;

    public List<Long> getDeviceRegistrationIds() {
        return deviceRegistrationIds;
    }

    public void setDeviceRegistrationIds(List<Long> deviceRegistrationIds) {
        this.deviceRegistrationIds = deviceRegistrationIds;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "deviceRegistrationIds=" + deviceRegistrationIds +
                ", text='" + text + '\'' +
                '}';
    }
}
