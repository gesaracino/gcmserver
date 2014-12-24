package com.gesaracino.gcm.server.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "DEVICE_REGISTRATIONS")
@NamedQueries({
		@NamedQuery(name = "DeviceRegistration.GetAll", query = "select d from DeviceRegistration d"),
		@NamedQuery(name = "DeviceRegistration.GetByDeclaredDeviceId", query = "select d from DeviceRegistration d where d.declaredDeviceId=:declaredDeviceId"),
		@NamedQuery(name = "DeviceRegistration.GetByRegistrationId", query = "select d from DeviceRegistration d where d.registrationId=:registrationId"),
		@NamedQuery(name = "DeviceRegistration.GetByDeclaredDeviceIdAndRegistrationId", query = "select d from DeviceRegistration d where d.declaredDeviceId=:declaredDeviceId and d.registrationId=:registrationId"),
		@NamedQuery(name = "DeviceRegistration.GetById", query = "select d from DeviceRegistration d where d.id=:id") })
public class DeviceRegistration {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqDeviceRegistrationIdGen")
	@SequenceGenerator(name = "SeqDeviceRegistrationIdGen", sequenceName = "SEQ_DEVICE_REGISTRATION_ID", allocationSize = 1, initialValue = 1)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@Column(name = "DECLARED_DEVICE_ID", nullable = false)
	private String declaredDeviceId;

	@NotNull
	@Column(name = "REGISTRATION_ID", nullable = false, unique = true)
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
		return "DeviceRegistration{" + "id=" + id + ", declaredDeviceId='"
				+ declaredDeviceId + '\'' + ", registrationId='"
				+ registrationId + '\'' + '}';
	}
}
