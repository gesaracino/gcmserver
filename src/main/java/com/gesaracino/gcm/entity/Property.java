package com.gesaracino.gcm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Gerardo Saracino on 11/11/2014.
 */

@Entity
@Table(name = "PROPERTIES")
public class Property {
    public static enum PropertyName {
        SERVER_API_KEY("server.api.key"),
        HTTP_PROXY_HOSTNAME("http.proxy.hostname"),
        HTTP_PROXY_PORT("http.proxy.port"),
        RETRIES("retries"),
        MULTICAST_SIZE("multicast.size"),
        USE_HTTP_PROXY("use.http.proxy"),
        DATA_MESSAGE_KEY("data.message.key"),
        THREADS("threads");

        private String value;

        PropertyName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Id
    @Column(name = "NAME")
    @NotNull
    @Size(min = 1)
    private String name;

    @Column(name = "VALUE")
    @NotNull
    @Size(min = 1)
    private String value;

    public Property() {
        super();
    }

    public Property(Property property) {
        super();
        name = property.getName();
        value = property.getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
