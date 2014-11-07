package com.gesaracino.gcm.boundary;

import com.gesaracino.gcm.control.DeviceRegistrationDatastore;
import com.gesaracino.gcm.entity.DeviceRegistration;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/deviceRegistrations")
@Stateless
public class DeviceRegistrations {
    @EJB
	private DeviceRegistrationDatastore datastore;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<DeviceRegistration> getDeviceRegistrations() {
		return datastore.getDeviceRegistrations();
	}
	
	@GET
	@Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public DeviceRegistration getDeviceRegistration(@PathParam(value = "id") Long id) {
		return datastore.getDeviceRegistration(id);
	}
	
	@POST
    @Interceptors({MethodParameterValidator.class})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DeviceRegistration insertDeviceRegistration(@Valid DeviceRegistration deviceRegistration) {
		return datastore.insertOrUpdateDeviceRegistration(deviceRegistration);
	}
	
	@PUT
	@Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({MethodParameterValidator.class})
    public DeviceRegistration updateDeviceRegistration(@PathParam(value = "id") Long id, @Valid DeviceRegistration deviceRegistration) {
		return datastore.updateDeviceRegistration(id, deviceRegistration);
	}
	
	@DELETE
	@Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public DeviceRegistration deleteDeviceRegistration(@PathParam(value = "id") Long id) {
		return datastore.deleteDeviceRegistration(id);
	}
}
