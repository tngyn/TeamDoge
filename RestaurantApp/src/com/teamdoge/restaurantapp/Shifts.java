package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;


public class Shifts {

	public ArrayList<Shifts> children;
	public ArrayList<String> selection;
	
	
	public String name;
	public String position;
	public boolean checked;
	private int index;
	
	public Shifts() {
		children = new ArrayList<Shifts>();
		selection = new ArrayList<String>();
		index = 0;
	}
	
	public Shifts(String name) {
		this();
		this.name = name;
	}
	
	public Shifts(String name, String position, boolean check) {
		this();
		this.name = name;
		this.position = position;
		this.checked = check;
	}
	public String toString() {
		return this.name;
	}
	
	private void generateChildren(String[] names, boolean[] check) {
		for(int i = 0; i < names.length; i++) {
			if (names[i] != null) {
			  String[] trie = names[i].split(":");
			  Log.d("ASD", "true" +  trie[0]);
			  Shifts cat = new Shifts(trie[0],trie[1], check[i]);
			  index++;
			  this.children.add(cat);
			}
		}
	}
	
	public static ArrayList<Shifts> getCategories(String[] shifts, String[][]names, boolean[][] check) {
		ArrayList<Shifts> categories = new ArrayList<Shifts>();
		for(int i = 0; i < shifts.length ; i++) {
			Shifts cat = new Shifts(shifts[i]);
			cat.generateChildren(names[i], check[i]);
			categories.add(cat);
		}
		return categories;
	}
	
	public static Shifts get(String name,String[] shifts, String[][]names, boolean[][] check)
	{
		ArrayList<Shifts> collection = Shifts.getCategories(shifts, names, check);
		for (Iterator<Shifts> iterator = collection.iterator(); iterator.hasNext();) {
			Shifts cat = (Shifts) iterator.next();
			if(cat.name.equals(name)) {
				return cat;
			}
			
		}
		return null;
	}
}
