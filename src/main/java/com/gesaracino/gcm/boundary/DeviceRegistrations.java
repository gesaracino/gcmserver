package com.gesaracino.gcm.boundary;

import com.gesaracino.gcm.control.DeviceRegistrationDatastore;
import com.gesaracino.gcm.entity.DeviceRegistration;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/deviceRegistrations")
@Produces(MediaType.APPLICATION_JSON)
@ValidateRequest
public class DeviceRegistrations {
	private DeviceRegistrationDatastore datastore = DeviceRegistrationDatastore.getInstance();
	
	@GET
	public List<DeviceRegistration> getDeviceRegistrations() {
		return datastore.getDeviceRegistrations();
	}
	
	@GET
	@Path("{id}")
	public DeviceRegistration getDeviceRegistration(@PathParam(value = "id") Long id) {
		return datastore.getRegisteredDevice(id);
	}
	
	@POST
	public DeviceRegistration insertDeviceRegistration(@Valid DeviceRegistration deviceRegistration) {
		return datastore.insertOrUpdateDeviceRegistration(deviceRegistration);
	}
	
	@PUT
	@Path("{id}")
	public DeviceRegistration updateDeviceRegistration(@PathParam(value = "id") Long id, @Valid DeviceRegistration deviceRegistration) {
		return datastore.updateDeviceRegistration(id, deviceRegistration);
	}
	
	@DELETE
	@Path("{id}")
	public DeviceRegistration deleteDeviceRegistration(@PathParam(value = "id") Long id) {
		return datastore.deleteDeviceRegistration(id);
	}
}
