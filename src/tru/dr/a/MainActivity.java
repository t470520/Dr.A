package tru.dr.a;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    
	private Handler mHandler = new Handler();
	private long mStartRX = 0;
	private long mStartTX = 0;
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	private static final String SCREEN_CLASS_NAME = "com.android.settings.RunningServices"; 
	
	TextView RX, TX, CPU, cpu_usage, RAM;
	Button btnCheckBattery, btnBlok, btn_running_app, wifi, allapp;
	Intent intentBatteryUsage;
	ListView permissionList;
	
	MemoryInfo mi;
	ActivityManager activityManager;
	ArrayList<PermissionInfo> retval;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        RX = (TextView)findViewById(R.id.RX);
        TX = (TextView)findViewById(R.id.TX);
        CPU = (TextView)findViewById(R.id.CPU);
        cpu_usage= (TextView)findViewById(R.id.cpu_usage);
        RAM= (TextView)findViewById(R.id.RAM);
        permissionList = (ListView) findViewById(R.id.permissionList);
        btnCheckBattery = (Button)findViewById(R.id.checkBattery);
        btnBlok = (Button)findViewById(R.id.btn_Blok);
        btn_running_app = (Button)findViewById(R.id.btn_running_app);
        wifi = (Button)findViewById(R.id.wifi);
        allapp = (Button)findViewById(R.id.allapp);
        
        
        //RAM
        mi = new MemoryInfo();
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        
        
        //network
        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();
        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Uh Oh!");
			alert.setMessage("Your device does not support traffic stat monitoring.");
			alert.show();
        } else {
        	mHandler.postDelayed(mRunnable, 100);
        }
        
        
        //batter usage summary
        intentBatteryUsage = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intentBatteryUsage,0);
        
        if(resolveInfo == null){
        	Toast.makeText(getApplicationContext(), "Not Support!", Toast.LENGTH_LONG).show();
         	btnCheckBattery.setEnabled(false);
        }else{
        	btnCheckBattery.setEnabled(true);
        }
        
        //intent to battery usage page
        btnCheckBattery.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		startActivity(intentBatteryUsage);
	   }});
        
        btnBlok.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		Intent intent= new Intent(getBaseContext(), showBlok.class);
        		startActivity(intent);
	   }});
        
        btn_running_app.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		Intent intent = new Intent();
        		intent.setAction(Intent.ACTION_VIEW);
        		intent.setClassName(APP_DETAILS_PACKAGE_NAME,  SCREEN_CLASS_NAME);
        		startActivity(intent);
	   }});
        
        wifi.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		Intent intent= new Intent(getBaseContext(), WiFi.class);
        		startActivity(intent);
	   }});
	   
        allapp.setOnClickListener(new Button.OnClickListener(){
       	public void onClick(View v) {
       		// TODO Auto-generated method stub
       		Intent intent= new Intent(getBaseContext(), showAllApp.class);
       		startActivity(intent);
	   }});
   
    
  
        
    }
    
    private final Runnable mRunnable = new Runnable() {
        public void run() {
        	
        	//network traffic
        	long rxBytes = TrafficStats.getTotalRxBytes()- mStartRX;
        	long txBytes = TrafficStats.getTotalTxBytes()- mStartTX;
                	
        	RX.setText(Long.toString(rxBytes));
        	TX.setText(Long.toString(txBytes));
        	
        	//CPU all cores > to percentage
        	float cpu_usage = readUsage();
        	NumberFormat formatter = NumberFormat.getNumberInstance();
        	formatter.setMinimumFractionDigits(2);
        	formatter.setMaximumFractionDigits(0);
        	String output = formatter.format((1-cpu_usage)*100);
        	CPU.setText(output + " %");
        	
        	//RAM
        	activityManager.getMemoryInfo(mi);
            long availableMegs = mi.availMem / 1048576L; //in megByte
            long totalMegs = mi.totalMem / 1048576L;//in megByte > "require sdk 16"
           // long percent = (availableMegs/totalMegs) * 100;
            long percent = 100 - (long)((float)availableMegs/totalMegs*100);
            RAM.setText(String.valueOf(availableMegs)+"MB free | " +totalMegs+ " MB total | " + percent + "% used");
        	
        	
        	mHandler.postDelayed(mRunnable, 100);
        }
     };
     
     private float readUsage() {
    	    try {
    	        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
    	        String load = reader.readLine();

    	        String[] toks = load.split(" ");

    	        long idle1 = Long.parseLong(toks[4]);
    	        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
    	              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

    	        try {
    	            Thread.sleep(360);
    	        } catch (Exception e) {}

    	        reader.seek(0);
    	        load = reader.readLine();
    	        reader.close();

    	        toks = load.split(" ");

    	        long idle2 = Long.parseLong(toks[4]);
    	        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
    	            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
    	        
    	        float usage = (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));
    	        
    	        if(usage>1)
    	        	usage=1;

    	        return usage;
    	        
    	    //    return (float)(1-(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1)));

    	    } catch (IOException ex) {
    	        ex.printStackTrace();
    	    }

    	    return 0;
    	} 
     
     
}