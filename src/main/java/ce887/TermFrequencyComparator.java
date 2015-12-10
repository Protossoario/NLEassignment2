package ce887;

import java.util.Comparator;
import java.util.HashMap;

public class TermFrequencyComparator implements Comparator<String> {
	private HashMap<String, Double> totals;
	public TermFrequencyComparator(HashMap<String, Double> totals) {
		this.totals = totals;
	}
	
	public int compare(String phraseA, String phraseB) {
		return totals.get(phraseA).compareTo(totals.get(phraseB));
	}
}
