package com.gesaracino.gcm.server.control;

import com.gesaracino.gcm.server.entity.DeviceRegistration;
import com.gesaracino.gcm.server.entity.Property;
import com.google.android.gcm.server.*;

import org.apache.log4j.Logger;

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
	private static final Logger LOGGER = Logger.getLogger(MessageSender.class);
	
    @EJB
    private PropertyRepository propertyRepository;

    @EJB
    private DeviceRegistrationRepository deviceRegistrationRepository;

    public void send(Long deviceRegistrationId, String text) {
        send(Arrays.asList(new Long[] {deviceRegistrationId}), text);
    }

    public void send(List<Long> deviceRegistrationIds, String text) {
    	LOGGER.info("Sending message with text: \"" + text + "\" to devices with Device Registration ids: " + deviceRegistrationIds);
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
            String collapseKey = propertyRepository.getPropertyValue(Property.PropertyName.COLLAPSE_KEY);
            int timeToLive = Integer.valueOf(propertyRepository.getPropertyValue(Property.PropertyName.TIME_TO_LIVE));
            boolean delayWhileIdle = Boolean.valueOf(propertyRepository.getPropertyValue(Property.PropertyName.DELAY_WHILE_IDLE));

            Message message = new Message.Builder().
                    collapseKey(collapseKey).
                    timeToLive(timeToLive).
                    delayWhileIdle(delayWhileIdle).
                    addData(dataMessageKey, text).
                    build();
            MulticastResult multicastResult;

            try {
                String serverApiKey = propertyRepository.getPropertyValue(Property.PropertyName.SERVER_API_KEY);
                int retries = Integer.valueOf(propertyRepository.getPropertyValue(Property.PropertyName.RETRIES));
                boolean useHttpProxy = Boolean.valueOf(propertyRepository.getPropertyValue(Property.PropertyName.USE_HTTP_PROXY));
                Sender sender = useHttpProxy ? new HttpProxySender(serverApiKey) : new Sender(serverApiKey);
                multicastResult = sender.send(message, registrationIds, retries);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                return;
            }

            // analyze the results
            for (int i = 0; i < registrationIds.size(); i ++) {
                String regId = registrationIds.get(i);
                Result result = multicastResult.getResults().get(i);
                String messageId = result.getMessageId();

                if (messageId != null) {
                    LOGGER.info("Succesfully sent message to device: " + regId + "; messageId = " + messageId);
                    String canonicalRegId = result.getCanonicalRegistrationId();

                    if (canonicalRegId != null) {
                        // same device has more than on registration id: update it
                        LOGGER.info("canonicalRegId " + canonicalRegId);
                        deviceRegistrationRepository.updateRegistrationId(regId, canonicalRegId);
                    }
                } else {
                    String error = result.getErrorCodeName();

                    if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                        // application has been removed from device - unregister it
                        LOGGER.warn("Unregistered device: " + regId);
                        deviceRegistrationRepository.deleteDeviceRegistrationByRegistrationId(regId);
                    } else {
                        LOGGER.warn("Error sending message to " + regId + ": " + error);
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
