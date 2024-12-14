package com.developer.nefarious.zjoule.core.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import com.developer.nefarious.zjoule.core.Activator;

public class ViewRender implements IViewRender {

	private static final String PROJECT_NAME = Activator.PLUGIN_ID;

	private static final String VIEW_FILES_PATH = "resources/views/";
	
	private ViewRender() { }
	
	public static IViewRender create() {
		return new ViewRender();
	}

	@Override
	public String build() {
		String marked = getResourceContent("marked.min.js");
		String js = getResourceContent("scripts.js");
		String css = getResourceContent("styles.css");
		
		StringBuilder buffer = new StringBuilder();

		buffer.append("<!doctype html>");
		buffer.append("<html lang=\"en\">");
		buffer.append("<head>");
		buffer.append("<meta charset=\"utf-8\">");
		buffer.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		buffer.append("<title>Sample View</title>");
		buffer.append("<style>" + css + "</style>");
		buffer.append("<script>" + marked + "</script>");
		buffer.append("<script>" + js + "</script>");
		buffer.append("</script>");
		buffer.append("</head>");
		buffer.append("<body>");
		buffer.append("<div class=\"center-instructions\" id=\"instructions\">");
		buffer.append("<h2>Let’s Get Started!</h2>");
		buffer.append("<p>Locate and click on the \"Authentication\" Button at the top of the screen.</p>");
		buffer.append("</div>");
		buffer.append("<div class=\"chat-container\">");
		buffer.append("<div class=\"chat-box\" id=\"chatBox\">");
		buffer.append("</div>");
		buffer.append("<div class=\"chat-input\">");
		buffer.append("<div class=\"tag-box\">#Tag</div>");
		buffer.append("<input type=\"text\" id=\"userInput\" placeholder=\"Type your message here...\" />");
		buffer.append("<button onclick=\"sendMessage()\">Send</button>");
		buffer.append("</div>");
		buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");

		return buffer.toString();
	}

	@Override
	public String getResourceContent(final String filename) {
		Bundle bundle = Platform.getBundle(ViewRender.PROJECT_NAME);
		final URL unresolvedfileURL = bundle.getEntry(VIEW_FILES_PATH + filename);
		try {
			URL resolvedFileURL = FileLocator.toFileURL(unresolvedfileURL);
			try (InputStream inputStream = resolvedFileURL.openStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				StringBuilder content = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					content.append(line).append("\n");
				}
				return content.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

}
