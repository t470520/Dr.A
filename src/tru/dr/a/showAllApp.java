package tru.dr.a;

import java.util.ArrayList;

import android.view.View;
import android.widget.SectionIndexer;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class showAllApp extends Activity {
    
	ListView appList;
	TextView appName;

	ActivityManager activityManager;
	ArrayList<ApplicationInfo> retval;
	ArrayList<String> results = new ArrayList<String>();
	int i = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_app);

        appList = (ListView) findViewById(R.id.appList);
        appName = (TextView) findViewById(R.id.appName);
        
        retval = new ArrayList<ApplicationInfo>();
        
        //Blok - PackageInfo{41dfdd38com.onetouchgame.Blok}
        //list all packages and id
        appName.setText("App List");
        
        PackageManager pm = this.getPackageManager();
        
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        int i=0;
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        for (ResolveInfo rInfo : list) {
        results.add(i +"-"+rInfo.activityInfo.applicationInfo.loadLabel(pm).toString()+"- "+rInfo.toString());
         i++;
      //   Log.w("Installed Applications", rInfo.activityInfo.applicationInfo.loadLabel(pm).toString());
        } 
        
        //load all permissions into the listview
        ArrayAdapter<PermissionInfo> arrayAdapter = new MyAdapter(
                this, android.R.layout.simple_list_item_1, results);
        
        appList.setFastScrollEnabled(true);

        appList.setAdapter(arrayAdapter); 
        
        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
            	
            	String text = appList.getItemAtPosition(i).toString();
            	String AName = text.substring(text.indexOf("-")+1,text.lastIndexOf("-"));
            	String mac = text.substring(text.lastIndexOf("-")+2);
            	
            	String packagename = mac.substring(mac.indexOf(" ")+1, mac.lastIndexOf(" ")-8);
            	
            	Toast.makeText(getApplicationContext(), packagename, Toast.LENGTH_LONG).show();
            	
            	
            	
            	Intent ii = new Intent(getApplicationContext(), showPermission.class);
            	ii.putExtra("packagename", packagename);
            	ii.putExtra("AName", AName);
        		startActivity(ii);
        	
            	
          //    Toast.makeText(getApplicationContext(), "myPos "+i, Toast.LENGTH_LONG).show();
            	
            	
            //	openAlert(view, mac);
            	/*
            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coffer.com/mac_find/?string="+mac));
            	startActivity(browserIntent);
            	*/
            	
            }
        });
  
        
    }
    
  
     
 
     
}