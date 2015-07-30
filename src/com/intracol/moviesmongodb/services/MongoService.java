package com.intracol.moviesmongodb.services;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.intracol.moviesmongodb.crud.DatabaseManipulator;
import com.intracol.moviesmongodb.models.Actor;
import com.intracol.moviesmongodb.models.Movie;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/mongo")
public class MongoService {

	// This method is called if TEXT_PLAIN is request
	@GET
	@Path("/generate")
	@Produces(MediaType.TEXT_PLAIN)
	public String generate() throws UnknownHostException {
		DatabaseManipulator.generate();
		return "Data generated!";
	}

	@GET
	@Path("/sort{n}")
	@Produces(MediaType.TEXT_PLAIN)
	public String sortMoviesByYear(@PathParam("n") int n) throws UnknownHostException {
		return DatabaseManipulator.sortMovies(n);
	}

	@POST
	@Path("/createactor")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String newActor(@FormParam("name") String name, @FormParam("description") String description,
			@FormParam("date") String dateBirth) throws UnknownHostException, ParseException {
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
		Date date = format.parse(dateBirth);
		DatabaseManipulator.addNewActor(new Actor(name, description, date));
		return "Actor created successfully";
	}

	@POST
	@Path("/createmovie")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String newMovie(@FormParam("name") String name, @FormParam("year") int year) throws UnknownHostException {
		DatabaseManipulator.addNewMovie(new Movie(name, year, new ArrayList<Actor>()));
		return "Movie created successfully";
	}

	@POST
	@Path("/addactor")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addActor(@FormParam("actorName") String actorName, @FormParam("movieName") String movieName)
			throws UnknownHostException, ParseException {
		if (DatabaseManipulator.addNewActorToMovie(actorName, movieName)) {
			return "Actor added successfully";
		} else {
			return "Movie or actor doesn't exist in database!";
		}
	}

	@GET
	@Path("/sort{n}actors")
	public String sortActors(@PathParam("n") int n) throws UnknownHostException {
		return DatabaseManipulator.sortActorsStarringInMovies(n).replaceAll("\n", "<br>");
	}

	@GET
	@Path("/deletedb")
	public String deleteDB() throws UnknownHostException {
		DatabaseManipulator.delete();
		return "Deleted database";
	}

	@POST
	@Path("/deleteactor")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String deleteActor(@FormParam("name") String name) throws UnknownHostException {
		DatabaseManipulator.removeActors(new String[] { name });
		return "Actor deleted successfully";
	}

	@POST
	@Path("/deletemovie")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String deleteMovie(@FormParam("name") String name) throws UnknownHostException {
		DatabaseManipulator.removeMovie(name);
		return "Movie deleted successfully";
	}

	@GET
	@Path("/starring{n}")
	public void starring(@PathParam("n") int n) throws UnknownHostException {
		DatabaseManipulator.moviesActorsStarIn(n);
	}

	@GET
	@Path("/listactors")
	public String listActors() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("movies");
		DBCollection actors = db.getCollection("actors");
		String actorsNames = "";
		for (String name : DatabaseManipulator.getNames(actors)) {
			actorsNames += "<a href=\"./actor/" + name + "\">" + name + "</a><br>";
		}
		return actorsNames;
	}

	@GET
	@Path("/listmovies")
	public String listMovies() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("movies");
		DBCollection movies = db.getCollection("movies");
		String moviesNames = "";
		for (String name : DatabaseManipulator.getNames(movies)) {
			moviesNames += "<a href=\"./movie/" + name + "\">" + name + "</a><br>";
		}
		return moviesNames;
	}

	@GET
	@Path("/actor/{name}")
	public String getActor(@PathParam("name") String name) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("movies");
		DBCollection actors = db.getCollection("actors");
		return DatabaseManipulator.getObject(name, actors).toString();
	}

	@GET
	@Path("/movie/{name}")
	public String getMovie(@PathParam("name") String name) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("movies");
		DBCollection movies = db.getCollection("movies");
		return DatabaseManipulator.getObject(name, movies).toString();
	}
}