package com.gesaracino.gcm.entity;

import javax.validation.constraints.NotNull;

public class DeviceRegistration {
	private Long id;

	@NotNull
	private String registrationId;

	public DeviceRegistration() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

    @Override
	public String toString() {
		return "Device [id=" + id + ", registrationId=" + registrationId + "]";
	}
}
