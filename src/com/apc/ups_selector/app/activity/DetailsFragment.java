package com.apc.ups_selector.app.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.apc.ups_selector.app.R;
import com.apc.ups_selector.app.entity.UPSBean;
import com.apc.ups_selector.app.util.ImageAdapter;
import com.apc.ups_selector.app.util.UPSSelectorUtil;

public class DetailsFragment extends Fragment {
	
	private TextView txtUPSModel;
	private TextView txtUPSPrice;
	private TextView txtUPSRuntime;
	private TextView txtUPSSku;
	private ListView listFeatures;
	private Gallery imgGallery;
	private UPSBean ups;
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        txtUPSModel = (TextView)this.getActivity().findViewById(R.id.txtUPSModel);
        txtUPSPrice = (TextView)this.getActivity().findViewById(R.id.txtUPSPrice);
        txtUPSRuntime = (TextView)this.getActivity().findViewById(R.id.txtUPSRuntime);
        txtUPSSku = (TextView)this.getActivity().findViewById(R.id.txtUPSSku);
        listFeatures = (ListView)this.getActivity().findViewById(R.id.listFeatures);
        imgGallery = (Gallery)this.getActivity().findViewById(R.id.imgGallery);
        
        ups = (UPSBean)getArguments().getParcelable("com.apc.ups_selector.app.activity.UPS");
        
        txtUPSSku.setText(UPSSelectorUtil.getCleanPartNum(ups.getPartNum()));
        txtUPSPrice.setText(new StringBuilder("$").append(ups.getPrice()).toString());
        txtUPSRuntime.setText(new StringBuilder(String.valueOf(ups.getRuntime())).append(" ").append(getResources().getString(R.string.minutes)).toString());
        txtUPSModel.setText(ups.getModel());
        
        if(null!=ups.getFeatures()){
			
			String f = ups.getFeatures().get(0).replace("[", "");
			f = f.replace("]", "");
			String[] features =  StringUtils.split(f, ",");
			List<String> featureList = Arrays.asList(features);
			
			ListAdapter adapter = new ArrayAdapter<String>(this.getActivity(),R.layout.list_item,featureList);
		
			listFeatures.setAdapter(adapter);
		}
        
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
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	/**     * Create a new instance of DetailsFragment, initialized to     * show the text at 'index'.     */  
    public static DetailsFragment newInstance(int index, UPSBean ups) {  
        DetailsFragment f = new DetailsFragment();  
        // Supply index input as an argument.          
        Bundle args = new Bundle();  
        args.putInt("index", index);  
        args.putParcelable("com.apc.ups_selector.app.activity.UPS", ups);
        f.setArguments(args);  
        return f;  
    }  
    
    public int getShownIndex() {
        return getArguments().getInt("index", 0);
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
				adapter.setContext(DetailsFragment.this.getActivity());
				adapter.setImages(result);
				imgGallery.setAdapter(adapter);
			}
	    }
    } 
    
}
