package application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;


public class Play_C2_Streams {

	public static void main(String[] args) {
		Stream<String> names = List.of( "Hendricks", "Raymond", "Pena", "Gonzalez",
				"Nielsen", "Hamilton", "Graham", "Gill", "Vance", "Howe", "Ray", "Talley",
				"Brock", "Hall", "Gomez", "Bernard", "Witt", "Joyner", "Rutledge", "Petty",
				"Strong", "Soto", "Duncan", "Lott", "Case", "Richardson", "Crane", "Cleveland",
				"Casey", "Buckner", "Hardin", "Marquez", "Navarro" ).stream();
		
		//Punkt 1: 
		//‐ Funktion eine Stream‐Verarbeitung für Nachnamen enthält, 
		//welche Namen primär nach Länge (kurze Namen zuerst) sortiert und ausgibt. 
		//Namen gleicher Länge sollen alphabetisch sortiert werden.
		
		/*
		 names.sorted().sorted(Comparator.comparingInt(n->n.length()))
		.forEach(n-> System.out.println(n));
		*/
		
		
		//Punkt 2: 
		// Erzeugen Sie eine List<String> aus einem Stream, 
		//der am Ende nur Namen enthält, die auf “ez“ 
		//enden. Geben Sie die Liste mit forEach() aus. 
		
		 names
		.filter(n->n.endsWith("ez")).forEach(m-> System.out.print(m + ", "));
	}

}
