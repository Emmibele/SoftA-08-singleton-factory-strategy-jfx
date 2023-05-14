package ohm.softa.a08.Filter;

import java.util.HashMap;

public abstract class MealsFilterFactory {

	private static final HashMap<String, IMealsFilter> filters = new HashMap<String, IMealsFilter>();

	static{
		filters.put("All", new NoFilter());
		filters.put("Vegetarian", new CategoryFilter(true, "vegetarisch", "vegan"));
		filters.put("No pork", new CategoryFilter(false, "schwein"));
		filters.put("No soy", new NotesFilter("soja"));
	}

	private MealsFilterFactory(){}

	public static final IMealsFilter getStrategy(String key){
		return filters.getOrDefault(key, filters.get("All"));
	}
}
