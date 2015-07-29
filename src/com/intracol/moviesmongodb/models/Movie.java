package com.intracol.moviesmongodb.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;

public class Movie extends BasicDBObject {
	private static final String ACTOR_ID = "_id";
	private static final String YEAR = "year";
	private static final String NAME = "name";
	private static final String ACTORS = "actors";

	public Movie(String name, int year, List<Actor> actors) {
		setName(name);
		setYear(year);
		setActors(actors);
	}

	public void setName(String name) {
		put(NAME, name);
	}

	public String getName() {
		return getString(NAME);
	}

	public void setYear(int year) {
		put(YEAR, year);
	}

	public int getYear() {
		return getInt(YEAR);
	}

	public void setActors(List<Actor> actors) {
		List<ObjectId> ids = new ArrayList<ObjectId>();
		for (Actor actor : actors) {
			ids.add((ObjectId) actor.get(ACTOR_ID));
		}
		put(ACTORS, ids);
	}

	public ObjectId[] getActorsIds() {
		return (ObjectId[]) get("actors");
	}

}
