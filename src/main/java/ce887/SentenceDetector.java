package ce887;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetector {
	private SentenceDetectorME detector;
	
	public SentenceDetector() {
            // Initialize the sentence detector model    
            detector = loadSentenceModel("en-sent.bin");
	}
	
	private SentenceDetectorME loadSentenceModel(String modelName) {
            InputStream modelIn = null;
            SentenceModel model;

            try {
                // Load the file of the SentenceDetector model    
                modelIn = new FileInputStream(modelName);
                model = new SentenceModel(modelIn);
            }
            catch (IOException e) {
                // Model load failure, handle error
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
            
            //Return the model of the SentenceDetector
            return new SentenceDetectorME(model);
	}
	
	public String[] getSentencesFromText(String text) {
            // Return the sentences detected by the model as a String
            return detector.sentDetect(text);
	}
}
