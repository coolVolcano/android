package com.apc.ups_selector.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Spinner;
import android.widget.Toast;

import com.apc.ups_selector.app.R;

public class UPSSelectorUtil {

	public static double va2Watt(double watt) {

		double factor = 0;

		if (watt < 800) {
			factor = 0.6;
		} else if (watt > 801 && watt < 1400) {
			factor = 0.65;
		} else if (watt > 1401 && watt < 16000) {
			factor = 0.7;
		} else {
			factor = 1.0;
		}

		return watt * factor;

	}

	public static double getTargetVAH(double tWatts, double tRuntime) {
		double nTare = 0.02 * 2 * tWatts;
		if (nTare < 6)
			nTare = 6;
		double nScale = 0.70;
		double nRslope = 0.12;
		double nExpo = 1.17;

		return (tWatts + nTare)
				* Math.pow(tRuntime / (60 * nScale) + nRslope, 1 / nExpo);
	}

	public static int getUPSRuntime(float totalWatts, int vah, int ptare,
			float bCurve_scale, float bCurve_expo, float bCurve_rSlope,
			float bCurve_comp, int max_runtime) {
		if ((vah <= 0) || (totalWatts < 0) || (ptare < 0)
				|| (totalWatts + ptare == 0) || (bCurve_scale <= 0)
				|| (bCurve_expo <= 0) || (bCurve_rSlope < 0)) {
			return 0;
		} else {
			return (int) Math.round(60
					* bCurve_scale
					* (Math.pow(vah / (totalWatts + ptare), bCurve_expo)
							- bCurve_rSlope + bCurve_comp
							* (totalWatts + ptare) / vah));
		}
	}

	public static int calculateRuntime(int rHour, int rMinute) {
		return rHour * 60 + rMinute;
	}

	public static double calculateFutureWatts(double totalWatts,
			double powerMargin) {
		return totalWatts * calculateMultFactor(powerMargin);
	}

	public static long calculateFutureVA(double power, double powerMargin,
			String powerType) {
		if (powerType.equals("kVA"))
			return Math.round(power * calculateMultFactor(powerMargin)) * 1000;
		else if (powerType.equals("VA"))
			return Math.round(power * calculateMultFactor(powerMargin));
		else
			return 0;
	}

	public static double calculateMultFactor(double powerMargin) {
		return 1 + powerMargin / 100;
	}

	public static Bitmap getBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public static String getCleanPartNum(String partNum){
		return partNum.replaceAll("[(][0-9]+[)]", "");
	}
	
	public static boolean validator(Spinner spinVoltage, String value_Voltage_in, String txtPower,Activity activity) {
    	
        
    	if("".equals(txtPower.trim())){
    		
    		Toast.makeText(activity, activity.getResources().getText(R.string.missing_power),Toast.LENGTH_LONG).show();
    	
    		return false;
    	}else if(spinVoltage.getSelectedItemId()==0){
    		
    		Toast.makeText(activity, activity.getResources().getText(R.string.missing_voltage),Toast.LENGTH_LONG).show();
    	
    		return false;
    	}else if(value_Voltage_in.indexOf(spinVoltage.getSelectedItem().toString()) == -1){
    		
    		
    		Toast.makeText(activity, activity.getResources().getText(R.string.site_and_voltages_not_compatible),Toast.LENGTH_LONG).show();
        	
    		return false;
    		
    	}else{
    		
    		return true;
    	}
	}

}
