package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class Shifts {

	public ArrayList<Shifts> children;
	public ArrayList<String> selection;
	
	
	public String name;
	
	public Shifts() {
		children = new ArrayList<Shifts>();
		selection = new ArrayList<String>();
	}
	
	public Shifts(String name) {
		this();
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
	
	// generate some random amount of child objects (1..10)
	private void generateChildren() {

		for(int i=0; i < 10; i++) {
			Shifts cat = new Shifts("Child "+i);
			this.children.add(cat);
		}
	}
	
	public static ArrayList<Shifts> getCategories() {
		ArrayList<Shifts> categories = new ArrayList<Shifts>();
		for(int i = 0; i < 10 ; i++) {
			Shifts cat = new Shifts("Category "+i);
			cat.generateChildren();
			categories.add(cat);
		}
		return categories;
	}
	
	public static Shifts get(String name)
	{
		ArrayList<Shifts> collection = Shifts.getCategories();
		for (Iterator<Shifts> iterator = collection.iterator(); iterator.hasNext();) {
			Shifts cat = (Shifts) iterator.next();
			if(cat.name.equals(name)) {
				return cat;
			}
			
		}
		return null;
	}
}
