package com.workorder.scheduler.ui.chart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.vaadin.server.StreamResource.StreamSource;

public class MyImageSource implements StreamSource {

	private static final long serialVersionUID = -3405862198563019827L;

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public InputStream getStream() {
		try {
			return new ByteArrayInputStream(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}