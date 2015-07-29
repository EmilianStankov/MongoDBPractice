package com.intracol.moviesmongodb.models;

import java.util.Date;

import com.mongodb.BasicDBObject;

public class Actor extends BasicDBObject {
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String DATE_BIRTH = "dateBirth";

	public Actor(String name, String description, Date date) {
		setName(name);
		setDescription(description);
		setDateBirth(date);
	}

	public void setName(String name) {
		put(NAME, name);
	}

	public String getName() {
		return getString(NAME);
	}

	public void setDescription(String description) {
		put(DESCRIPTION, description);
	}

	public String getDescription() {
		return getString(DESCRIPTION);
	}

	public void setDateBirth(Date date) {
		put(DATE_BIRTH, date);
	}

	public Date getDateBirth() {
		return getDate(DATE_BIRTH);
	}
}
