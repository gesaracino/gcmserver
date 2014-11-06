package com.gesaracino.gcm.control;

import com.gesaracino.gcm.entity.DeviceRegistration;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Singleton
public class DeviceRegistrationDatastore {
	private HashMap<String, DeviceRegistration> deviceRegistrationsByRegistrationId = new HashMap<String, DeviceRegistration>();
	private HashMap<Long, DeviceRegistration> deviceRegistrationsById = new HashMap<Long, DeviceRegistration>();

    public List<DeviceRegistration> getDeviceRegistrations() {
		return new ArrayList<DeviceRegistration>(deviceRegistrationsByRegistrationId.values());
	}

	public DeviceRegistration insertDeviceRegistration(DeviceRegistration deviceRegistration) {
		if(deviceRegistrationsByRegistrationId.containsKey(deviceRegistration.getRegistrationId())) {
			throw new IllegalArgumentException("Device Registration already exists");
		}
		
		deviceRegistration.setId(generateDeviceId());
		deviceRegistrationsByRegistrationId.put(deviceRegistration.getRegistrationId(), deviceRegistration);
		deviceRegistrationsById.put(deviceRegistration.getId(), deviceRegistration);
		return deviceRegistration;
	}

	public DeviceRegistration updateDeviceRegistration(Long id, DeviceRegistration deviceRegistration) {
        DeviceRegistration persistentDeviceRegistration = deviceRegistrationsById.get(id);
        return makeUpdateDeviceRegistration(persistentDeviceRegistration.getId(), deviceRegistration);
	}

    public DeviceRegistration updateDeviceRegistrationByRegistrationId(String registrationId, DeviceRegistration deviceRegistration) {
        DeviceRegistration persistentDeviceRegistration = deviceRegistrationsByRegistrationId.get(registrationId);
        return makeUpdateDeviceRegistration(persistentDeviceRegistration.getId(), deviceRegistration);
    }
	
	public DeviceRegistration insertOrUpdateDeviceRegistration(DeviceRegistration deviceRegistration) {
		DeviceRegistration registeredDeviceRegistration = deviceRegistrationsByRegistrationId.get(deviceRegistration.getRegistrationId());
		
		if(registeredDeviceRegistration == null) {
			return insertDeviceRegistration(deviceRegistration);
		}
		
		return updateDeviceRegistration(registeredDeviceRegistration.getId(), deviceRegistration);
	}

	public DeviceRegistration getDeviceRegistration(Long id) {
		return deviceRegistrationsById.get(id);
	}

	public DeviceRegistration deleteDeviceRegistration(Long id) {
		DeviceRegistration removed = deviceRegistrationsById.remove(id);
		return removed != null ? deviceRegistrationsByRegistrationId.remove(removed.getRegistrationId()) : null;
	}

    public DeviceRegistration deleteDeviceRegistrationByRegistrationId(String registrationId) {
        DeviceRegistration removed = deviceRegistrationsByRegistrationId.remove(registrationId);
        return removed != null ? deviceRegistrationsById.remove(removed.getId()) : null;
    }
	
	private Long generateDeviceId() {
		return deviceRegistrationsById.isEmpty() ? 1L : Collections.max(deviceRegistrationsById.keySet()) + 1;
	}

    private DeviceRegistration makeUpdateDeviceRegistration(Long id, DeviceRegistration deviceRegistration) {
        DeviceRegistration persistentDeviceRegistration = deviceRegistrationsById.get(id);

        if(persistentDeviceRegistration == null) {
            throw new IllegalArgumentException("Device Registration doesn't exist");
        }

        persistentDeviceRegistration.setRegistrationId(deviceRegistration.getRegistrationId());
        return persistentDeviceRegistration;
    }
}
