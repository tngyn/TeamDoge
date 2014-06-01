package com.teamdoge.trackingmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;

import java.util.List;

import com.teamdoge.restaurantapp.R;

public class IngredientListAdapter extends ArrayAdapter{
	private Context context;
	
	public IngredientListAdapter(Context context, List items) {
		super(context, android.R.layout.simple_list_item_1, items);
		this.context = context;
	}
	
     // Holder for the inventory list.
    private class ViewHolder{
        TextView ingredientName;
        TextView ingredientUnitQuantity;
        TextView ingredientUnits;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder = null;
        IngredientListItem item = (IngredientListItem)getItem(position);
        View viewToUse = null;
        
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        
     // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        if (convertView == null) {
            viewToUse = mInflater.inflate(R.layout.fragment_menu, null);
            holder = new ViewHolder();
            holder.ingredientName = (TextView)viewToUse.findViewById(R.id.newIngredient);
            holder.ingredientUnitQuantity = (TextView)viewToUse.findViewById(R.id.newIngredientUnitAmount);
            holder.ingredientUnits = (TextView)viewToUse.findViewById(R.id.newIngredientUnitType);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }
        
        holder.ingredientName.setText(item.getIngredientName());
        holder.ingredientUnitQuantity.setText(item.getIngredientUnitQuantity());
        holder.ingredientUnits.setText(item.getIngredientUnits());
		return viewToUse;
    }
}
