package com.coeus.pdfreader.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PdfFileDataModel {

	String bookTitle;
	String bookUrl;
	String coverUrl;
	String pdfFileName;
	String fileSize;
	
	public PdfFileDataModel(JSONObject pdfListJsonObject) {
		super();
		try {
		this.bookTitle = pdfListJsonObject.getString("title");
		this.bookUrl = pdfListJsonObject.getString("bookUrl");
		this.coverUrl = pdfListJsonObject.getString("coverUrl");
		this.pdfFileName = pdfListJsonObject.getString("fileName");
		this.fileSize = pdfListJsonObject.getString("bookSize");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public PdfFileDataModel(String  title) {
		super();
		try {
		this.bookTitle = title;
//		this.bookUrl = pdfListJsonObject.getString("bookUrl");
//		this.coverUrl = pdfListJsonObject.getString("coverUrl");
//		this.pdfFileName = pdfListJsonObject.getString("fileName");
//		this.fileSize = pdfListJsonObject.getString("bookSize");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getBookUrl() {
		return bookUrl;
	}

	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getPdfFileName() {
		return pdfFileName;
	}

	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	
}
