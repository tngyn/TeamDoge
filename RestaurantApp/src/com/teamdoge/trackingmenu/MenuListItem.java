package com.teamdoge.trackingmenu;

import java.util.ArrayList;
import java.util.Iterator;

public class MenuListItem {

	public ArrayList<MenuListItem> children;
	public ArrayList<String> selection;
	
	
	public String name;
	public String position;
	
	public MenuListItem() {
		children = new ArrayList<MenuListItem>();
		selection = new ArrayList<String>();
	}
	
	public MenuListItem(String name) {
		this();
		this.name = name;
	}
	
	public MenuListItem(String name, String position) {
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
			  MenuListItem cat = new MenuListItem(trie[0],trie[1]);
			  this.children.add(cat);
			}
		}
	}
	
	public static ArrayList<MenuListItem> getCategories(String[] shifts, String[][]names) {
		ArrayList<MenuListItem> categories = new ArrayList<MenuListItem>();
		for(int i = 0; i < shifts.length ; i++) {
			MenuListItem cat = new MenuListItem(shifts[i]);
			cat.generateChildren(names[i]);
			categories.add(cat);
		}
		return categories;
	}
	
	public static MenuListItem get(String name,String[] shifts, String[][]names)
	{
		ArrayList<MenuListItem> collection = MenuListItem.getCategories(shifts, names);
		for (Iterator<MenuListItem> iterator = collection.iterator(); iterator.hasNext();) {
			MenuListItem cat = (MenuListItem) iterator.next();
			if(cat.name.equals(name)) {
				return cat;
			}
			
		}
		return null;
	}
}
