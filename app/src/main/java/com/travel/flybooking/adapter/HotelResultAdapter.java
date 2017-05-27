package com.travel.flybooking.adapter;

import java.util.ArrayList;
import java.util.Locale;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.support.ImageDownloader;
import com.travel.flybooking.HotelResultActivity;
import com.travel.flybooking.R;
import com.travel.model.HotelResultItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class HotelResultAdapter extends BaseAdapter{

	final ImageDownloader imageDownloader = new ImageDownloader();
	private Context context;
	private ArrayList<HotelResultItem> hotelResultItem;
	String strSessionId;
	public HotelResultAdapter(Context context, ArrayList<HotelResultItem> hotelResultItem, String strSessionId){
		this.context = context;
		this.hotelResultItem = hotelResultItem;
		this.strSessionId = strSessionId;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return hotelResultItem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return hotelResultItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_search_result_hotel, null);
            
        }
		
		final HotelResultItem hItem = hotelResultItem.get(position);
		
		ImageView iv = (ImageView) convertView.findViewById(R.id.iv_hotel_logo);
		if(!hItem.getHotelThumbImage().contains("no_image")){
			Bitmap bmp = HotelResultActivity.getBitmapFromMemCache(hItem.getHotelThumbImage());
			iv.setImageBitmap(bmp);
        }
		else
		{
			iv.setImageResource(R.mipmap.ic_no_image);
		}
		
		
		((TextView) convertView.findViewById(R.id.tv_hotel_name)).setText(hItem.getHotelName());
        ((TextView) convertView.findViewById(R.id.tv_place)).setText(hItem.getHotelAddress().equals("") ? HotelResultActivity.strCity : hItem.getHotelAddress());
        
        String price = String.format(new Locale("en"), "%.3f", Double.parseDouble(hItem.getDisplayRate()));
        
        ((TextView) convertView.findViewById(R.id.tv_hotel_cost)).setText(CommonFunctions.strCurrency+" "+price);
        ((RatingBar) convertView.findViewById(R.id.rb_hotel_ratng)).setRating(hItem.getHotelRating());
        
        if(hItem.getBoardTypes().toLowerCase().contains("breakfast") ||
        		hItem.getBoardTypes().toLowerCase().contains("الافطار")  ||
        		hItem.getBoardTypes().toLowerCase().contains("افطار"))
        	((LinearLayout) convertView.findViewById(R.id.ll_breakfast_included)).setVisibility(View.VISIBLE);
        else
        	((LinearLayout) convertView.findViewById(R.id.ll_breakfast_included)).setVisibility(View.GONE);
        
//        LinearLayout llMap = (LinearLayout) convertView.findViewById(R.id.ll_map);
//        llMap.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent marker = new Intent(context, MarkeActivity.class);
//				marker.putExtra("HotelName", 	hItem.strHotelName);
//				marker.putExtra("HotelAddress", hItem.strHotelAddress);
//				marker.putExtra("Latitude", 	hItem.strHotelLattitude);
//				marker.putExtra("Langitude", 	hItem.strHotelLongitude);
//				context.startActivity(marker);
//				
//			}
//		});
        
//        if(hItem.strHotelLattitude == 0.0 || hItem.strHotelLongitude == 0.0)
//        	llMap.setVisibility(View.GONE);
//        else
//        	llMap.setVisibility(View.VISIBLE);
        
        return convertView;
	}

}
