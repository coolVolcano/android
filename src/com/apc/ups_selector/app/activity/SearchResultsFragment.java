package com.apc.ups_selector.app.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apc.ups_selector.app.R;
import com.apc.ups_selector.app.entity.UPSBean;
import com.apc.ups_selector.app.util.UPSSelectorUtil;
import com.apc.ups_selector.app.ws.WebServiceHelper;

public class SearchResultsFragment extends ListFragment{
	
	private UPSBean bestPriceUPS;
	private UPSBean bestValueUPS;
	private UPSBean bestPerformanceUPS;
	private double totalWatts;
	private int runtime;
	private double maxWatts;
	
	int mCurCheckPosition = 0;  
    int mShownCheckPosition = -1;  
    boolean mDualPane;
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        
        super.onActivityCreated(savedInstanceState);
        
        ArrayList<UPSBean> upsList = getActivity().getIntent().getParcelableArrayListExtra("com.apc.ups_selector.app.activity.Results");
        
        totalWatts = getActivity().getIntent().getDoubleExtra("watts", 0.0);
        runtime = getActivity().getIntent().getIntExtra("runtime", 0);
        maxWatts = getActivity().getIntent().getDoubleExtra("maxWatts", 0);
        
        getSpecificUPS(upsList);
        
        String[] resultTypes = {"Best Price: " + UPSSelectorUtil.getCleanPartNum(bestPriceUPS.getPartNum()), "Best Value: " + UPSSelectorUtil.getCleanPartNum(bestValueUPS.getPartNum()), "Best Performace: " + UPSSelectorUtil.getCleanPartNum(bestPerformanceUPS.getPartNum())};
        
