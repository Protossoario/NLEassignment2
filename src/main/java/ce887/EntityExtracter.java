package ce887;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

public class EntityExtracter {
	
	private NameFinderME loadNER(String modelName) {
		InputStream modelIn = null;
		TokenNameFinderModel model;
		
		try {
			modelIn = new FileInputStream(modelName);
			model = new TokenNameFinderModel(modelIn);
		}
		catch (IOException e) {
			// Model loading failed, handle the error
			model = null;
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
		
		return new NameFinderME(model);
	}
	
	public String extractPeople(String[] tokens) {
		NameFinderME tagger = loadNER("en-ner-person.bin");
		
		Span[] nameSpans = tagger.find(tokens);
		String[] entitiesStrings = Span.spansToStrings(nameSpans, tokens);
		StringBuilder result = new StringBuilder();
		for (String s : entitiesStrings) {
			if (result.length() > 0) {
				result.append('\n');
			}
			result.append(s);
		}
		
		return result.toString();
	}
	
	public String extractLocations(String[] tokens) {
		NameFinderME tagger = loadNER("en-ner-location.bin");
		
		Span[] nameSpans = tagger.find(tokens);
		String[] entitiesStrings = Span.spansToStrings(nameSpans, tokens);
		StringBuilder result = new StringBuilder();
		for (String s : entitiesStrings) {
			if (result.length() > 0) {
				result.append('\n');
			}
			result.append(s);
		}
		
		return result.toString();
	}
	
	public String extractOrganizations(String[] tokens) {
		NameFinderME tagger = loadNER("en-ner-organization.bin");
		
		Span[] nameSpans = tagger.find(tokens);
		String[] entitiesStrings = Span.spansToStrings(nameSpans, tokens);
		StringBuilder result = new StringBuilder();
		for (String s : entitiesStrings) {
			if (result.length() > 0) {
				result.append('\n');
			}
			result.append(s);
		}
		
		return result.toString();
	}
	
	public String extractDates(String[] tokens) {
		NameFinderME tagger = loadNER("en-ner-date.bin");
		
		Span[] nameSpans = tagger.find(tokens);
		String[] entitiesStrings = Span.spansToStrings(nameSpans, tokens);
		StringBuilder result = new StringBuilder();
		for (String s : entitiesStrings) {
			if (result.length() > 0) {
				result.append('\n');
			}
			result.append(s);
		}
		
		return result.toString();
	}
}
