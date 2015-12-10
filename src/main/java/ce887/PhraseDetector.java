package ce887;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

public class PhraseDetector {
    
    private ChunkerME chunker;
    
    public PhraseDetector () {
        //Initialize chunking model
        chunker = loadChunkerModel("en-chunker.bin");
    }
    
    private ChunkerME loadChunkerModel (String modelName) {
        
        InputStream modelIn = null;
        ChunkerModel model = null;

        try {
            // Load file of the chunker model
            modelIn = new FileInputStream(modelName);
            model = new ChunkerModel(modelIn);
        } catch (IOException e) {
            // Model loading failed, handle the error
            e.printStackTrace();
        } finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                } catch (IOException e) {
                }
            }
        }
        
        // Return model of the chunker
        return new ChunkerME(model);
    }
    
    public String[] extractPhrases(String[] tokens, String[] tags) {
        // Get the phrases and return it as an array of Strings
        // Each element of the array is a phrase
        return chunker.chunk(tokens, tags);
    }
}
