package com.gesaracino.gcm.boundary;

import com.gesaracino.gcm.control.MessageSender;
import com.gesaracino.gcm.entity.Message;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Gerardo Saracino on 06/11/2014.
 */

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class Messages {
    @EJB
    private MessageSender messageSender;

    @POST
    @Interceptors({MethodParameterValidator.class})
    public void insertDeviceRegistration(@Valid Message message) {
        messageSender.send(message.getDeviceRegistrationId(), message.getText());
    }
}
