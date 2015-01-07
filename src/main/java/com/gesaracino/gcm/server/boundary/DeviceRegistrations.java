package com.gesaracino.gcm.server.boundary;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gesaracino.gcm.server.control.DeviceRegistrationRepository;
import com.gesaracino.gcm.server.entity.DeviceRegistration;

@Stateless
@Path("/deviceRegistrations")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceRegistrations {
    @EJB
	private DeviceRegistrationRepository datastore;

    @GET
	public List<DeviceRegistration> getDeviceRegistrations() {
    	return datastore.getDeviceRegistrations();
	}
    
    @GET
    @Path("byRegistrationId/{registrationId}")
	public DeviceRegistration getDeviceRegistrationByRegistrationId(@PathParam(value = "registrationId") String registrationId) {
    	return datastore.getDeviceRegistrationByRegistrationId(registrationId);
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
		return datastore.insertDeviceRegistrationIfNotExists(deviceRegistration);
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
