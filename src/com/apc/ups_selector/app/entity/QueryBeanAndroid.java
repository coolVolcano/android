package com.apc.ups_selector.app.entity;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class QueryBeanAndroid implements KvmSerializable{

	private int shopCapable;
	
	private String countryCode;
	
	private String targetVah;
	
	private String watts;
	
	private String maxWatts;
	
	private String power;
	
	private String powerType;
	
	private String appType;
	
	private String voltageIn;
	
	private String voltageOut;
	
	private String rackmount;
	
	private String oem;
	
	private String redundant;
	
	private String baseSkuOnly;
	
	private int getLargeUPS;
	
	private String upsFamily;
	
	private String priceListCode;
	
	private String selectNum;
	
	private String usbSolution;
	
	private String onlineSolution;
	
	private String webDisplayed;
	
	private int supportAllVoltages;
	
	private String runtime;
	
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0: return shopCapable;
		case 1: return countryCode;
		case 2: return targetVah;
		case 3: return watts;
		case 4: return maxWatts;
		case 5: return power;
		case 6: return powerType;
		case 7: return appType;
		case 8: return voltageIn;
		case 9: return voltageOut;
		case 10: return rackmount;
		case 11: return oem;
		case 12: return redundant;
		case 13: return baseSkuOnly;
		case 14: return getLargeUPS;
		case 15: return upsFamily;
		case 16: return priceListCode;
		case 17: return selectNum;
		case 18: return usbSolution;
		case 19: return onlineSolution;
		case 20: return webDisplayed;
		case 21: return supportAllVoltages;
		case 22: return runtime;
		default: return null;
		}
	}

	@Override
	public int getPropertyCount() {
		return 23;
	}

	@Override
	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo arg2) {
		switch(arg0){
		case 0:
			arg2.type = PropertyInfo.INTEGER_CLASS;
			//arg2.type = uriList.getClass();
			arg2.name = "shopCapable";
			break;
		case 1:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "countryCode";
			break;
		case 2:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "targetVah";
			break;
		case 3:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "watts";
			break;
		case 4:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "maxWatts";
			break;
		case 5:
			arg2.type = PropertyInfo.STRING_CLASS;
			//arg2.type = uriList.getClass();
			arg2.name = "power";
			break;
		case 6:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "powerType";
			break;
		case 7:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "appType";
			break;
		case 8:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "voltageIn";
			break;
		case 9:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "voltageOut";
			break;
		case 10:
			arg2.type = PropertyInfo.STRING_CLASS;
			//arg2.type = uriList.getClass();
			arg2.name = "rackmount";
			break;
		case 11:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "oem";
			break;
		case 12:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "redundant";
			break;
		case 13:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "baseSkuOnly";
			break;
		case 14:
			arg2.type = PropertyInfo.INTEGER_CLASS;
			arg2.name = "getLargeUPS";
			break;
		case 15:
			arg2.type = PropertyInfo.STRING_CLASS;
			//arg2.type = uriList.getClass();
			arg2.name = "upsFamily";
			break;
		case 16:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "priceListCode";
			break;
		case 17:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "selectNum";
			break;
		case 18:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "usbSolution";
			break;
		case 19:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "onlineSolution";
			break;
		case 20:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "webDisplayed";
			break;
		case 21:
			arg2.type = PropertyInfo.INTEGER_CLASS;
			arg2.name = "supportAllVoltages";
			break;
		case 22:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "runtime";
			break;
		default: break;
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		switch(arg0){
		case 0: shopCapable = Integer.parseInt(arg1.toString());break;
		case 1: countryCode = arg1.toString();break;
		case 2: targetVah = arg1.toString();break;
		case 3: watts = arg1.toString();break;
		case 4: maxWatts = arg1.toString();break;
		case 5: power = arg1.toString();break;
		case 6: powerType = arg1.toString();break;
		case 7: appType = arg1.toString();break;
		case 8: voltageIn = arg1.toString();break;
		case 9: voltageOut = arg1.toString();break;
		case 10: rackmount = arg1.toString();break;
		case 11: oem = arg1.toString();break;
		case 12: redundant = arg1.toString();break;
		case 13: baseSkuOnly =arg1.toString();break;
		case 14: getLargeUPS = Integer.parseInt(arg1.toString());break;
		case 15: upsFamily = arg1.toString();break;
		case 16: priceListCode = arg1.toString();break;
		case 17: selectNum = arg1.toString();break;
		case 18: usbSolution = arg1.toString();break;
		case 19: onlineSolution = arg1.toString();break;
		case 20: webDisplayed = arg1.toString();break;
		case 21: supportAllVoltages = Integer.parseInt(arg1.toString());break;
		case 22: runtime = arg1.toString();break;
		default:break;
		}
	}

}
