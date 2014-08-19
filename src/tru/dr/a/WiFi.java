package tru.dr.a;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WiFi extends Activity {

	TextView mainText;
	ListView lv;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    Button refresh;
    StringBuilder sb = new StringBuilder();
    
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.check_wifi);
       lv = (ListView) findViewById(R.id.wifilist);
       mainText = (TextView) findViewById(R.id.appName);
       mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
       receiverWifi = new WifiReceiver();
       registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
       mainWifi.startScan();
       mainText.setText("AVailable AP");
       
       refresh = (Button)findViewById(R.id.refresh);
       
       refresh.setOnClickListener(new Button.OnClickListener(){
       	public void onClick(View v) {
       		// TODO Auto-generated method stub
       		mainWifi.startScan();
	   }});
       
       
       
       
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Refresh");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        mainWifi.startScan();
     //   mainText.setText("Starting Scan");
        return super.onMenuItemSelected(featureId, item);
    }

    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }
    
    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults();
            List<String> wifi_list = new ArrayList<String>();
            for(int i = 0; i < wifiList.size(); i++){
            //	Date dateFromSms = new Date(wifiList.get(i).timestamp); convert to hhmmss
                wifi_list.add(Integer.toString(i+1) + "." + (wifiList.get(i)).toString());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), 
            	    android.R.layout.simple_list_item_1, wifi_list) {
            	@Override
            	public View getView(int position, View convertView, ViewGroup parent) {
            	    View view = super.getView(position, convertView, parent);
            	    TextView text = (TextView) view.findViewById(android.R.id.text1);
   
            	      text.setTextColor(Color.BLACK);
            
            	    return view;
            	  }
            	};
            
            
            lv.setAdapter(arrayAdapter); 
            
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                	
                	String text = lv.getItemAtPosition(i).toString();
                	
                	String mac = text.substring(text.lastIndexOf("BSSID") + 6).substring(1, 18);
                	
                	Toast.makeText(getApplicationContext(), text.substring(text.lastIndexOf("BSSID") + 6).substring(1, 18), Toast.LENGTH_LONG).show();
              //    Toast.makeText(getApplicationContext(), "myPos "+i, Toast.LENGTH_LONG).show();
                	
                	
                	openAlert(view, mac);
                	/*
                	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coffer.com/mac_find/?string="+mac));
                	startActivity(browserIntent);
                	*/
                	
                }
            });
            
        }
    }
    
    
    private void openAlert(View view, final String mac) {
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	     
		 alertDialogBuilder.setTitle(this.getTitle()+ " AP checker");
		 alertDialogBuilder.setMessage("Perform BSSID " + "(" +  mac + ")" + " lookup?");
		 
		 
		 alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
		 alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coffer.com/mac_find/?string="+mac));
             	startActivity(browserIntent);

				}
			  });
		 
		 AlertDialog alertDialog = alertDialogBuilder.create();
		 alertDialog.show();
	}

}