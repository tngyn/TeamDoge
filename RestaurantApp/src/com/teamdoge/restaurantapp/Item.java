package com.teamdoge.restaurantapp;

public class Item {
	String name;
	String cateName;
	int quantity;
	String unit;
	String description;
	
	public Item() {
		name = "";
		cateName = "";
		unit = "";
		quantity = 0;
		description = "";
	}
	
	public Item(String n, String c, String u, int q, String d) {
		name = n;
		cateName = c;
		unit = u;
		description = d;
		quantity = q;
	}

	public void setItem(String n, String c, String u, int q, String d){
		name = n;
		cateName = c;
		unit = u;
		description = d;
		quantity = q;
	}
}
