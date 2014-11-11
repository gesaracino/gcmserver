package com.gesaracino.gcm.control;

import com.gesaracino.gcm.entity.DeviceRegistration;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;

@Stateless
public class DeviceRegistrationRepository {
	private HashMap<String, DeviceRegistration> deviceRegistrationsByRegistrationId = new HashMap<String, DeviceRegistration>();
	private HashMap<Long, DeviceRegistration> deviceRegistrationsById = new HashMap<Long, DeviceRegistration>();

    @PersistenceContext
    private EntityManager entityManager;

    public List<DeviceRegistration> getDeviceRegistrations() {
		return entityManager.createQuery("select d from DeviceRegistration d", DeviceRegistration.class).getResultList();
	}

	public DeviceRegistration insertDeviceRegistration(DeviceRegistration deviceRegistration) {
		entityManager.persist(deviceRegistration);
        return deviceRegistration;
	}

	public DeviceRegistration updateDeviceRegistration(Long id, DeviceRegistration deviceRegistration) {
        DeviceRegistration persistent = entityManager.
                createQuery("select d from DeviceRegistration d where d.id=:id", DeviceRegistration.class).
                setParameter("id", id).
                getSingleResult();
        persistent.setRegistrationId(deviceRegistration.getRegistrationId());
        return persistent;
	}

    public DeviceRegistration updateDeviceRegistrationByRegistrationId(String registrationId, DeviceRegistration deviceRegistration) {
        DeviceRegistration persistent = entityManager.
                createQuery("select d from DeviceRegistration d where d.registrationId=:registrationId", DeviceRegistration.class).
                setParameter("registrationId", registrationId).
                getSingleResult();
        return updateDeviceRegistration(persistent.getId(), deviceRegistration);
    }

	public DeviceRegistration insertOrUpdateDeviceRegistration(DeviceRegistration deviceRegistration) {
        DeviceRegistration persistent = null;

        try {
            persistent = entityManager.
                    createQuery("select d from DeviceRegistration d where d.registrationId=:registrationId", DeviceRegistration.class).
                    setParameter("registrationId", deviceRegistration.getRegistrationId()).
                    getSingleResult();
        } catch (NoResultException e) {
            return insertDeviceRegistration(deviceRegistration);
        }

		return updateDeviceRegistration(persistent.getId(), deviceRegistration);
	}

	public DeviceRegistration getDeviceRegistration(Long id) {
		return entityManager.
                createQuery("select d from DeviceRegistration d where d.id=:id", DeviceRegistration.class).
                setParameter("id", id).
                getSingleResult();
	}

	public DeviceRegistration deleteDeviceRegistration(Long id) {
        DeviceRegistration persistent = getDeviceRegistration(id);
        entityManager.detach(persistent);
        return persistent;
	}

    public DeviceRegistration deleteDeviceRegistrationByRegistrationId(String registrationId) {
        DeviceRegistration persistent = entityManager.
                createQuery("select d from DeviceRegistration d where d.registrationId=:registrationId", DeviceRegistration.class).
                setParameter("registrationId", registrationId).
                getSingleResult();
        entityManager.detach(persistent);
        return persistent;
    }
}
