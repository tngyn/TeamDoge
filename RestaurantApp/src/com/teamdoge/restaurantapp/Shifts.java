package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class Shifts {

	public ArrayList<Shifts> children;
	public ArrayList<String> selection;
	
	
	public String name;
	public String position;
	
	public Shifts() {
		children = new ArrayList<Shifts>();
		selection = new ArrayList<String>();
	}
	
	public Shifts(String name) {
		this();
		this.name = name;
	}
	
	public Shifts(String name, String position) {
		this();
		this.name = name;
		this.position = position;
	}
	public String toString() {
		return this.name;
	}
	
	private void generateChildren(String[] names) {
		for(int i = 0; i < names.length; i++) {
			if (names[i] != null) {
			  String[] trie = names[i].split(":");
			  Shifts cat = new Shifts(trie[0],trie[1]);
			  this.children.add(cat);
			}
		}
	}
	
	public static ArrayList<Shifts> getCategories(String[] shifts, String[][]names) {
		ArrayList<Shifts> categories = new ArrayList<Shifts>();
		for(int i = 0; i < shifts.length ; i++) {
			Shifts cat = new Shifts(shifts[i]);
			cat.generateChildren(names[i]);
			categories.add(cat);
		}
		return categories;
	}
	
	public static Shifts get(String name,String[] shifts, String[][]names)
	{
		ArrayList<Shifts> collection = Shifts.getCategories(shifts, names);
		for (Iterator<Shifts> iterator = collection.iterator(); iterator.hasNext();) {
			Shifts cat = (Shifts) iterator.next();
			if(cat.name.equals(name)) {
				return cat;
			}
		}
		return null;
	}
}
