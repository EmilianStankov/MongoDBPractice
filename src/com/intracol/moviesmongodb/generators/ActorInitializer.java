package com.intracol.moviesmongodb.generators;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.intracol.moviesmongodb.models.Actor;

public class ActorInitializer {
	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
	private static Random random = new Random();

	public static Actor getActor() {
		return new Actor(randomString(15), randomString(40), randomDate());
	}

	public static List<Actor> initializeActors(int n) {
		List<Actor> actors = new ArrayList<Actor>();
		for (int i = 0; i < n; i++) {
			actors.add(getActor());
		}
		return actors;
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

	private static Date randomDate() {
		int year = 1950 + random.nextInt(60);
		int dayOfYear = 1 + random.nextInt(365);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
		return calendar.getTime();
	}
}
