package tru.dr.a;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class showPermission extends Activity {
    
	ListView permissionList;
	TextView appName;

	ActivityManager activityManager;
	ArrayList<PermissionInfo> retval;
	int i = 0;
	String selectedApp, AName;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_permission);

        permissionList = (ListView) findViewById(R.id.permissionList);
        appName = (TextView) findViewById(R.id.appName);
        
        retval = new ArrayList<PermissionInfo>();
        
        Intent intent = getIntent();
		
        selectedApp = intent.getStringExtra("packagename");
        AName = intent.getStringExtra("AName");
        
        //Blok - PackageInfo{41dfdd38com.onetouchgame.Blok}
        //list all packages and id
         
        PackageManager p = getPackageManager();
        List<PackageInfo> appinstall = p.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        Iterator<PackageInfo> it = appinstall.iterator();
        while(it.hasNext()){
        	PackageInfo rf = (PackageInfo)it.next();
        	String an = rf.toString().substring(rf.toString().indexOf(" ")+1, rf.toString().indexOf("}"));
        //	appName.setText(selectedApp); from show permission
        //	appName.setText(an);
        //	tw.append(rf.toString());  > show all packages installed on the device
        	
        	//if(rf.toString().contains(selectedApp))
        	if(an.compareTo(selectedApp) > 0){//compare package name
        		 appName.setText("App List >> " + AName+" ( "+selectedApp + " ) "); 
        	//	appName.setText(selectedApp+" " + id);
        		//get Blok's permissions list
        		if (rf.requestedPermissions != null)
                {
                    for (String permName : rf.requestedPermissions)
                    {	i++;
                        try
                        {
                            retval.add(p.getPermissionInfo(permName, PackageManager.GET_META_DATA));
                        }
                        catch (NameNotFoundException e)
                        {
                            // Not an official android permission...permission name not found
                        }
                    }
                }
        	}
        	
        	
        	
        }
        //load all permissions into the listview
        ArrayAdapter<PermissionInfo> arrayAdapter = new MyAdapter(
                this, android.R.layout.simple_list_item_1, retval);
        
        permissionList.setFastScrollEnabled(true);

        permissionList.setAdapter(arrayAdapter); 
        
        
    }
    
  
     
 
     
}