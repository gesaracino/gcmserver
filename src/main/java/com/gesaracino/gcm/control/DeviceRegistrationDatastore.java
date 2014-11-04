package com.gesaracino.gcm.control;

import com.gesaracino.gcm.entity.DeviceRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DeviceRegistrationDatastore {
	private static DeviceRegistrationDatastore instance;

	private HashMap<String, DeviceRegistration> deviceRegistrationsByDeclaredDeviceId = new HashMap<String, DeviceRegistration>();
	private HashMap<Long, DeviceRegistration> deviceRegistrationsById = new HashMap<Long, DeviceRegistration>();
	
	public static DeviceRegistrationDatastore getInstance() {
		if (instance == null) {
			instance = new DeviceRegistrationDatastore();
		}

		return instance;
	}

	public List<DeviceRegistration> getDeviceRegistrations() {
		return new ArrayList<DeviceRegistration>(deviceRegistrationsByDeclaredDeviceId.values());
	}

	public DeviceRegistration insertDeviceRegistration(DeviceRegistration deviceRegistration) {
		if(deviceRegistrationsByDeclaredDeviceId.containsKey(deviceRegistration.getDeclaredDeviceId())) {
			throw new IllegalArgumentException("Device Registration already exists");
		}
		
		deviceRegistration.setId(generateDeviceId());
		deviceRegistrationsByDeclaredDeviceId.put(deviceRegistration.getDeclaredDeviceId(), deviceRegistration);
		deviceRegistrationsById.put(deviceRegistration.getId(), deviceRegistration);
		return deviceRegistration;
	}

	public DeviceRegistration updateDeviceRegistration(Long id, DeviceRegistration deviceRegistration) {
		DeviceRegistration persistentDeviceRegistration = deviceRegistrationsById.get(id);

        if(persistentDeviceRegistration == null) {
            throw new IllegalArgumentException("Device Registration doesn't exist");
        }

		persistentDeviceRegistration.setGcmRegistrationId(deviceRegistration.getGcmRegistrationId());
		return persistentDeviceRegistration;
	}
	
	public DeviceRegistration insertOrUpdateDeviceRegistration(DeviceRegistration deviceRegistration) {
		DeviceRegistration registeredDeviceRegistration = deviceRegistrationsByDeclaredDeviceId.get(deviceRegistration.getDeclaredDeviceId());
		
		if(registeredDeviceRegistration == null) {
			return insertDeviceRegistration(deviceRegistration);
		}
		
		return updateDeviceRegistration(registeredDeviceRegistration.getId(), deviceRegistration);
	}

	public DeviceRegistration getRegisteredDevice(Long id) {
		return deviceRegistrationsById.get(id);
	}

	public DeviceRegistration deleteDeviceRegistration(Long id) {
		DeviceRegistration removed = deviceRegistrationsById.remove(id);
		return removed != null ? deviceRegistrationsByDeclaredDeviceId.remove(removed.getDeclaredDeviceId()) : null;
	}
	
	private Long generateDeviceId() {
		return deviceRegistrationsById.isEmpty() ? 1L : Collections.max(deviceRegistrationsById.keySet()) + 1;
	}
}
