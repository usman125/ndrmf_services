package com.ndrmf.engine.dto;

import org.hibernate.envers.tools.Pair;

public class FileReturnItem {
	private Pair<String, String> fileNameAndPath;
	
	public Pair getFileNameAndPath() {
		return fileNameAndPath;
	}
	public void setFileNameAndPath(Pair<String, String> fileNameAndPath) {
		this.fileNameAndPath = fileNameAndPath;
	}

}
