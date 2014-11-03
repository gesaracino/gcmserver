package com.gesaracino.gcm.boundary;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gesaracino.gcm.control.DeviceRegistrationDatastore;
import com.gesaracino.gcm.entity.DeviceRegistration;
import org.jboss.resteasy.spi.validation.ValidateRequest;

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
		return datastore.deleteRegisteredDevice(id);
	}
}