        // Populate list with our static array of titles.
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,resultTypes));
        
        this.getListView().setSelector(R.drawable.se_list_item);

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            if(mCurCheckPosition==0){
            	showDetails(mCurCheckPosition, bestPriceUPS);
            }else if(mCurCheckPosition==1){
            	showDetails(mCurCheckPosition, bestValueUPS);
            }else if(mCurCheckPosition==2){
            	showDetails(mCurCheckPosition, bestPerformanceUPS);
            }
            
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);  
        outState.putInt("shownChoice", mShownCheckPosition); 
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	if(position==0){
        	showDetails(position, bestPriceUPS);
        }else if(position==1){
        	showDetails(position, bestValueUPS);
        }else if(position==2){
        	showDetails(position, bestPerformanceUPS);
        }
    }
    
    private void showDetails(int index, UPSBean ups){
    	
        mCurCheckPosition = index;
        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            DetailsFragment details = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailsFragment.newInstance(index,ups);
                
                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
        	Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("index", index);
//            if(index==0){
//            	intent.putExtra("com.apc.ups_selector.app.activity.UPS", bestPriceUPS);
//            }else if(index==1){
//            	intent.putExtra("com.apc.ups_selector.app.activity.UPS", bestValueUPS);
//            }else if(index==2){
//            	intent.putExtra("com.apc.ups_selector.app.activity.UPS", bestPerformanceUPS);
//            }
            intent.putExtra("com.apc.ups_selector.app.activity.UPS", ups);
            startActivity(intent);
        }
    }
    
    private void getSpecificUPS(ArrayList<UPSBean> upsList){
    	
    	double value_watts;
    	double value_runtime;
    	double perf_watts;
    	double perf_runtime;
    	double value_target_vah;
    	double value_min_watts;
    	double perf_target_vah;
    	double perf_min_watts;
    	
    	if(upsList.size()>0){
    		
    		//get the best price ups
    		bestPriceUPS = upsList.get(0);
    		bestValueUPS = bestPriceUPS; //default value
    		bestPerformanceUPS = bestPriceUPS; //default value
    		
    		//get the best value ups
    		if(maxWatts < 9000)
        		value_watts = totalWatts * 1.2;
        	else
        		value_watts = totalWatts * 1.1;
        	
        	value_runtime = runtime * 1.2;
        	value_target_vah = UPSSelectorUtil.getTargetVAH(value_watts, value_runtime);
        	
        	if(value_watts < bestPriceUPS.getPowerRated())
        		value_min_watts = bestPriceUPS.getPowerRated();
        	else
        		value_min_watts = value_watts;
        	
			ArrayList<UPSBean> tempList = new ArrayList<UPSBean>();
    		for(UPSBean ups : upsList){
    			if(!ups.getPartNum().equals(bestPriceUPS.getPartNum()) && ups.getPowerRated()>=value_min_watts && ups.getVah() >= value_target_vah){
    				tempList.add(ups);
    			}
    		}
    		
    		if(tempList.size()>0){
    			Collections.sort(tempList, new Comparator<UPSBean>(){
    				@Override
    				public int compare(UPSBean arg0, UPSBean arg1) {
    					
    					if(arg0.getErpValue() > arg1.getErpValue()){
    						return 1;
    					}else if(arg0.getErpValue() == arg1.getErpValue()){
    						return 0;
    					}else{
    						return -1;
    					}
    				}
        		});
        		bestValueUPS = tempList.get(0);
    		}else{
    			bestValueUPS = bestPriceUPS;
    		}
    		
    		//get the best performance ups
    		if(maxWatts < 1000)
    			perf_watts = totalWatts * 1.5;
    		else if(maxWatts < 9000)
    			perf_watts = totalWatts * 1.2;
    		else
    			perf_watts = totalWatts * 1.1;
    		
    		if(runtime <= 30)
    			perf_runtime = runtime * 1.5;
    		else
    			perf_runtime = runtime * 1.2;
    		
    		perf_target_vah = UPSSelectorUtil.getTargetVAH(perf_watts, perf_runtime);
    		
    		if(perf_watts < bestValueUPS.getPowerRated())
    			perf_min_watts = bestValueUPS.getPowerRated();
    		else
    			perf_min_watts = perf_watts;
    		
    		ArrayList<UPSBean> tempList2 = new ArrayList<UPSBean>();
    		for(UPSBean ups : upsList){
    			if(!ups.getPartNum().equals(bestPriceUPS.getPartNum()) && !ups.getPartNum().equals(bestValueUPS.getPartNum()) && ups.getPowerRated()>=perf_min_watts && ups.getVah() >= perf_target_vah){
    				tempList2.add(ups);
    			}
    		}
    		
    		if(tempList2.size()>0){
    			Collections.sort(tempList2, new Comparator<UPSBean>(){
    				@Override
    				public int compare(UPSBean arg0, UPSBean arg1) {
    					
    					if(arg0.getErpPerf() > arg1.getErpPerf()){
    						return 1;
    					}else if(arg0.getErpPerf() == arg1.getErpPerf()){
    						return 0;
    					}else{
    						return -1;
    					}
    				}
        		});
    			bestPerformanceUPS = tempList2.get(0);
    		}else{
    			bestPerformanceUPS = bestValueUPS;
    		}
    		
    	}
    	
    	new GetImageSrcAndFeaturesTask().execute();
    }
    
    private class GetImageSrcAndFeaturesTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
				
			bestPriceUPS.setImageSrc(WebServiceHelper.getUPSImageLink(bestPriceUPS.getSku()));
	    	bestPriceUPS.setFeatures(WebServiceHelper.getFeatures(bestPriceUPS.getSku()));
	    	bestValueUPS.setImageSrc(WebServiceHelper.getUPSImageLink(bestValueUPS.getSku()));
	    	bestValueUPS.setFeatures(WebServiceHelper.getFeatures(bestValueUPS.getSku()));
	    	bestPerformanceUPS.setImageSrc(WebServiceHelper.getUPSImageLink(bestPerformanceUPS.getSku()));
	    	bestPerformanceUPS.setFeatures(WebServiceHelper.getFeatures(bestPerformanceUPS.getSku()));
			return null;
		}
		
    }

}
