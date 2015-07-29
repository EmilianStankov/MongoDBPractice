package com.intracol.moviesmongodb.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.intracol.moviesmongodb.models.Actor;
import com.intracol.moviesmongodb.models.Movie;

public class MovieInitializer {
	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
	private static Random random = new Random();

	public static Movie getMovie(List<Actor> actorsList) {
		int actorsCount = 1 + random.nextInt(actorsList.size());
		List<Actor> actorsInMovie = new ArrayList<Actor>();
		for (int i = 0; i < actorsCount; i++) {
			Actor a = actorsList.get(random.nextInt(actorsList.size()));
			if (!actorsInMovie.contains(a)) {
				actorsInMovie.add(a);
			}
		}
		return new Movie(randomString(15), randomYear(), actorsInMovie);
	}

	public static List<Movie> initializeMovies(int n, List<Actor> actorsList) {
		List<Movie> movies = new ArrayList<Movie>();
		for (int i = 0; i < n; i++) {
			movies.add(getMovie(actorsList));
		}
		return movies;
	}

	private static String randomString(int length) {
		char[] chars = ALPHABET.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();
	}

	private static int randomYear() {
		return 1900 + random.nextInt(115);
	}
}
