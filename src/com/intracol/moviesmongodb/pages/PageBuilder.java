package com.intracol.moviesmongodb.pages;

import java.net.UnknownHostException;

import com.intracol.moviesmongodb.crud.DatabaseManipulator;

public class PageBuilder {
	private static final String MENU = "<html><head>"
			+ "<script type='text/javascript' src='https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js'>"
			+ "</script><script type='text/javascript' src='../../menu_ui.js'></script></head>"
			+ "<body><h1>Welcome</h1><h2 class='menuToggle'>Hide menu</h2><ul id='menu'><li>"
			+ "<a href='../../create_actor.html'>Create actor</a></li><li><a href='../../add_actor.html'>"
			+ "Add actor to movie</a></li><li><a href='../../delete_actor.html'>Delete actor</a></li>"
			+ "<li><a href='../../create_movie.html'>Create movie</a></li>"
			+ "<li><a href='../../delete_movie.html'>Delete Movie</a>"
			+ "</li><li><a href='../../rest/db/listactors'>List Actors</a></li><li>"
			+ "<a href='../../rest/db/listmovies'>List Movies</a></li><li>"
			+ "<a href='../../rest/db/latest100'>Latest movies</a></li><li>"
			+ "<a href='../../rest/db/deletedb'>Delete all</a></li><li>"
			+ "<a href='../../rest/db/generate'>Generate data</a></li></ul></body></html>";

	public static String buildGenerateDBPage() throws UnknownHostException {
		return MENU + "Data generated!";
	}

	public static String buildSortMoviesByYearPage(int n) throws UnknownHostException {
		return MENU + DatabaseManipulator.sortMovies(n).replaceAll("\n", "<br>");
	}

	public static String buildSortActorsPage(String name, String p) throws UnknownHostException {
		return MENU.replaceAll("../../", "../../../")
				+ DatabaseManipulator.sortActorsStarringInMovie(name, p).replaceAll("\n", "<br>");
	}

	public static String buildDeleteDBPage() {
		return MENU + "Deleted database";
	}

	public static String buildStarringPage(String name) throws UnknownHostException {
		return MENU + String.format("Movies %s stars in:<br>%s", name,
				DatabaseManipulator.moviesActorStarsIn(name).replaceAll("\n", "<br>"));
	}

	public static String buildListActorsPage() throws UnknownHostException {
		String actorsNames = "";
		for (String name : DatabaseManipulator.getActorNames()) {
			actorsNames += String.format("<a href=\"./actor/%s\">%s</a><br>", name, name);
		}
		return MENU + actorsNames;
	}

	public static String buildListMoviesPage() throws UnknownHostException {
		String moviesNames = "";
		for (String name : DatabaseManipulator.getMovieNames()) {
			moviesNames += String.format("<a href=\"./movie/%s\">%s</a><br>", name, name);
		}
		return MENU + moviesNames;
	}

	public static String buildActorPage(String name) {
		return MENU.replaceAll("../../", "../../../") + String.format(
				"<h2>%s</h2><h3>Description</h3>%s<h3>Date of birth</h3>%s<br>"
						+ "<a href=\"../starring%s\">Movies starring %s</a>",
				name, DatabaseManipulator.getActorDescription(name), DatabaseManipulator.getActorDateOfBirth(name),
				name, name);
	}

	public static String buildMoviePage(String name) throws UnknownHostException {
		return MENU.replaceAll("../../", "../../../") + String.format(
				"<h2>%s</h2><h3>Year</h3>%s<br><h3>Starring:</h3>%s",
				name, DatabaseManipulator.getMovieYear(name),
				DatabaseManipulator.sortActorsStarringInMovie(name, "dateBirth").replaceAll("\n", "<br>"));
	}

	public static String buildBadRequest() {
		return MENU + "Bad Request!";
	}
}
