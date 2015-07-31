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
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@Path("/mongo")
public class MongoService {

	private static final String MENU = "<html><head><link rel='stylesheet' type='text/css' href='../../style.css'>"
			+ "<script type='text/javascript' src='https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js'>"
			+ "</script><script type='text/javascript' src='../../menu_ui.js'></script></head>"
			+ "<body><h1>Welcome</h1><h2 class='menuToggle'>Hide menu</h2><ul id='menu'><li>"
			+ "<a href='../../create_actor.html'>Create actor</a></li><li><a href='../../add_actor.html'>"
			+ "Add actor to movie</a></li><li><a href='../../delete_actor.html'>Delete actor</a></li>"
			+ "<li><a href='../../create_movie.html'>Create movie</a></li>"
			+ "<li><a href='../../delete_movie.html'>Delete Movie</a>"
			+ "</li><li><a href='../../rest/mongo/listactors'>List Actors</a></li><li>"
			+ "<a href='../../rest/mongo/listmovies'>List Movies</a></li><li>"
			+ "<a href='../../rest/mongo/latest100'>Latest movies</a></li><li>"
			+ "<a href='../../rest/mongo/deletedb'>Delete all</a></li><li>"
			+ "<a href='../../rest/mongo/generate'>Generate data</a></li></ul>";

	@GET
	@Path("/generate")
	@Produces(MediaType.TEXT_HTML)
	public String generate() throws UnknownHostException {
		DatabaseManipulator.generate();
		return MENU + "Data generated!";
	}

	@GET
	@Path("/latest{n}")
	@Produces(MediaType.TEXT_HTML)
	public String sortMoviesByYear(@PathParam("n") int n) throws UnknownHostException {
		return MENU + DatabaseManipulator.sortMovies(n).replaceAll("\n", "<br>");
	}

	@POST
	@Path("/createactor")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String newActor(@FormParam("name") String name, @FormParam("description") String description,
			@FormParam("date") String dateBirth) throws UnknownHostException, ParseException {
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
		Date date = format.parse(dateBirth);
		DatabaseManipulator.addNewActor(new Actor(name, description, date));
		return MENU + "Actor created successfully";
	}

	@POST
	@Path("/createmovie")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String newMovie(@FormParam("name") String name, @FormParam("year") int year) throws UnknownHostException {
		DatabaseManipulator.addNewMovie(new Movie(name, year, new ArrayList<Actor>()));
		return MENU + "Movie created successfully";
	}

	@POST
	@Path("/addactor")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addActor(@FormParam("actorName") String actorName, @FormParam("movieName") String movieName)
			throws UnknownHostException, ParseException {
		if (DatabaseManipulator.addNewActorToMovie(actorName, movieName)) {
			return MENU + "Actor added successfully";
		} else {
			return MENU + "Movie or actor doesn't exist in database!";
		}
	}

	@GET
	@Path("/sortactors{n}/{p}")
	public String sortActors(@PathParam("n") String n, @PathParam("p") String p) throws UnknownHostException {
		return MENU.replaceAll("../../", "../../../")
				+ DatabaseManipulator.sortActorsStarringInMovie(n, p).replaceAll("\n", "<br>");
	}

	@GET
	@Path("/deletedb")
	public String deleteDB() throws UnknownHostException {
		DatabaseManipulator.delete();
		return MENU + "Deleted database";
	}

	@POST
	@Path("/deleteactor")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String deleteActor(@FormParam("name") String name) throws UnknownHostException {
		if (DatabaseManipulator.removeActors(name)) {
			return MENU + "Actor deleted successfully";
		} else {
			return MENU + "No such actor in database";
		}
	}

	@POST
	@Path("/deletemovie")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String deleteMovie(@FormParam("name") String name) throws UnknownHostException {
		if (DatabaseManipulator.removeMovie(name)) {
			return MENU + "Deleted " + name;
		} else {
			return MENU + "No such movie in database";
		}
	}

	@GET
	@Path("/starring{n}")
	@Produces(MediaType.TEXT_HTML)
	public String starring(@PathParam("n") String name) throws UnknownHostException {
		return MENU + String.format("Movies %s stars in:<br>%s", name,
				DatabaseManipulator.moviesActorStarsIn(name).replaceAll("\n", "<br>"));
	}

	@GET
	@Path("/listactors")
	@Produces(MediaType.TEXT_HTML)
	public String listActors() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("movies");
		DBCollection actors = db.getCollection("actors");
		String actorsNames = "";
		for (String name : DatabaseManipulator.getNames(actors)) {
			actorsNames += String.format("<a href=\"./actor/%s\">%s</a><br>", name, name);
		}
		return MENU + actorsNames;
	}

	@GET
	@Path("/listmovies")
	@Produces(MediaType.TEXT_HTML)
	public String listMovies() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("movies");
		DBCollection movies = db.getCollection("movies");
		String moviesNames = "";
		for (String name : DatabaseManipulator.getNames(movies)) {
			moviesNames += String.format("<a href=\"./movie/%s\">%s</a><br>", name, name);
		}
		return MENU + moviesNames;
	}

	@GET
	@Path("/actor/{name}")
	@Produces(MediaType.TEXT_HTML)
	public String getActor(@PathParam("name") String name) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("movies");
		DBCollection actors = db.getCollection("actors");
		DBObject actor = DatabaseManipulator.getObject(name, actors);
		return MENU.replaceAll("../../", "../../../") + String.format(
				"<h2>%s</h2><h3>Description</h3>%s<h3>Date of birth</h3>%s<br><a href=\"../starring%s\">Movies starring %s</a>",
				name, actor.get("description"), actor.get("dateBirth").toString(), name, name);
	}

	@GET
	@Path("/movie/{name}")
	@Produces(MediaType.TEXT_HTML)
	public String getMovie(@PathParam("name") String name) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("movies");
		DBCollection movies = db.getCollection("movies");
		DBObject movie = DatabaseManipulator.getObject(name, movies);
		return MENU.replaceAll("../../", "../../../") + String.format(
				"<h2>%s</h2><h3>Year</h3>%s<br><h3>Starring:</h3>%s",
				name, movie.get("year"),
				DatabaseManipulator.sortActorsStarringInMovie(name, "dateBirth").replaceAll("\n", "<br>"));
	}
}