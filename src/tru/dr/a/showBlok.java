package tru.dr.a;

import java.util.ArrayList;
import android.widget.SectionIndexer;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class showBlok extends Activity {
    
	ListView permissionList;
	TextView appName;

	ActivityManager activityManager;
	ArrayList<PermissionInfo> retval;
	int i = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_blok);

        permissionList = (ListView) findViewById(R.id.permissionList);
        appName = (TextView) findViewById(R.id.appName);
        
        retval = new ArrayList<PermissionInfo>();
        
        //Blok - PackageInfo{41dfdd38com.onetouchgame.Blok}
        //list all packages and id
         
        PackageManager p = getPackageManager();
        List<PackageInfo> appinstall = p.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        Iterator<PackageInfo> it = appinstall.iterator();
        while(it.hasNext()){
        	PackageInfo rf = (PackageInfo)it.next();
        //	tw.append(rf.toString());  > show all packages installed on the device
        	if(rf.toString().contains("Blok")){//hardcoded package name specific for Blok
        		appName.setText("PackageInfo{41dfdd38com.onetouchgame.Blok}");
        			
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