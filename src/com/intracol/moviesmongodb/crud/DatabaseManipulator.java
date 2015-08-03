package com.intracol.moviesmongodb.crud;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.intracol.moviesmongodb.generators.ActorInitializer;
import com.intracol.moviesmongodb.generators.MovieInitializer;
import com.intracol.moviesmongodb.models.Actor;
import com.intracol.moviesmongodb.models.Movie;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class DatabaseManipulator {
	private static final String LINE_BREAK = "*******************************";

	private static DBCollection movies;
	private static DBCollection actors;
	private static DB db;

	private static void initDB() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		db = mongoClient.getDB("movies");
		movies = db.getCollection("movies");
		actors = db.getCollection("actors");
		movies.createIndex(new BasicDBObject("name", 1).append("year", 1));
		actors.createIndex(new BasicDBObject("name", 1).append("dateBirth", 1));
	}

	public static void addNewActor(Actor actor) throws UnknownHostException {
		initDB();
		System.out.println(LINE_BREAK);
		System.out.println("Adding a new Actor to the database");
		actors.save(actor);
		System.out.println(LINE_BREAK);
	}

	public static void addNewMovie(Movie movie) throws UnknownHostException {
		initDB();
		System.out.println(LINE_BREAK);
		System.out.println("Adding a new Movie to the database");
		movies.save(movie);
		System.out.println(LINE_BREAK);
	}

	public static boolean addNewActorToMovie(String actorName, String movieName) throws UnknownHostException {
		initDB();
		DBCursor movie = movies.find(new BasicDBObject("name", movieName)).limit(1);
		DBCursor actor = actors.find(new BasicDBObject("name", actorName)).limit(1);
		boolean result = false;
		while (movie.hasNext() && actor.hasNext()) {
			result = true;
			movies.update(movie.next(),
					new BasicDBObject("$push", new BasicDBObject("actors", actor.next().get("_id"))));
		}
		return result;
	}

	private static String movieData(DBCursor movie) {
		String data = "";
		while (movie.hasNext()) {
			data += String.format("%s, %d\n", movie.next().get("name"), movie.curr().get("year"));
		}
		return data;
	}

	private static String actorData(DBCursor actor) {
		String data = "";
		while (actor.hasNext()) {
			data += String.format("%s, born on %s\n", actor.next().get("name"), actor.curr().get("dateBirth"));
		}
		return data;
	}

	public static String moviesActorStarsIn(String actorName) throws UnknownHostException {
		initDB();
		System.out.println(LINE_BREAK);
		System.out.println("Movies " + actorName + " stars in:");
		DBCursor moviesStarringActor;
		DBCursor actor = actors.find(new BasicDBObject("name", actorName)).limit(1);
		String result = "";
		while (actor.hasNext()) {
			moviesStarringActor = movies.find(new BasicDBObject("actors",
					new BasicDBObject("$elemMatch", new BasicDBObject("$eq", actor.next().get("_id")))));
			moviesStarringActor.sort(new BasicDBObject("year", 1));
			while (moviesStarringActor.hasNext()) {
				result += moviesStarringActor.next().get("name").toString() + " "
						+ moviesStarringActor.curr().get("year").toString() + "\n";
			}
		}
		System.out.println(LINE_BREAK);
		return result;
	}

	public static String sortActorsStarringInMovie(String movieName, String p) throws UnknownHostException {
		initDB();
		System.out.println(LINE_BREAK);
		System.out.println("Sorting actors starring in movies");
		DBCursor movie;
		DBCursor actorsInMovie;
		String data = "";
		movie = movies.find(new BasicDBObject("name", movieName)).limit(1);
		while (movie.hasNext()) {
			actorsInMovie = actors.find(new BasicDBObject("_id", new BasicDBObject("$in", movie.next().get("actors"))));
			actorsInMovie.sort(new BasicDBObject(p, 1));
			data += actorData(actorsInMovie) + "\n";
		}
		System.out.println(LINE_BREAK);
		return data;
	}

	public static String sortMovies(int n) throws UnknownHostException {
		initDB();
		System.out.println(LINE_BREAK);
		System.out.println("Sorting movies by year");
		DBCursor movie = movies.find().limit(n);
		System.out.println(LINE_BREAK);
		return movieData(movie.sort(new BasicDBObject("year", -1)));
	}

	public static boolean removeActors(String actorName) throws UnknownHostException {
		initDB();
		System.out.println(LINE_BREAK);
		System.out.println("Removing actors");
		DBCursor movie;
		boolean removed = false;
		if (actors.find(new BasicDBObject("name", actorName)).hasNext()) {
			removed = true;
			ObjectId actorId = (ObjectId) actors.findOne(new BasicDBObject("name", actorName)).get("_id");
			actors.remove(new BasicDBObject("name", actorName));
			movie = movies.find();
			while (movie.hasNext()) {
				movie.next();
				movies.update(movie.curr(), new BasicDBObject("$pull", new BasicDBObject("actors", actorId)));
			}
		}
		movie = movies.find();
		System.out.println(movieData(movie));
		System.out.println(LINE_BREAK);
		return removed;
	}

	public static boolean removeMovie(String movieName) throws UnknownHostException {
		initDB();
		System.out.println(LINE_BREAK);
		System.out.println("Deleting movie");
		if (movies.find(new BasicDBObject("name", movieName)).hasNext()) {
			movies.remove(new BasicDBObject("name", movieName));
			return true;
		}
		return false;
	}

	private static void insertIntoCollection(DBCollection collection, List<? extends BasicDBObject> list) {
		for (BasicDBObject a : list) {
			collection.save(a);
		}
	}

	private static List<String> getNames(DBCollection collection) {
		List<String> names = new ArrayList<String>();
		DBCursor c = collection.find();
		while (c.hasNext()) {
			names.add(c.next().get("name").toString());
		}
		return names;
	}

	public static List<String> getActorNames() {
		return getNames(actors);
	}

	public static List<String> getMovieNames() {
		return getNames(movies);
	}

	private static DBObject getObject(String name, DBCollection collection) {
		return collection.findOne(new BasicDBObject("name", name));
	}

	public static DBObject getActor(String name) {
		return getObject(name, actors);
	}

	public static DBObject getMovie(String name) {
		return getObject(name, movies);
	}

	public static void generate() throws UnknownHostException {
		initDB();
		List<Actor> actorsList = ActorInitializer.initializeActors(7);
		DatabaseManipulator.insertIntoCollection(actors, actorsList);
		List<Movie> moviesList = MovieInitializer.initializeMovies(10, actorsList);
		DatabaseManipulator.insertIntoCollection(movies, moviesList);
	}

	public static void delete() throws UnknownHostException {
		initDB();
		db.dropDatabase();
	}
}
