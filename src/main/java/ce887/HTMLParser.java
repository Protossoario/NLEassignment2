package ce887;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HTMLParser {
	public String parseHTML(String fileName) {
                // Get the filename of the HTML File
		File input = new File(fileName);
		StringBuilder res = new StringBuilder();
		try {
                        // Access the HTML file
			Document doc = Jsoup.parse(input, "UTF-8");
                        // Get each element in the HTML file that is a "meta" tag and extract the information of the "content"
			for (Element el : doc.select("meta")) {
                                // Add an end-of-line for each one of the "meta" tags
				if (res.length() > 0) {
					res.append('\n');
				}
                                // Append the content
				res.append(el.attr("content"));
			}
			if (res.length() > 0) {
				res.append('\n');
			}
                        // Get the text information of the HTML file and append it
			res.append(doc.text());
		} catch (IOException e) {
			e.printStackTrace();
		}
                // Return all the text and information extracted from HTML file as a String
		return res.toString();
	}
}
