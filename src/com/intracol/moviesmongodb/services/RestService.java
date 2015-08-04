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
import com.intracol.moviesmongodb.pages.PageBuilder;

@Path("/db")
public class RestService {

	@GET
	@Path("/generate")
	@Produces(MediaType.TEXT_HTML)
	public String generate() throws UnknownHostException {
		DatabaseManipulator.generate();
		return PageBuilder.buildGenerateDBPage();
	}

	@GET
	@Path("/latest{n}")
	@Produces(MediaType.TEXT_HTML)
	public String sortMoviesByYear(@PathParam("n") int n) throws UnknownHostException {
		if (n < 1 || n > 1000) {
			return PageBuilder.buildBadRequest();
		}
		return PageBuilder.buildSortMoviesByYearPage(n);
	}

	@GET
	@Path("/starring{n}")
	@Produces(MediaType.TEXT_HTML)
	public String starring(@PathParam("n") String name) throws UnknownHostException {
		if (name != null && name.length() < 64) {
			return PageBuilder.buildStarringPage(name);
		}
		return PageBuilder.buildBadRequest();
	}

	@GET
	@Path("/listactors")
	@Produces(MediaType.TEXT_HTML)
	public String listActors() throws UnknownHostException {
		return PageBuilder.buildListActorsPage();
	}

	@GET
	@Path("/listmovies")
	@Produces(MediaType.TEXT_HTML)
	public String listMovies() throws UnknownHostException {
		return PageBuilder.buildListMoviesPage();
	}

	@GET
	@Path("/actor/{name}")
	@Produces(MediaType.TEXT_HTML)
	public String getActor(@PathParam("name") String name) {
		if (name != null && name.length() < 64) {
			return PageBuilder.buildActorPage(name);
		}
		return PageBuilder.buildBadRequest();
	}

	@GET
	@Path("/movie/{name}")
	@Produces(MediaType.TEXT_HTML)
	public String getMovie(@PathParam("name") String name) throws UnknownHostException {
		if (name != null && name.length() < 64) {
			return PageBuilder.buildMoviePage(name);
		}
		return PageBuilder.buildBadRequest();
	}

	@GET
	@Path("/sortactors{n}/{p}")
	public String sortActors(@PathParam("n") String name, @PathParam("p") String p) throws UnknownHostException {
		if (name != null && name.length() < 64 && (p.equals("dateBirth") || p.equals("name"))) {
			return PageBuilder.buildSortActorsPage(name, p);
		}
		return PageBuilder.buildBadRequest();
	}

	@GET
	@Path("/deletedb")
	public String deleteDB() throws UnknownHostException {
		DatabaseManipulator.delete();
		return PageBuilder.buildDeleteDBPage();
	}

	@POST
	@Path("/createactor")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newActor(@FormParam("name") String name, @FormParam("description") String description,
			@FormParam("date") String dateBirth) throws UnknownHostException {
		if (name.length() >= 3 && name.length() < 64
				&& dateBirth != null && dateBirth.length() != 10
				&& description.length() < 1000 && description.length() > 10) {
			try {
				DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
				Date date = format.parse(dateBirth);
				DatabaseManipulator.addNewActor(new Actor(name, description, date));
			} catch (ParseException e) {
			}
		}
	}

	@POST
	@Path("/createmovie")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newMovie(@FormParam("name") String name, @FormParam("year") int year) throws UnknownHostException {
		if (name != null && name.length() < 64 && year > 0) {
			DatabaseManipulator.addNewMovie(new Movie(name, year, new ArrayList<Actor>()));
		}
	}

	@POST
	@Path("/addactor")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addActor(@FormParam("actorName") String actorName, @FormParam("movieName") String movieName)
			throws UnknownHostException {
		if (actorName != null && actorName.length() < 64 && movieName != null && movieName.length() < 64) {
			DatabaseManipulator.addNewActorToMovie(actorName, movieName);
		}
	}

	@POST
	@Path("/deleteactor")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void deleteActor(@FormParam("name") String name) throws UnknownHostException {
		if (name != null && name.length() < 64) {
			DatabaseManipulator.removeActors(name);
		}
	}

	@POST
	@Path("/deletemovie")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void deleteMovie(@FormParam("movieName") String name) throws UnknownHostException {
		if (name != null && name.length() < 64) {
			DatabaseManipulator.removeMovie(name);
		}
	}
}