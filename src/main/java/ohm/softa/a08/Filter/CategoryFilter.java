package ohm.softa.a08.Filter;

import ohm.softa.a08.model.Meal;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class CategoryFilter extends FilterBase{

	private boolean include;
	private String[] categories;

	public CategoryFilter(boolean include, String... categories){
		this.include = include;
		this.categories = categories;
	}

	@Override
	protected boolean include(Meal m) {
		boolean contains = Arrays.stream(categories)
			.anyMatch(x -> x.toLowerCase().contains(m.getCategory().toLowerCase()));

		if(include)
			return contains;

		return !contains;
	}
}
