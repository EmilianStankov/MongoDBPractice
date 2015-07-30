package com.intracol.moviesmongodb.crud;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.intracol.moviesmongodb.models.Actor;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class DatabaseManipulator {
	private static final String LINE_BREAK = "*******************************";

	public static void addNewActor(DBCollection movies, DBCollection actors, Actor actor, int n) {
		System.out.println(LINE_BREAK);
		System.out.println("Adding a new Actor to the database");
		DBCursor movie;
		actors.save(actor);
		movie = movies.find().limit(n);
		while (movie.hasNext()) {
			movie.next();
			movies.update(movie.curr(), new BasicDBObject("$push", new BasicDBObject("actors",
					actors.findOne(new BasicDBObject("name", actor.getName())).get("_id"))));
		}
		movie = movies.find();
		System.out.println(cursorData(movie));
		System.out.println(LINE_BREAK);
	}

	public static void addNewActorToMovie(DBCollection movies, DBCollection actors, String actorName,
			String movieName) {
		DBCursor movie = movies.find(new BasicDBObject("name", movieName)).limit(1);
		DBCursor actor = actors.find(new BasicDBObject("name", actorName)).limit(1);
		while (movie.hasNext() && actor.hasNext()) {
			movies.update(movie.next(),
					new BasicDBObject("$push", new BasicDBObject("actors", actor.next().get("_id"))));
		}
	}

	private static String cursorData(DBCursor c) {
		String data = "";
		while (c.hasNext()) {
			data += c.next().toString() + "\n";
		}
		return data;
	}

	public static void moviesActorsStarIn(DBCollection movies, DBCollection actors, int n) {
		System.out.println(LINE_BREAK);
		System.out.println("Movies " + n + " of the actors star in:");
		DBCursor moviesStarringActor;
		DBCursor actor;
		actor = actors.find().limit(n);
		while (actor.hasNext()) {
			moviesStarringActor = movies.find(new BasicDBObject("actors",
					new BasicDBObject("$elemMatch", new BasicDBObject("$eq", actor.next().get("_id")))));
			while (moviesStarringActor.hasNext()) {
				System.out.println(moviesStarringActor.next().get("name").toString() + " "
						+ moviesStarringActor.curr().get("year").toString());
			}
		}
		System.out.println(LINE_BREAK);
	}

	public static String sortActorsStarringInMovies(DBCollection movies, DBCollection actors, int n) {
		System.out.println(LINE_BREAK);
		System.out.println("Sorting actors starring in movies");
		DBCursor movie;
		DBCursor actorsInMovie;
		String data = "";
		movie = movies.find().limit(n);
		while (movie.hasNext()) {
			actorsInMovie = actors.find(new BasicDBObject("_id", new BasicDBObject("$in", movie.next().get("actors"))));
			actorsInMovie.sort(new BasicDBObject("dateBirth", 1));
			data += cursorData(actorsInMovie) + "\n";
		}
		System.out.println(LINE_BREAK);
		return data;
	}

	public static String sortMovies(DBCollection movies, int n) {
		System.out.println(LINE_BREAK);
		System.out.println("Sorting movies by year");
		DBCursor movie = movies.find().limit(n);
		movie.sort(new BasicDBObject("year", 1));
		System.out.println(LINE_BREAK);
		return cursorData(movie);
	}

	public static void removeActors(DBCollection movies, DBCollection actors, String[] actorsNames) {
		System.out.println(LINE_BREAK);
		System.out.println("Removing actors");
		DBCursor movie;
		for (String a : actorsNames) {
			ObjectId actorId = (ObjectId) actors.findOne(new BasicDBObject("name", a)).get("_id");
			actors.remove(new BasicDBObject("name", a));
			movie = movies.find();
			while (movie.hasNext()) {
				movie.next();
				movies.update(movie.curr(), new BasicDBObject("$pull", new BasicDBObject("actors", actorId)));
			}
		}
		movie = movies.find();
		System.out.println(cursorData(movie));
		System.out.println(LINE_BREAK);
	}

	public static void insertIntoCollection(DBCollection collection, List<? extends BasicDBObject> list) {
		for (BasicDBObject a : list) {
			collection.save(a);
		}
	}

	public static List<String> getNames(DBCollection collection) {
		List<String> names = new ArrayList<String>();
		DBCursor c = collection.find();
		while (c.hasNext()) {
			names.add(c.next().get("name").toString());
		}
		return names;
	}

	public static DBObject getObject(String name, DBCollection collection) {
		return collection.findOne(new BasicDBObject("name", name));
	}
}
