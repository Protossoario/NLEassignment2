package ce887;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetector {
	private SentenceDetectorME detector;
	
	public SentenceDetector() {
		detector = loadSentenceModel("en-sent.bin");
	}
	
	private SentenceDetectorME loadSentenceModel(String modelName) {
		InputStream modelIn = null;
		SentenceModel model;

		try {
			modelIn = new FileInputStream(modelName);
			model = new SentenceModel(modelIn);
		}
		catch (IOException e) {
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
		
		return new SentenceDetectorME(model);
	}
	
	public String[] getSentencesFromText(String text) {
		return detector.sentDetect(text);
	}
}
