package com.prototype.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prototype.model.Address;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public abstract class AddressEvent extends GlobalEvent
{
	@DBRef(lazy = true)
	private Address address;
	private boolean personal;

	public AddressEvent(LocalDateTime dateEvent, Address address) {
		super(dateEvent);
		this.address = address;
		personal = true;
	}

	public boolean isPersonal() {
		return personal;
	}

	public void setPersonal(boolean personal) {
		this.personal = personal;
	}

	@JsonIgnore
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
