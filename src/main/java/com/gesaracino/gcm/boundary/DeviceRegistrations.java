package com.gesaracino.gcm.boundary;

import com.gesaracino.gcm.control.DeviceRegistrationDatastore;
import com.gesaracino.gcm.control.MessageSender;
import com.gesaracino.gcm.entity.DeviceRegistration;
import com.gesaracino.gcm.entity.Message;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/deviceRegistrations")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class DeviceRegistrations {
    @EJB
	private DeviceRegistrationDatastore datastore;

    @GET
	public List<DeviceRegistration> getDeviceRegistrations() {
		return datastore.getDeviceRegistrations();
	}
	
	@GET
	@Path("{id}")
	public DeviceRegistration getDeviceRegistration(@PathParam(value = "id") Long id) {
		return datastore.getDeviceRegistration(id);
	}
	
	@POST
    @Interceptors({MethodParameterValidator.class})
    public DeviceRegistration insertDeviceRegistration(@Valid DeviceRegistration deviceRegistration) {
		return datastore.insertOrUpdateDeviceRegistration(deviceRegistration);
	}
	
	@PUT
	@Path("{id}")
    @Interceptors({MethodParameterValidator.class})
    public DeviceRegistration updateDeviceRegistration(@PathParam(value = "id") Long id, @Valid DeviceRegistration deviceRegistration) {
		return datastore.updateDeviceRegistration(id, deviceRegistration);
	}
	
	@DELETE
	@Path("{id}")
	public DeviceRegistration deleteDeviceRegistration(@PathParam(value = "id") Long id) {
		return datastore.deleteDeviceRegistration(id);
	}
}
