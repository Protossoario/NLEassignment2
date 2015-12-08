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
    
    public String tag (String text) {
        InputStream modelIn = null;
        
        POSModel model;
        
        try {
            modelIn = new FileInputStream("en-pos-maxent.bin");
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
        
        POSTaggerME tagger = new POSTaggerME(model);
        
        
        return tagger.tag(text);
    }
    
}
