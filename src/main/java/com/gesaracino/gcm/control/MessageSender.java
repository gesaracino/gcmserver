package com.gesaracino.gcm.control;

import com.gesaracino.gcm.entity.DeviceRegistration;
import com.google.android.gcm.server.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Gerardo Saracino on 05/11/2014.
 */

@Stateless
public class MessageSender {
    private static final String SERVER_API_KEY = "AIzaSyCWANqNXq72mWW44dVtsFg4hF44F2GxdQ8";
    private static final int MULTICAST_SIZE = 1000;
    private static final int RETRIES = 3;
    private static final String DATA_MESSAGE_KEY = "message";
    private static final Executor THREAD_POOL = Executors.newFixedThreadPool(5);

    @EJB
    private DeviceRegistrationDatastore datastore;

    public void send(Long deviceRegistrationId, String text) {
        send(Arrays.asList(new Long[] {deviceRegistrationId}), text);
    }

    public void send(List<Long> deviceRegistrationIds, String text) {
        ArrayList<String> partialRegistrationIds = new ArrayList<String>();

        for(int i = 0; i < deviceRegistrationIds.size(); i ++) {
            DeviceRegistration deviceRegistration = datastore.getDeviceRegistration(deviceRegistrationIds.get(i));
            partialRegistrationIds.add(deviceRegistration.getRegistrationId());

            if(partialRegistrationIds.size() == MULTICAST_SIZE || (i + 1) == deviceRegistrationIds.size()) {
                asyncSend(partialRegistrationIds, text);
                partialRegistrationIds.clear();
            }
        }
    }

    private void asyncSend(List<String> gcmRegistrationIds, String text) {
        final String textMessage = new String(text);
        final ArrayList<String> registrationIds = new ArrayList<String>(gcmRegistrationIds);

        THREAD_POOL.execute(new Runnable() {
            public void run() {
                Message message = new Message.Builder().collapseKey("1").
                        timeToLive(3).
                        delayWhileIdle(true).
                        addData(DATA_MESSAGE_KEY, textMessage).
                        build();
                MulticastResult multicastResult;

                try {
                    multicastResult = new Sender(SERVER_API_KEY).send(message, registrationIds, RETRIES);
                } catch (IOException e) {
                    System.out.println("Error posting messages");
                    return;
                }

                // analyze the results
                for (int i = 0; i < registrationIds.size(); i ++) {
                    String regId = registrationIds.get(i);
                    Result result = multicastResult.getResults().get(i);
                    String messageId = result.getMessageId();

                    if (messageId != null) {
                        System.out.println("Succesfully sent message to device: " + regId + "; messageId = " + messageId);
                        String canonicalRegId = result.getCanonicalRegistrationId();

                        if (canonicalRegId != null) {
                            // same device has more than on registration id: update it
                            System.out.println("canonicalRegId " + canonicalRegId);
                            DeviceRegistration deviceRegistration = new DeviceRegistration();
                            deviceRegistration.setRegistrationId(canonicalRegId);
                            datastore.updateDeviceRegistrationByRegistrationId(regId, deviceRegistration);
                        }
                    } else {
                        String error = result.getErrorCodeName();

                        if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                            // application has been removed from device - unregister it
                            System.out.println("Unregistered device: " + regId);
                            datastore.deleteDeviceRegistrationByRegistrationId(regId);
                        } else {
                            System.out.println("Error sending message to " + regId + ": " + error);
                        }
                    }
                }
            }
        });
    }
}
