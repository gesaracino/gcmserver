package com.gesaracino.gcm.control;

import com.gesaracino.gcm.entity.Property;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;

/**
 * Created by Gerardo Saracino on 11/11/2014.
 */

@Singleton
public class PropertyRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private HashMap<String, String> propertyCache = new HashMap<String, String>();

    public String getPropertyValue(String name) {
        String ret = propertyCache.get(name);

        if(ret == null) {
            ret = entityManager.
                    createQuery("select p.value from Property p where p.name=:name", String.class).
                    setParameter("name", name).
                    getSingleResult();
            propertyCache.put(name, ret);
        }

        return ret;
    }

    public String getPropertyValue(Property.PropertyName propertyName) {
        return getPropertyValue(propertyName.getValue());
    }
}
