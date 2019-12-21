package com.furfel.factorycity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.furfel.factorycity.R;

public class LibraryListAdapter extends ArrayAdapter<String> {
	
	private final String[] values;
	private final Context context;
	
	public LibraryListAdapter(Context context, String[] values) {
		super(context, R.layout.lib_row, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.lib_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(values[position]);
		
		//GameInformation gameInfo = new GameInformation();
		
		int gr;
		
		if(position<GameInformation.graphics.length){
			imageView.setImageResource(GameInformation.graphics[position]);}
 
		return rowView;
	}
}
