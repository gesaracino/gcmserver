package com.gesaracino.gcm.control;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import com.gesaracino.gcm.entity.DeviceRegistration;

@Stateless
public class DeviceRegistrationRepository {
	private static final Logger LOGGER = Logger.getLogger(DeviceRegistrationRepository.class);
	
	@PersistenceContext
    private EntityManager entityManager;

    public List<DeviceRegistration> getDeviceRegistrations() {
        return entityManager.createNamedQuery("DeviceRegistration.GetDeviceRegistrations", DeviceRegistration.class).getResultList();
	}

	public DeviceRegistration insertDeviceRegistration(DeviceRegistration deviceRegistration) {
		LOGGER.info("Persisting Device Registration: " + deviceRegistration);
		entityManager.persist(deviceRegistration);
		return new DeviceRegistration(deviceRegistration);
	}

	public DeviceRegistration updateDeviceRegistration(Long id, DeviceRegistration deviceRegistration) {
        DeviceRegistration retrieved = entityManager.
                createNamedQuery("DeviceRegistration.GetById", DeviceRegistration.class).
                setParameter("id", id).
                getSingleResult();
        return updateRegistrationId(retrieved.getRegistrationId(), deviceRegistration.getRegistrationId());
	}

	public DeviceRegistration insertDeviceRegistrationIfNotExists(DeviceRegistration deviceRegistration) {
        DeviceRegistration retrieved = null;

        try {
            retrieved = entityManager.
                    createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                    setParameter("registrationId", deviceRegistration.getRegistrationId()).
                    getSingleResult();
        } catch (NoResultException e) {
            return insertDeviceRegistration(deviceRegistration);
        }

        return retrieved;
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
        DeviceRegistration retrieved = entityManager.
                createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                setParameter("registrationId", registrationId).
                getSingleResult();
        entityManager.detach(retrieved);
        return retrieved;
    }

    public DeviceRegistration updateRegistrationId(String registrationId, String newRegistrationId) {
        DeviceRegistration retrieved = entityManager.
                createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                setParameter("registrationId", registrationId).
                getSingleResult();

        try {
            DeviceRegistration retrievedByNewRegistrationId = entityManager.
                    createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                    setParameter("registrationId", newRegistrationId).
                    getSingleResult();

            entityManager.detach(retrieved);
            return retrievedByNewRegistrationId;
        } catch(NoResultException e) {
            retrieved.setRegistrationId(newRegistrationId);
        }

        return new DeviceRegistration(entityManager.merge(retrieved));
    }
}
