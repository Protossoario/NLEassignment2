/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce887;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 *
 * @author eovill
 */
public class POSTagger {
	
	private POSTaggerME tagger;
	
	public POSTagger() {
		tagger = loadTaggerModel("en-pos-maxent.bin");
	}
	
	private POSTaggerME loadTaggerModel(String modelName) {
		InputStream modelIn = null;
        
        POSModel model;
        
        try {
            modelIn = new FileInputStream(modelName);
            model = new POSModel(modelIn);
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
        
        return new POSTaggerME(model);
	}
    
    public String tag (String text) {
        return tagger.tag(text);
    }
    
    public String[] tagTokens(String[] tokens) {
    	return tagger.tag(tokens);
    }
    
}
