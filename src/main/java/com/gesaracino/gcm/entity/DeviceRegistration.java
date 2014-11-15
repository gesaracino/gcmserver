package com.gesaracino.gcm.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "DEVICE_REGISTRATIONS")
@NamedQueries({
        @NamedQuery(name = "DeviceRegistration.GetDeviceRegistrations", query = "select new com.gesaracino.gcm.entity.DeviceRegistration(d) from DeviceRegistration d"),
        @NamedQuery(name = "DeviceRegistration.GetById", query = "select new com.gesaracino.gcm.entity.DeviceRegistration(d) from DeviceRegistration d where d.id=:id"),
        @NamedQuery(name = "DeviceRegistration.GetByRegistrationId", query = "select new com.gesaracino.gcm.entity.DeviceRegistration(d) from DeviceRegistration d where d.registrationId=:registrationId")
})
public class DeviceRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqDeviceRegistrationIdGen")
    @SequenceGenerator(name = "SeqDeviceRegistrationIdGen", sequenceName = "SEQ_DEVICE_REGISTRATION_ID", allocationSize = 1, initialValue = 1)
    @Column(name = "ID")
	private Long id;

    @NotNull
    @Column(name = "DECLARED_DEVICE_ID")
    private String declaredDeviceId;

	@NotNull
    @Column(name = "REGISTRATION_ID", unique = true)
	private String registrationId;

	public DeviceRegistration() {
		super();
	}

    public DeviceRegistration(DeviceRegistration deviceRegistration) {
        super();
        id = deviceRegistration.getId();
        declaredDeviceId = deviceRegistration.getDeclaredDeviceId();
        registrationId = deviceRegistration.getRegistrationId();
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public String getDeclaredDeviceId() {
        return declaredDeviceId;
    }

    public void setDeclaredDeviceId(String declaredDeviceId) {
        this.declaredDeviceId = declaredDeviceId;
    }

    public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

    @Override
    public String toString() {
        return "DeviceRegistration{" +
                "id=" + id +
                ", declaredDeviceId='" + declaredDeviceId + '\'' +
                ", registrationId='" + registrationId + '\'' +
                '}';
    }
}
