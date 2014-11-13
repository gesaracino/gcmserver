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
        return entityManager.createNamedQuery("DeviceRegistration.GetDeviceRegistrations", DeviceRegistration.class).getResultList();
	}

	public DeviceRegistration insertDeviceRegistration(DeviceRegistration deviceRegistration) {
		entityManager.persist(deviceRegistration);
        return new DeviceRegistration(deviceRegistration);
	}

	public DeviceRegistration updateDeviceRegistration(Long id, DeviceRegistration deviceRegistration) {
        DeviceRegistration retrieved = entityManager.
                createNamedQuery("DeviceRegistration.GetById", DeviceRegistration.class).
                setParameter("id", id).
                getSingleResult();
        retrieved.setRegistrationId(deviceRegistration.getRegistrationId());
        return new DeviceRegistration(entityManager.merge(retrieved));
	}

    public DeviceRegistration updateDeviceRegistrationByRegistrationId(String registrationId, DeviceRegistration deviceRegistration) {
        DeviceRegistration retrieved = entityManager.
                createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                setParameter("registrationId", registrationId).
                getSingleResult();
        return updateDeviceRegistration(retrieved.getId(), deviceRegistration);
    }

	public DeviceRegistration insertOrUpdateDeviceRegistration(DeviceRegistration deviceRegistration) {
        DeviceRegistration retrieved = null;

        try {
            retrieved = entityManager.
                    createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                    setParameter("registrationId", deviceRegistration.getRegistrationId()).
                    getSingleResult();
        } catch (NoResultException e) {
            return insertDeviceRegistration(deviceRegistration);
        }

		return updateDeviceRegistration(retrieved.getId(), deviceRegistration);
	}

	public DeviceRegistration getDeviceRegistration(Long id) {
		return entityManager.
                createNamedQuery("DeviceRegistration.GetById", DeviceRegistration.class).
                setParameter("id", id).
                getSingleResult();
	}

	public DeviceRegistration deleteDeviceRegistration(Long id) {
        DeviceRegistration retrieved = getDeviceRegistration(id);
        entityManager.detach(retrieved);
        return retrieved;
	}

    public DeviceRegistration deleteDeviceRegistrationByRegistrationId(String registrationId) {
        DeviceRegistration persistent = entityManager.
                createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                setParameter("registrationId", registrationId).
                getSingleResult();
        entityManager.detach(persistent);
        return persistent;
    }
}
