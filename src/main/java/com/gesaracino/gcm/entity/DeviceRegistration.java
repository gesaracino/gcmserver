package com.gesaracino.gcm.entity;

import javax.validation.constraints.NotNull;

public class DeviceRegistration {
	private Long id;

	@NotNull
	private String declaredDeviceId;

	@NotNull
	private String gcmRegistrationId;

	public DeviceRegistration() {
		super();
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

	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}

    @Override
	public String toString() {
		return "Device [id=" + id + ", declaredDeviceId=" + declaredDeviceId
				+ ", gcmRegistrationId=" + gcmRegistrationId + "]";
	}
}
