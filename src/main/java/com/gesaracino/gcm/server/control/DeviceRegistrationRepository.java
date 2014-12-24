package com.gesaracino.gcm.server.control;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.gesaracino.gcm.server.entity.DeviceRegistration;

@Stateless
public class DeviceRegistrationRepository {
	@PersistenceContext
    private EntityManager entityManager;

    public List<DeviceRegistration> getDeviceRegistrations(String declaredDeviceId, String registrationId) {
    	TypedQuery<DeviceRegistration> query = entityManager.createNamedQuery("DeviceRegistration.GetAll", DeviceRegistration.class);
    	
    	if(declaredDeviceId == null && registrationId == null) {
    		query = entityManager.createNamedQuery("DeviceRegistration.GetAll", DeviceRegistration.class);
    	} else if(declaredDeviceId != null && registrationId == null) {
    		query = entityManager.
    				createNamedQuery("DeviceRegistration.GetByDeclaredDeviceId", DeviceRegistration.class).
    				setParameter("declaredDeviceId", declaredDeviceId);
    	} else if(declaredDeviceId == null && registrationId != null) {
    		query = entityManager.
    				createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
    				setParameter("registrationId", registrationId);
    	} else {
    		query = entityManager.createNamedQuery("DeviceRegistration.GetByDeclaredDeviceIdAndRegistrationId", DeviceRegistration.class).
    				setParameter("declaredDeviceId", declaredDeviceId).
    				setParameter("registrationId", registrationId);
    	}
    	
        List<DeviceRegistration> result = query.getResultList();
        ArrayList<DeviceRegistration> ret = new ArrayList<DeviceRegistration>(result.size());
        for(DeviceRegistration registration : result) {
            ret.add(new DeviceRegistration(registration));
        }
        return ret;
    }
	
    public DeviceRegistration getDeviceRegistration(Long id) {
        return new DeviceRegistration(entityManager.
                createNamedQuery("DeviceRegistration.GetById", DeviceRegistration.class).
                setParameter("id", id).
                getSingleResult());
    }

    public DeviceRegistration insertDeviceRegistrationIfNotExists(DeviceRegistration deviceRegistration) {
        try {
            return new DeviceRegistration(entityManager.
                    createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                    setParameter("registrationId", deviceRegistration.getRegistrationId()).
                    getSingleResult());
        } catch (NoResultException e) {
            return insertDeviceRegistration(deviceRegistration);
        }
    }

	public DeviceRegistration insertDeviceRegistration(DeviceRegistration deviceRegistration) {
		entityManager.persist(deviceRegistration);
		return new DeviceRegistration(deviceRegistration);
	}

	public DeviceRegistration updateDeviceRegistration(Long id, DeviceRegistration deviceRegistration) {
        DeviceRegistration persistent = entityManager.
                createNamedQuery("DeviceRegistration.GetById", DeviceRegistration.class).
                setParameter("id", id).
                getSingleResult();

        return makeUpdateRegistrationId(persistent, deviceRegistration.getRegistrationId());
	}

    public DeviceRegistration updateRegistrationId(String registrationId, String newRegistrationId) {
        DeviceRegistration persistent = entityManager.
                createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                setParameter("registrationId", registrationId).
                getSingleResult();

        return makeUpdateRegistrationId(persistent, newRegistrationId);
    }

	public DeviceRegistration deleteDeviceRegistration(Long id) {
        DeviceRegistration persistent = entityManager.
                createNamedQuery("DeviceRegistration.GetById", DeviceRegistration.class).
                setParameter("id", id).
                getSingleResult();
        entityManager.remove(persistent);
        return persistent;
	}

    public DeviceRegistration deleteDeviceRegistrationByRegistrationId(String registrationId) {
        DeviceRegistration retrieved = entityManager.
                createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                setParameter("registrationId", registrationId).
                getSingleResult();
        entityManager.remove(retrieved);
        return retrieved;
    }

    private DeviceRegistration makeUpdateRegistrationId(DeviceRegistration persistent, String newRegistrationId) {
        if(persistent.getRegistrationId().equals(newRegistrationId)) {
            return new DeviceRegistration(persistent);
        }

        try {
            DeviceRegistration persistentByNewRegistrationId = entityManager.
                    createNamedQuery("DeviceRegistration.GetByRegistrationId", DeviceRegistration.class).
                    setParameter("registrationId", newRegistrationId).
                    getSingleResult();

            entityManager.remove(persistent);
            return persistentByNewRegistrationId;
        } catch(NoResultException e) {
            persistent.setRegistrationId(newRegistrationId);
        }

        return new DeviceRegistration(persistent);
    }
}
