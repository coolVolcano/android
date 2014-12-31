package com.apc.ups_selector.app.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.apc.ups_selector.app.R;
import com.apc.ups_selector.app.entity.QueryBean;
import com.apc.ups_selector.app.entity.UPSBean;
import com.apc.ups_selector.app.util.UPSSelectorUtil;
import com.apc.ups_selector.app.ws.WebServiceHelper;

public class Main2Activity extends Activity implements OnClickListener{
	
	private Spinner spinPMargin;
	private Button btnSearch;
	private Spinner spinRuntimeHours;
	private Spinner spinRuntimeMins;
	private RadioButton radioRackmountable_yes;
	private RadioButton radioRackmountable_no;
	private RadioButton radioRedundant_yes;
	private RadioButton radioRedundant_no;
	private ProgressDialog progress;
	
	private double totalWatts = 0;
	private int runtime = 0;
	private double maxWatts = 0;
	private String powerType = "";
	private String voltageIn = "";
	private String voltageOut = "";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //customize title
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main2); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        
        totalWatts = getIntent().getDoubleExtra("watts", 0.0);
        powerType = getIntent().getStringExtra("powerType");
        voltageIn = getIntent().getStringExtra("voltageIn");
        voltageOut = getIntent().getStringExtra("voltageOut");
        spinPMargin = (Spinner) findViewById(R.id.spinPMargin);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        
        spinRuntimeHours = (Spinner)findViewById(R.id.spinRuntimeHours);
        spinRuntimeMins = (Spinner)findViewById(R.id.spinRuntimeMins);
        radioRackmountable_yes = (RadioButton)findViewById(R.id.radioRackmountable_yes);
        radioRackmountable_no = (RadioButton)findViewById(R.id.radioRackmountable_no);
        radioRedundant_yes = (RadioButton)findViewById(R.id.radioRedundant_yes);
        radioRedundant_no = (RadioButton)findViewById(R.id.radioRedundant_no);
        
        radioRackmountable_no.setChecked(true);
        radioRedundant_no.setChecked(true);
        
        initAllDownList();
        
        setDropDownListDefaultValue();
        
        btnSearch.setOnClickListener(this);
    }
    
    @Override
	public void onClick(View arg0) {
		
    	progress =new ProgressDialog(this);
    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	progress.setTitle(getResources().getString(R.string.progress_dialog_title));
    	progress.setMessage(getResources().getString(R.string.progress_dialog_label));
    	progress.setIndeterminate(false);
    	progress.show();

    	new GetUPSListViaWSTask().execute();
	}
    
    private void initAllDownList(){
    	
    	List<String> list = new ArrayList<String>();
    	for(int i=0;i<31;i++){
    		if(i<20){
    			list.add(new StringBuilder(String.valueOf(5*i)).append("%").toString());
    		}else{
    			list.add(new StringBuilder(String.valueOf((i-20)*10+100)).append("%").toString());
    		}
    	}
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.se_spinner_item_long,list);
		dataAdapter.setDropDownViewResource(R.layout.se_spinner_item_long);
		spinPMargin.setAdapter(dataAdapter);
		
		dataAdapter = new ArrayAdapter<String>(this, R.layout.se_spinner_item_short,Arrays.asList(this.getResources().getStringArray(R.array.runtimeHours_options)));
		dataAdapter.setDropDownViewResource(R.layout.se_spinner_item_short);
		spinRuntimeHours.setAdapter(dataAdapter);
		
		dataAdapter = new ArrayAdapter<String>(this, R.layout.se_spinner_item_short,Arrays.asList(this.getResources().getStringArray(R.array.runtimeMins_options)));
		dataAdapter.setDropDownViewResource(R.layout.se_spinner_item_short);
		spinRuntimeMins.setAdapter(dataAdapter);
    }
    
    private void setDropDownListDefaultValue(){
    	spinPMargin.setSelection(6);
    	spinRuntimeMins.setSelection(6);
    }
    
    
    private QueryBean prepareQueryData(){
    	
    	double pMargin;
    	
    	pMargin = new Double(spinPMargin.getSelectedItem().toString().replace("%","")).doubleValue();
    	
    	QueryBean query = new QueryBean();
    	
    	query.setPowerType(powerType);
    	query.setWatts(String.valueOf(totalWatts));
    	
    	voltageIn = voltageIn.replace("V", "");
    	
    	query.setVoltageIn(voltageIn);
    	query.setPower(String.valueOf(UPSSelectorUtil.calculateFutureVA(totalWatts, pMargin, query.getPowerType())));
    	query.setShopCapable(1);
    	query.setCountryCode("us");
    	
    	String runtimeMins = spinRuntimeMins.getSelectedItem().toString();
    	if(runtimeMins.startsWith("0"))
    		runtimeMins = runtimeMins.replaceFirst("0", "");
    	runtime = UPSSelectorUtil.calculateRuntime((new Integer(spinRuntimeHours.getSelectedItem().toString())).intValue(), (new Integer(runtimeMins)).intValue());
    	
    	query.setTargetVah(String.valueOf(UPSSelectorUtil.getTargetVAH(totalWatts, runtime)));
    	
    	maxWatts = UPSSelectorUtil.calculateFutureWatts(totalWatts, pMargin);
    	query.setMaxWatts(String.valueOf(maxWatts));
    	
    	query.setAppType("");
    
    	query.setVoltageOut(voltageOut.replace("V", ""));
    	
    	if(radioRackmountable_yes.isChecked())
    		query.setRackmount("yes");
    	else if(radioRackmountable_no.isChecked())
    		query.setRackmount("no");
    	
    	if(radioRedundant_yes.isChecked())
    		query.setRedundant("yes");
    	else if(radioRedundant_no.isChecked())
    		query.setRedundant("no");
    	
    	query.setOem("apc");
    	query.setBaseSkuOnly("0");
    	query.setGetLargeUPS(1);
    	query.setUpsFamily("");
    	query.setPriceListCode("erp");
    	query.setSelectNum("50");
    	query.setUsbSolution("no");
    	query.setOnlineSolution("no");
    	query.setWebDisplayed("UPS Selector");
    	query.setSupportAllVoltages(0);
    	query.setRuntime(String.valueOf(runtime));
    	
    	return query;
    	
    }
    
    private class GetUPSListViaWSTask extends AsyncTask<Void, Void, ArrayList<UPSBean>>{

		@Override
		protected ArrayList<UPSBean> doInBackground(Void... params) {
			QueryBean query = prepareQueryData();
			ArrayList<UPSBean> upsList = (ArrayList<UPSBean>)WebServiceHelper.getAllUps(query);
			
	        return upsList;
		}
		
		protected void onPostExecute(ArrayList<UPSBean> result) {
			progress.dismiss();
			Intent intent = new Intent(Main2Activity.this,SearchResultsActivity.class);
			intent.putParcelableArrayListExtra("com.apc.ups_selector.app.activity.Results", result);
			intent.putExtra("watts", totalWatts);
			intent.putExtra("runtime", runtime);
			intent.putExtra("maxWatts", maxWatts);
			
			if(result!=null && result.size()>0){
				Main2Activity.this.startActivityForResult(intent, 9002);
			}else{
				Toast.makeText(Main2Activity.this.getApplicationContext(), Main2Activity.this.getResources().getText(R.string.no_ups_found), Toast.LENGTH_LONG).show();
			}
			
	    }
    }
}