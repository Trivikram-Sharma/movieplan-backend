package com.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FilterMap {
	public static Map<String, Set<String>> getFilterMap() {
		final Map<String, Set<String>> filterMap = new LinkedHashMap<>();
		filterMap.put(SearchType.ADDRESS.toString(),
				new HashSet<String>(Arrays.asList(
				"city","state","country","pincode"
				)));
//		filterMap.put(SearchType.MOVIE.toString(),
//				new HashSet<String>(
//						Arrays.asList(
//				"language", "status", 
//								)));
		return filterMap;
	}
}
