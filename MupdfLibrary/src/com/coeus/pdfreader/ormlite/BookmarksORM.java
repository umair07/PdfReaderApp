package com.coeus.pdfreader.ormlite;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MessageTable")
public class BookmarksORM {

	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(unique=true)
	String bookmarkPageNum;
	@DatabaseField
	long millis;
	@DatabaseField
	Date date;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBookmarkPageNum() {
		return bookmarkPageNum;
	}

	public void setBookmarkPageNum(String bookmarkPageNum) {
		this.bookmarkPageNum = bookmarkPageNum;
	}

	public long getMillis() {
		return millis;
	}

	public void setMillis(long millis) {
		this.millis = millis;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	BookmarksORM() {
	}

	public BookmarksORM( String bookmarkPageNum, long millis) {
		this.bookmarkPageNum = bookmarkPageNum;
		this.millis = millis;
		this.date = new Date(millis);

	}

}
