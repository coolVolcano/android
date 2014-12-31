package com.apc.ups_selector.app.entity;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class UPSBean implements Parcelable{
	
	private String model;
	
	private String sku;
	
	private int runtime;
	
	private double price;
	
	private String currency;
	
	private List<String> features;
	
	private List<String> imageSrc;
	
	private int powerRated;
	
	private int vah;
	
	private double erpValue;
	
	private double erpPerf;
	
	private String partNum;

	public String getPartNum() {
		return partNum;
	}

	public void setPartNum(String partNum) {
		this.partNum = partNum;
	}

	public double getErpPerf() {
		return erpPerf;
	}

	public void setErpPerf(double erpPerf) {
		this.erpPerf = erpPerf;
	}

	public int getPowerRated() {
		return powerRated;
	}

	public void setPowerRated(int powerRated) {
		this.powerRated = powerRated;
	}

	public int getVah() {
		return vah;
	}

	public void setVah(int vah) {
		this.vah = vah;
	}

	public double getErpValue() {
		return erpValue;
	}

	public void setErpValue(double erpValue) {
		this.erpValue = erpValue;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public List<String> getImageSrc() {
		return imageSrc;
	}

	public void setImageSrc(List<String> imageSrc) {
		this.imageSrc = imageSrc;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(model);
		arg0.writeString(sku);
		arg0.writeInt(runtime);
		arg0.writeDouble(price);
		arg0.writeString(currency);
		arg0.writeStringList(features);
		arg0.writeStringList(imageSrc);
		arg0.writeInt(powerRated);
		arg0.writeInt(vah);
		arg0.writeDouble(erpValue);
		arg0.writeDouble(erpPerf);
		arg0.writeString(partNum);
	}
	
	public static final Parcelable.Creator<UPSBean> CREATOR = new Creator<UPSBean>() {  
        @Override  
        public UPSBean createFromParcel(Parcel source) {  
        	UPSBean parcelableUPS = new UPSBean();  
        	parcelableUPS.setModel(source.readString());
        	parcelableUPS.setSku(source.readString());
        	parcelableUPS.setRuntime(source.readInt());
        	parcelableUPS.setPrice(source.readDouble());
        	parcelableUPS.setCurrency(source.readString());
        	parcelableUPS.setFeatures(source.createStringArrayList());
        	parcelableUPS.setImageSrc(source.createStringArrayList());
        	parcelableUPS.setPowerRated(source.readInt());
        	parcelableUPS.setVah(source.readInt());
        	parcelableUPS.setErpValue(source.readDouble());
        	parcelableUPS.setErpPerf(source.readDouble());
        	parcelableUPS.setPartNum(source.readString());
        	
            return parcelableUPS;  
        }  
        @Override  
        public UPSBean[] newArray(int size) {  
            return new UPSBean[size];  
        }  
    };  
}
