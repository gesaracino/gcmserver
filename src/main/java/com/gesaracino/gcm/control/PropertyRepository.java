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

    private HashMap<String, Property> PropertyCache = new HashMap<String, Property>();

    public String getPropertyValue(String name) {
        Property ret = PropertyCache.get(name);

        if(ret == null) {
            ret = entityManager.
                    createQuery("select p from Property p where p.name=:name", Property.class).
                    setParameter("name", name).
                    getSingleResult();
            PropertyCache.put(name, ret);
        }

        return ret.getValue();
    }

    public String getPropertyValue(Property.PropertyName propertyName) {
        return getPropertyValue(propertyName.getValue());
    }
}
