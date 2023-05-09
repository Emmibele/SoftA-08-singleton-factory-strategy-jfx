package ohm.softa.a08.Filter;

import java.util.HashMap;

public abstract class MealsFilterFactory {

	private static final HashMap<String, IMealsFilter> filters = new HashMap<String, IMealsFilter>();

	private MealsFilterFactory(){}

	public final IMealsFilter getStrategy(String key){
		return filters.getOrDefault(key, new NoFilter());
	}
}
