package com.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class SearchFieldMap {
	public static Map<String,String> getSearchFieldMap() {
	final Map<String, String> searchFields = new LinkedHashMap<>();
	searchFields.put(SearchType.ADDRESS.toString(),"building,street,area");
	searchFields.put(SearchType.MOVIE.toString(), "title");
	searchFields.put(SearchType.THEATRE.toString(), "theatre_name");
	searchFields.put(SearchType.GENRE.toString(), "genre_name");
	searchFields.put(SearchType.SHOWTIME.toString(),"show_name");
	return searchFields;
	}

}
