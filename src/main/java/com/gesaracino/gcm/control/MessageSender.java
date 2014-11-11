package com.gesaracino.gcm.control;

import com.gesaracino.gcm.entity.DeviceRegistration;
import com.gesaracino.gcm.entity.Property;
import com.google.android.gcm.server.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
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
    @EJB
    private PropertyRepository propertyRepository;

    @EJB
    private DeviceRegistrationRepository deviceRegistrationRepository;

    public void send(Long deviceRegistrationId, String text) {
        send(Arrays.asList(new Long[] {deviceRegistrationId}), text);
    }

    public void send(List<Long> deviceRegistrationIds, String text) {
        int multicastSize = Integer.valueOf(propertyRepository.getPropertyValue(Property.PropertyName.MULTICAST_SIZE));
        int nThreads = Integer.valueOf(propertyRepository.getPropertyValue(Property.PropertyName.THREADS));
        ArrayList<String> partialRegistrationIds = new ArrayList<String>();

        Executor threadPool = Executors.newFixedThreadPool(nThreads);

        for(int i = 0; i < deviceRegistrationIds.size(); i ++) {
            DeviceRegistration deviceRegistration = deviceRegistrationRepository.getDeviceRegistration(deviceRegistrationIds.get(i));
            partialRegistrationIds.add(deviceRegistration.getRegistrationId());

            if(partialRegistrationIds.size() == multicastSize || (i + 1) == deviceRegistrationIds.size()) {
                threadPool.execute(new MessageSenderWorker(new ArrayList<String>(partialRegistrationIds), text));
                partialRegistrationIds.clear();
            }
        }
    }

    private class MessageSenderWorker implements Runnable {
        private List<String> registrationIds;
        private String text;

        private MessageSenderWorker(List<String> registrationIds, String text) {
            this.registrationIds = registrationIds;
            this.text = text;
        }

        @Override
        public void run() {
            String dataMessageKey = propertyRepository.getPropertyValue(Property.PropertyName.DATA_MESSAGE_KEY);

            Message message = new Message.Builder().collapseKey("1").
                    timeToLive(3).
                    delayWhileIdle(true).
                    addData(dataMessageKey, text).
                    build();
            MulticastResult multicastResult;

            try {
                String serverApiKey = propertyRepository.getPropertyValue(Property.PropertyName.SERVER_API_KEY);
                int retries = Integer.valueOf(propertyRepository.getPropertyValue(Property.PropertyName.RETRIES));
                String useHttpProxy = propertyRepository.getPropertyValue(Property.PropertyName.USE_HTTP_PROXY);
                Sender sender = "Y".equals(useHttpProxy) ? new HttpProxySender(serverApiKey) : new Sender(serverApiKey);
                multicastResult = sender.send(message, registrationIds, retries);
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
                        deviceRegistrationRepository.updateDeviceRegistrationByRegistrationId(regId, deviceRegistration);
                    }
                } else {
                    String error = result.getErrorCodeName();

                    if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                        // application has been removed from device - unregister it
                        System.out.println("Unregistered device: " + regId);
                        deviceRegistrationRepository.deleteDeviceRegistrationByRegistrationId(regId);
                    } else {
                        System.out.println("Error sending message to " + regId + ": " + error);
                    }
                }
            }
        }
    }

    private class HttpProxySender extends Sender {
        private HttpProxySender(String key) {
            super(key);
        }

        @Override
        protected HttpURLConnection getConnection(String url) throws IOException {
            String proxyHostName = propertyRepository.getPropertyValue(Property.PropertyName.HTTP_PROXY_HOSTNAME);
            int proxyPort = Integer.valueOf(propertyRepository.getPropertyValue(Property.PropertyName.HTTP_PROXY_PORT));

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostName, proxyPort));
            return (HttpURLConnection) new URL(url).openConnection(proxy);
        }
    }
}
