package com.gesaracino.gcm.boundary;

import com.gesaracino.gcm.control.DeviceRegistrationRepository;
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
@Produces(MediaType.APPLICATION_JSON)
public class DeviceRegistrations {
    @EJB
	private DeviceRegistrationRepository datastore;

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
    @Consumes(MediaType.APPLICATION_JSON)
    @Interceptors({MethodParameterValidator.class})
    public DeviceRegistration insertDeviceRegistration(@Valid DeviceRegistration deviceRegistration) {
		return datastore.insertOrUpdateDeviceRegistration(deviceRegistration);
	}
	
	@PUT
	@Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
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
