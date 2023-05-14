package ohm.softa.a08.Filter;

import ohm.softa.a08.model.Meal;

import java.util.Arrays;
import java.util.List;

public class NotesFilter extends FilterBase{

	private List<String> notes;

	public NotesFilter(String... notes){
		this.notes = Arrays.asList(notes);
	}

	@Override
	protected boolean include(Meal m) {
		for (String note : m.getNotes()) {
			note = note.toLowerCase();
			for(String filterNote : notes) {
				if(note.contains(filterNote))
					return false;
			}
		}
		return true;
	}
}
