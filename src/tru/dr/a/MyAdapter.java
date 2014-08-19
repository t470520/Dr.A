package tru.dr.a;

import java.util.List;

import android.widget.SectionIndexer;
import android.content.Context;
import android.content.pm.PermissionInfo;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<PermissionInfo> {

	public MyAdapter(Context context, int resource, List objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {  
	View view = super.getView(position, convertView, parent);  
	
	/*
	if (position % 2 == 1) {
	    view.setBackgroundColor(Color.BLUE);  
	} else {
	    view.setBackgroundColor(Color.CYAN);  
	}
*/
	//view.toString().contains("FINE")
	//1 2 5 6 7 10 11 17
	/*
    if(position==1 | position == 2 | position == 5 | position == 6 | position == 7 | position == 10 | position == 11 | position == 17)
    {
    	((TextView) view).setTextColor(Color.RED);

    }else {
    	((TextView) view).setTextColor(Color.BLACK);  
	}
    */
    ((TextView) view).setTextColor(Color.BLACK);  
	
 
 //   String currentLocation = RouteFinderBookmarksActivity.this.getResources().getString(R.string.Current_Location);
 //   int textColor = textView.getText().toString().equals(currentLocation) ? R.color.holo_blue : R.color.text_color_btn_holo_dark;
 //   textView.setTextColor(RouteFinderBookmarksActivity.this.getResources().getColor(textColor));

  //  return textView;
    
   
	return view;  
	}
	

}
