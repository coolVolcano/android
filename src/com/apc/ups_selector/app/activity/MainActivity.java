package com.apc.ups_selector.app.activity;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.apc.ups_selector.app.R;
import com.apc.ups_selector.app.util.UPSSelectorUtil;

public class MainActivity extends Activity implements OnClickListener{
	
	private ImageButton btnNext;
	private EditText txtPower;
	private RadioButton radioWatts;
	private RadioButton radioVA;
	private RadioButton radioKva;
	private Spinner spinVoltage;
	private Spinner spinVoltage_in;
	
	private double totalWatts = 0;
	private String value_Voltage_in = "";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //customize title
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        
        txtPower = (EditText)findViewById(R.id.txtPower);
        radioWatts = (RadioButton)findViewById(R.id.radioWatts);
        radioVA = (RadioButton)findViewById(R.id.radioVA);
        radioKva = (RadioButton)findViewById(R.id.radioKva);
        spinVoltage = (Spinner)findViewById(R.id.spinVoltage);
        spinVoltage_in = (Spinner)findViewById(R.id.spinVoltage_in);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        
        ArrayAdapter<String> dataAdapter =  new ArrayAdapter<String>(this, R.layout.se_spinner_item_long,Arrays.asList(this.getResources().getStringArray(R.array.operating_voltages_options)));
		dataAdapter.setDropDownViewResource(R.layout.se_spinner_item_long);
		spinVoltage.setAdapter(dataAdapter);
		spinVoltage.setSelection(2);
		
		dataAdapter = new ArrayAdapter<String>(this, R.layout.se_spinner_item_long,Arrays.asList(this.getResources().getStringArray(R.array.voltage_in_options)));
		dataAdapter.setDropDownViewResource(R.layout.se_spinner_item_long);
		spinVoltage_in.setAdapter(dataAdapter);
		spinVoltage_in.setSelection(1);
		
		radioWatts.setChecked(true);
        
		btnNext.setOnClickListener(this);
    }
    
    @Override
	public void onClick(View arg0) {
		
    	String powerType = "";
    	totalWatts = (new Double(txtPower.getText().toString())).doubleValue();
    	if(radioWatts.isChecked()){
    		powerType = "watts";
    	}
    	else if(radioKva.isChecked()){
    		powerType = "kVA";
    		totalWatts = UPSSelectorUtil.va2Watt(totalWatts*1000);
    	}
    	else if(radioVA.isChecked()){
    		powerType = "VA";
    		totalWatts = UPSSelectorUtil.va2Watt(totalWatts);
    	}
    	
    	if(spinVoltage_in.getSelectedItemId()==0)
    		value_Voltage_in = "";	
    	else if(spinVoltage_in.getSelectedItemId()==1)
    		value_Voltage_in = "120V,208V,208V 3 Phase,480V,480V 3 Phase,600V 3 Phase";
    	else if(spinVoltage_in.getSelectedItemId()==2)
    		value_Voltage_in = "230V,400V 3 Phase";
    	else if(spinVoltage_in.getSelectedItemId()==3)
    		value_Voltage_in = "100V,200V";
    	
    	Intent intent = new Intent(MainActivity.this,Main2Activity.class);
		intent.putExtra("watts", totalWatts);
		intent.putExtra("powerType", powerType);
		intent.putExtra("voltageIn", value_Voltage_in);
		intent.putExtra("voltageOut", spinVoltage.getSelectedItem().toString());
		
		if(UPSSelectorUtil.validator(spinVoltage,value_Voltage_in,txtPower.getText().toString(),MainActivity.this))
			MainActivity.this.startActivityForResult(intent, 9001);
	}
 
}