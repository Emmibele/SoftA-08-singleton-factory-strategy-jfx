package ohm.softa.a08.Filter;

import ohm.softa.a08.model.Meal;

import java.util.List;

public interface IMealsFilter {
	public List<Meal> filter(List<Meal> meals);
}
