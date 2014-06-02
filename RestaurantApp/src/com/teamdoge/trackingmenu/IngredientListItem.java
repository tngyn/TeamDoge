package com.teamdoge.trackingmenu;

public class IngredientListItem {
	private String ingredientName;
	private String ingredientUnitQuantity;
	private String ingredientUnits;

    public String getIngredientName() {
        return ingredientName;
    }
    
    public String getIngredientUnitQuantity() {
    	return ingredientUnitQuantity;
    }
    
    public String getIngredientUnits() {
    	return ingredientUnits;
    }

    public void setingredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
    
    public void setIngredientUnitQuantity(String ingredientUnitQuantity) {
        this.ingredientUnitQuantity = ingredientUnitQuantity;
    }
    
    public void setIngredientUnits(String ingredientUnits){
    	this.ingredientUnits = ingredientUnits;
    }

    public IngredientListItem(String name){
        this.ingredientName = name;
    }
}
