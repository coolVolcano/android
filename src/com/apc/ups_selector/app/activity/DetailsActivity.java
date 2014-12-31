package com.apc.ups_selector.app.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.apc.ups_selector.app.R;
import com.apc.ups_selector.app.entity.UPSBean;
import com.apc.ups_selector.app.util.ImageAdapter;
import com.apc.ups_selector.app.util.UPSSelectorUtil;

public class DetailsActivity extends Activity {
	
	private TextView txtLabel;
	private TextView txtUPSModel;
	private TextView txtUPSPrice;
	private TextView txtUPSRuntime;
	private TextView txtUPSSku;
	private ListView listFeatures;
	private ListView listButtons;
	private Gallery imgGallery;
	private int index;
	private UPSBean ups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		ups = (UPSBean)getIntent().getParcelableExtra("com.apc.ups_selector.app.activity.UPS");
		
		super.onCreate(savedInstanceState);
		
		if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            DetailsFragment details = new DetailsFragment();
            details.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
        
        index = getIntent().getIntExtra("index", 0);
        
        //customize title
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.detail);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        
        txtLabel = (TextView)this.findViewById(R.id.txtLabel);
        txtUPSModel = (TextView)this.findViewById(R.id.txtUPSModel);
        txtUPSPrice = (TextView)this.findViewById(R.id.txtUPSPrice);
        txtUPSRuntime = (TextView)this.findViewById(R.id.txtUPSRuntime);
        txtUPSSku = (TextView)this.findViewById(R.id.txtUPSSku);
        listFeatures = (ListView)this.findViewById(R.id.listFeatures);
        listButtons = (ListView)this.findViewById(R.id.listButtons);
        imgGallery = (Gallery)this.findViewById(R.id.imgGallery);
        
        fillTexts(ups, index);
	}
	
	private void fillTexts(UPSBean ups, int index){
		if(index==0){
			txtLabel.setText("Best Price");
		}else if(index==1){
			txtLabel.setText("Best Value");
		}else{
			txtLabel.setText("Best Performance");
		}
		
		txtUPSModel.setText(ups.getModel());
		txtUPSPrice.setText(ups.getCurrency() + String.valueOf(ups.getPrice()));
		txtUPSRuntime.setText(String.valueOf(ups.getRuntime())+" Minutes");
		txtUPSSku.setText(UPSSelectorUtil.getCleanPartNum(ups.getPartNum()));
		
		if(null!=ups.getFeatures()){
			
			String f = ups.getFeatures().get(0).replace("[", "");
			f = f.replace("]", "");
			String[] features =  StringUtils.split(f, ",");
			List<String> featureList = Arrays.asList(features);
			
			ListAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item,featureList);
			listFeatures.setAdapter(adapter);
		}
		
		List<HashMap<String, Object>> buttonListItems = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("itemTitle", this.getResources().getString(R.string.button_call));
		map.put("itemImage",R.drawable.call_contact);
		buttonListItems.add(map);
		map = new HashMap<String, Object>();
		map.put("itemTitle", this.getResources().getString(R.string.find_reseller));
		map.put("itemImage",R.drawable.compass_arrow);
		buttonListItems.add(map);
		ListAdapter adapter2 = new SimpleAdapter(this,buttonListItems,R.layout.list_item_with_image,new String[]{"itemTitle","itemImage"}, new int[]{R.id.itemTxt,R.id.itemImg});
		listButtons.setAdapter(adapter2);
		listButtons.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

				if(position==0){
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01065037674"));
		     	    DetailsActivity.this.startActivity(intent);
				}else if(position==1){
					Intent intent = new Intent(DetailsActivity.this, ResellerMapActivity.class);
		     	    DetailsActivity.this.startActivity(intent);
				}
			}
			
		});
		
		if(ups.getImageSrc()!=null && ups.getImageSrc().size()>0){
			if(ups.getImageSrc().get(0).length()>0){
				String l = ups.getImageSrc().get(0).replace("[", "");
				l = l.replace("]", "");
				String[] links =  StringUtils.split(l, ",");
				List<String> linksList = Arrays.asList(links);
				new GetImageBitmapTask().execute(linksList);
			}
			
		}
	}
	
	private class GetImageBitmapTask extends AsyncTask<List<String>, Void, List<Bitmap>>{

		@Override
		protected List<Bitmap> doInBackground(List<String>... params) {
			
			String imageURL = "";
			List<Bitmap> images = new ArrayList<Bitmap>();
			List<String> imageLinks = params[0];
			
			for(String link : imageLinks){
				link = link.trim();
				if(link.startsWith("/")){
					imageURL = new StringBuilder("http://empire.apc.com").append(link).toString();
				}else{
					imageURL = new StringBuilder("http://empire.apc.com/resource/images/products/200/").append(link).toString();
				}
				Bitmap bitmap = UPSSelectorUtil.getBitMap(imageURL);
				images.add(bitmap);
			}
			
			
	        return images;
		}
		
		protected void onPostExecute(List<Bitmap> result) {
			if(null!=result){
				ImageAdapter adapter = new ImageAdapter();
				adapter.setContext(DetailsActivity.this);
				adapter.setImages(result);
				imgGallery.setAdapter(adapter);
			}
	    }
    } 
	
}
