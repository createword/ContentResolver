package com.example.androidlistview;

public class ImageFloder {
	private String dir; // wenjianlujing
	private String dirname;
	private String firstImagePath;
	private int count;

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
	
		this.dir = dir;
		int lastIndexOf = this.dir.lastIndexOf("/");
		this.dirname = this.dir.substring(lastIndexOf);
	}

	public String getDirname() {
		return dirname;
	}

	public String getFirstImagePath() {
		return firstImagePath;
	}

	public void setFirstImagePath(String firstImagePath) {
		this.firstImagePath = firstImagePath;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
