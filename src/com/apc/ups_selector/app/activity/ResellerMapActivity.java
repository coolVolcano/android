package com.apc.ups_selector.app.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import com.apc.ups_selector.app.R;
import com.apc.ups_selector.app.util.Location;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ResellerMapActivity extends MapActivity{
	
	private MapController mapController;
	private MapView mapView;
    
	private double latitude = 40.007080;           //SE China Headquarter
	private double longitude = 116.4886910;        //SE China Headquarter

	private double latitudeOfLocation = 39.904214;   
	private double longtitudeOfLocation = 116.407414;
	private int radius = 20000;   //20km
	private String googlePlaceKey = "AIzaSyAQSwQRjV0ka4OmM-g_z3C3s64ZiKNGMxE";
	private String searchKey = "Schneider";
	
    int zoomLevel=12;
	
	 /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        
        //customize title
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.reseller_map); 
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        
        mapView = (MapView) findViewById(R.id.mapReseller);
        mapView.setStreetView(true);
        mapView.setBuiltInZoomControls(true);
        
        mapController=mapView.getController();
        mapController.setZoom(zoomLevel);
        
        loadLocations();
        
        new MarkOnMapTask().execute(searchKey);
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private List<Location> loadLocations(){
		List<Location> locations = new ArrayList<Location>();
		Location location;
		
		location = new Location();
		location.setLatitude(40.007080);
		location.setLongitude(116.4886910);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.8373240);
		location.setLongitude(116.3430160);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.9292460);
		location.setLongitude(116.371490);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.8439630);
		location.setLongitude(116.4441840);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.9078360);
		location.setLongitude(116.449110);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.9252950);
		location.setLongitude(116.3735610);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.9052920);
		location.setLongitude(116.4608380);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.9152360);
		location.setLongitude(116.4928220);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.9805520);
		location.setLongitude(116.4765940);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.7612160);
		location.setLongitude(116.5213040);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.8037220);
		location.setLongitude(116.5095630);
		locations.add(location);
		
		location = new Location();
		location.setLatitude(39.7609480);
		location.setLongitude(116.5221780);
		locations.add(location);
		
		return locations;
	}

	private StringBuilder searchLocationFromName(String name){
		StringBuilder stringBuilder = new StringBuilder();
        try {

	        name = name.replaceAll(" ","%20"); 
	        HttpPost httppost = new HttpPost("https://maps.googleapis.com/maps/api/place/search/json?location="+latitudeOfLocation+","+longtitudeOfLocation+"&radius="+radius+"&name="+name+"&sensor=false&key="+googlePlaceKey);
	        HttpClient client = new DefaultHttpClient();
	        HttpResponse response;
	        stringBuilder = new StringBuilder();
	
            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
	    } catch (ClientProtocolException e) {
	        
	    } catch (IOException e) {
	        
	    }
	    
	    return stringBuilder;
	}
	
	private List<Location> getLocations(String name){
		StringBuilder sb = this.searchLocationFromName(name);
		
		List<Location> locations = new ArrayList<Location>();
		
		Location location = new Location();
		location.setLatitude(this.latitude);
		location.setLongitude(this.longitude);
		
		locations.add(location);
		
		JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(sb.toString());
            
            JSONArray results = (JSONArray)jsonObject.get("results");
            
            int countOfResults = results.length();
            if(countOfResults>0){
            	for(int i=0;i<countOfResults;i++){
            		longitude = results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            		latitude = results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            		
            		location = new Location();
            		location.setLatitude(latitude);
            		location.setLongitude(longitude);
            		locations.add(location);
            	}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		return locations;
	}
	
	class MapOverlay extends Overlay{
		
		private GeoPoint geoPoint;
		
		public MapOverlay(GeoPoint geoPoint){
			super();
			this.geoPoint = geoPoint;
		}
	    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
	    	
	        super.draw(canvas, mapView, shadow);                   

	        //---translate the GeoPoint to screen pixels---
	        Point screenPts = new Point();
	        mapView.getProjection().toPixels(geoPoint, screenPts);

	        //---add the marker---
	        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.marker);            
	        canvas.drawBitmap(bmp, screenPts.x, screenPts.y-32, null);         
	        return true;
	    }
	}
	
	private class MarkOnMapTask extends AsyncTask<String, Void, List<Location>>{

		@Override
		protected List<Location> doInBackground(String... params) {
//			List<Location> results = getLocations(params[0]);
	        return loadLocations();
		}
		
		protected void onPostExecute(List<Location> results) {
			List<Overlay> listOfOverlays = mapView.getOverlays();
			for(Location location : results){
	        	GeoPoint p = new GeoPoint( (int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
	        	mapController.animateTo(p);
	        	MapOverlay mapOverlay = new MapOverlay(p);
	            
	            listOfOverlays.add(mapOverlay);
	            
	            mapView.invalidate();
	        }
	    }
	}
	
}
