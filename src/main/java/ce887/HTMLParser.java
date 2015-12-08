package ce887;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HTMLParser {
	public String parseHTML(String fileName) {
		File input = new File(fileName);
		StringBuilder res = new StringBuilder();
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			for (Element el : doc.select("meta")) {
				if (res.length() > 0) {
					res.append('\n');
				}
				res.append(el.attr("content"));
			}
			if (res.length() > 0) {
				res.append('\n');
			}
			res.append(doc.text());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res.toString();
	}
}
