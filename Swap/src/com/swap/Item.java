package com.swap;

import java.io.Serializable;
import java.util.Date;

public class Item implements Serializable {
	public int id;
	public String title;
	public float price;
	public String description;
	public Date date;
	public boolean featured;
	public int rating;
	public String location;
	public boolean available;
	public String sellerid;
	public int imagesnum;
	
	public Item()
	{
		
	}
}
