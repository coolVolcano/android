package com.apc.ups_selector.app.ws;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.apc.ups_selector.app.entity.QueryBean;
import com.apc.ups_selector.app.entity.QueryBeanAndroid;
import com.apc.ups_selector.app.entity.UPSBean;

public class WebServiceHelper {
	private static final String targetNameSpace = "http://www.apc.com/ups_selector";
	
	private static final String search = "search";
	
	private static final String getUPSFeatures = "getUPSFeatures";
	
	private static final String getUPSImageLink = "getUPSImageLink";
	
	private static final String wsdl = "http://10.177.201.90:7070/ups-selector/upsSelector?wsdl";
	
	public static List<UPSBean> getAllUps(QueryBean query){
		List<UPSBean> allUpsList = new ArrayList<UPSBean>();
		
		QueryBeanAndroid theQueryBean = new QueryBeanAndroid();
		theQueryBean.setProperty(0, query.getShopCapable());
		theQueryBean.setProperty(1, query.getCountryCode());
		theQueryBean.setProperty(2, query.getTargetVah());
		theQueryBean.setProperty(3, query.getWatts());
		theQueryBean.setProperty(4, query.getMaxWatts());
		theQueryBean.setProperty(5, query.getPower());
		theQueryBean.setProperty(6, query.getPowerType());
		theQueryBean.setProperty(7, query.getAppType());
		theQueryBean.setProperty(8, query.getVoltageIn());
		theQueryBean.setProperty(9, query.getVoltageOut());
		theQueryBean.setProperty(10, query.getRackmount());
		theQueryBean.setProperty(11, query.getOem());
		theQueryBean.setProperty(12, query.getRedundant());
		theQueryBean.setProperty(13, query.getBaseSkuOnly());
		theQueryBean.setProperty(14, query.getGetLargeUPS());
		theQueryBean.setProperty(15, query.getUpsFamily());
		theQueryBean.setProperty(16, query.getPriceListCode());
		theQueryBean.setProperty(17, query.getSelectNum());
		theQueryBean.setProperty(18, query.getUsbSolution());
		theQueryBean.setProperty(19, query.getOnlineSolution());
		theQueryBean.setProperty(20, query.getWebDisplayed());
		theQueryBean.setProperty(21, query.getSupportAllVoltages());
		theQueryBean.setProperty(22, query.getRuntime());
		
		SoapObject request  = new SoapObject(targetNameSpace, search);
		request.addProperty("query", theQueryBean);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.setOutputSoapObject(request);
		HttpTransportSE  httpTrans = new HttpTransportSE(wsdl);
		
		try{
			httpTrans.call(targetNameSpace+search, envelope);
			@SuppressWarnings("unchecked" )
			List<SoapObject> results = (List<SoapObject>)envelope.getResponse();
			if(null!=results){
				for(int i=0;i<results.size();i++){
					SoapObject singleResult = results.get(i);
					allUpsList.add(parseUPS(singleResult));
				}
			}
		}catch(SocketTimeoutException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(XmlPullParserException e){
			e.printStackTrace();
		}
		
		return allUpsList;
	}
	
	public static List<String> getFeatures(String sku){
		List<String> features = new ArrayList<String>();
		
		SoapObject request  = new SoapObject(targetNameSpace, getUPSFeatures);
		request.addProperty("sku", sku);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.setOutputSoapObject(request);
		HttpTransportSE  httpTrans = new HttpTransportSE(wsdl);
		
		try{
			httpTrans.call(targetNameSpace+getUPSFeatures, envelope);
			if(null!=envelope.getResponse()){
				try{
					@SuppressWarnings("unchecked")
					List<SoapObject> results = (List<SoapObject>)envelope.getResponse();
					for(int i=0;i<results.size();i++){
						features.add(results.get(i).toString());
					}
				}catch(ClassCastException e){
					//for there is only one feature returned
					features.add(envelope.getResponse().toString());
				}
			}
		}catch(SocketTimeoutException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(XmlPullParserException e){
			e.printStackTrace();
		}
		
		return features;
	}
	
	public static List<String> getUPSImageLink(String sku){
		List<String> imageLinks = new ArrayList<String>();
		
		SoapObject request  = new SoapObject(targetNameSpace, getUPSImageLink);
		request.addProperty("sku", sku);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.setOutputSoapObject(request);
		HttpTransportSE  httpTrans = new HttpTransportSE(wsdl);
		
		try{
			httpTrans.call(targetNameSpace+getUPSFeatures, envelope);
			if(null!=envelope.getResponse()){
				try{
					@SuppressWarnings("unchecked")
					List<SoapObject> results = (List<SoapObject>)envelope.getResponse();
					for(int i=0;i<results.size();i++){
						imageLinks.add(results.get(i).toString());
					}
				}catch(ClassCastException e){
					//for there is only one feature returned
					imageLinks.add(envelope.getResponse().toString());
				}
			}
		}catch(SocketTimeoutException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(XmlPullParserException e){
			e.printStackTrace();
		}
		
		return imageLinks;
	}
	
	
	private static UPSBean parseUPS(SoapObject soapObject){ 
		UPSBean ups = new UPSBean();
		ups.setSku(soapObject.getProperty("sku").toString());
		ups.setModel(soapObject.getProperty("model").toString());
		ups.setRuntime(Integer.parseInt(soapObject.getProperty("runtime").toString()));
		ups.setPrice(Double.parseDouble(soapObject.getProperty("price").toString()));
		ups.setCurrency("$");
		ups.setPowerRated(Integer.parseInt(soapObject.getProperty("powerRated").toString()));
		ups.setVah(Integer.parseInt(soapObject.getProperty("vah").toString()));
		ups.setErpValue(Double.parseDouble(soapObject.getProperty("erpValue").toString()));
		ups.setErpPerf(Double.parseDouble(soapObject.getProperty("erpPerf").toString()));
		ups.setPartNum(soapObject.getProperty("part_num").toString());
		
		return ups;
	}
}

