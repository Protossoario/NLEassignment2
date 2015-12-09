package ce887;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

public class EntityExtracter {
	public String extractPeople(String[] tokens) {
		InputStream modelIn = null;
		TokenNameFinderModel model;
		
		try {
			modelIn = new FileInputStream("en-ner-person.bin");
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
		
		NameFinderME tagger = new NameFinderME(model);
		
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
