package com.gesaracino.gcm.boundary;

import com.gesaracino.gcm.control.MessageSender;
import com.gesaracino.gcm.entity.NotificationMessage;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * Created by Gerardo Saracino on 06/11/2014.
 */

@Path("/notificationMessages")
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class NotificationMessages {
    @EJB
    private MessageSender messageSender;

    @POST
    @Interceptors({MethodParameterValidator.class})
    public void sendNotificationMessage(@Valid NotificationMessage notificationMessage) {
        messageSender.send(notificationMessage.getDeviceRegistrationIds(), notificationMessage.getText());
    }
}
