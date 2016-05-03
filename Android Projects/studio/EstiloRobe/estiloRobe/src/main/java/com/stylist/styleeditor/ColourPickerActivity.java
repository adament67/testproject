package com.stylist.styleeditor;

import java.util.ArrayList;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.constants.BaseActivity;

import com.stylist.R;
import com.stylist.lookbook.LookBookActivity;
import com.stylist.lookbook.LookBookItemActivity;

public class ColourPickerActivity extends BaseActivity {
	ListView colorList;
	 public static Activity ctx;
	private ArrayList<ArrayList<ColourPicker>> ColourPickerArray=new ArrayList<ArrayList<ColourPicker>>();
private ArrayList<String> ColourRangeName=new ArrayList<String>();
	private ColourPicker ColourPickerObject;
	ArrayList<ColourPicker> tempColourPickerArrayN1 = new ArrayList<ColourPicker>();
	ArrayList<ColourPicker> tempColourPickerArrayN2 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN3 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN4 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN5 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN6 = new ArrayList<ColourPicker>();

	
	ArrayList<ColourPicker> tempColourPickerArrayN7 = new ArrayList<ColourPicker>();

	
	ArrayList<ColourPicker> tempColourPickerArrayN8 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN9 = new ArrayList<ColourPicker>();

	
	ArrayList<ColourPicker> tempColourPickerArrayN10 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN11 = new ArrayList<ColourPicker>();
	ArrayList<ColourPicker> tempColourPickerArrayN12 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN13 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN14 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN15 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN16 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN17 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN18 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN19 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN20 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN21 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN22 = new ArrayList<ColourPicker>();

	ArrayList<ColourPicker> tempColourPickerArrayN23 = new ArrayList<ColourPicker>();
	ArrayList<ColourPicker> tempColourPickerArrayN24 = new ArrayList<ColourPicker>();
	ArrayList<ColourPicker> tempColourPickerArrayN = new ArrayList<ColourPicker>();
	
	ArrayList<ColourPicker> tempColourPickerArrayN26 = new ArrayList<ColourPicker>();
	ArrayList<ColourPicker> tempColourPickerArrayN27 = new ArrayList<ColourPicker>();
	ArrayList<ColourPicker> tempColourPickerArrayN28 = new ArrayList<ColourPicker>();
	

	public void onStart() {
		super.onStart();
		//.onStartSession(this,
		//		getResources().getString(R.string.flurryKey));
	}

	public void onStop() {
		super.onStop();
		//.onEndSession(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_colour_picker);
		colorList = (ListView) findViewById(R.id.colorList);
		setHeading("SKIN COLOR");
		backClick();
		ctx=this;
		intializeColorArray();
		System.out.println("ColourPickerArray===size===="+ColourPickerArray.size());
	
	}

	private class ColourPickerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return ColourPickerArray.size();
		}

		@Override
		public Object getItem(int arg0) {
			return ColourPickerArray.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}


		
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			
			
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(ColourPickerActivity.this,
						R.layout.grid_component, null);
		
				holder.colorname=(TextView)convertView.findViewById(R.id.colorname);
				holder.imageView1 = (ImageView) convertView
						.findViewById(R.id.color_image1);
				holder.imageView2 = (ImageView) convertView
						.findViewById(R.id.color_image2);
				holder.imageView3 = (ImageView) convertView
						.findViewById(R.id.color_image3);
				holder.imageView4 = (ImageView) convertView
						.findViewById(R.id.color_image4);
				holder.imageView5 = (ImageView) convertView
						.findViewById(R.id.color_image5);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
	
			
			
		ArrayList<ColourPicker> temp = ColourPickerArray.get(position);
	
			setListColor(holder.imageView1,temp.get(0));
			setListColor(holder.imageView2, temp.get(1));
			setListColor(holder.imageView3, temp.get(2));
			setListColor(holder.imageView4, temp.get(3));
			setListColor(holder.imageView5, temp.get(4));
			
			holder.colorname.setText(ColourRangeName.get(position));

			holder.imageView1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					backtoActivity(position, 0);
				}
			});
			holder.imageView2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					backtoActivity(position, 1);
				}
			});
			holder.imageView3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					backtoActivity(position, 2);
				}
			});
			holder.imageView4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					backtoActivity(position, 3);
				}
			});
			holder.imageView5.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					backtoActivity(position, 4);
				}
			});
			return convertView;
		}

		private void setListColor(ImageView imageView, ColourPicker ColourPicker) {

			int r = ColourPicker.getRed();
			int g = ColourPicker.getGreen();
			int b = ColourPicker.getBlue();
			int alpha = ColourPicker.getAlpha();
			final Intent intent = new Intent();
			intent.putExtra("r", r);
			intent.putExtra("g", g);
			intent.putExtra("b", b);
			intent.putExtra("alpha", alpha);
			if (imageView != null) {
				imageView.setBackgroundColor(Color.argb(alpha, r, g, b));
			} else {
				// do nothing
			}
		}

		private void backtoActivity(int position, int index) {
			ColourPicker ColourPicker = ColourPickerArray.get(position).get(
					index);
			Intent intent = new Intent();
			intent.putExtra("r", ColourPicker.getRed());
			intent.putExtra("g", ColourPicker.getGreen());
			intent.putExtra("b", ColourPicker.getBlue());
			intent.putExtra("alpha", ColourPicker.getAlpha());
			setResult(RESULT_OK, intent);
			finish();
		}
		private class ViewHolder {
			ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
			TextView colorname;
			
		}
	}

	private void intializeColorArray() {
		ColourRangeName.add("Pastel");
		ColourRangeName.add("Ocean");
		ColourRangeName.add("Beauty");
		ColourRangeName.add("Stones");
		ColourRangeName.add("Autumn");
		ColourRangeName.add("Natural");
		ColourRangeName.add("Candy");
		ColourRangeName.add("Softy");
		ColourRangeName.add("Earth");
		ColourRangeName.add("Safari");
		ColourRangeName.add("Beach");
		ColourRangeName.add("Skyline");
		ColourRangeName.add("Flowers");
		ColourRangeName.add("Plants");
		ColourRangeName.add("Summer Tree");
		ColourRangeName.add("Cloud");
		ColourRangeName.add("Deep Color");
		ColourRangeName.add("Sky");
		ColourRangeName.add("Waves");
		ColourRangeName.add("Lilac");
		ColourRangeName.add("Sweet");
		ColourRangeName.add("Lagoon");
		ColourRangeName.add("Lavender");
		ColourRangeName.add("Bath");
		ColourRangeName.add("Sweet Home");
		ColourRangeName.add("Coral");
		ColourRangeName.add("Pink");
		ColourRangeName.add("Blue");

		/* 1 */
		
		
		tempColourPickerArrayN1.add(new ColourPicker(205,205,205, 80));
		tempColourPickerArrayN1.add(new ColourPicker(201,226,201, 80));
		tempColourPickerArrayN1.add(new ColourPicker(255,201,187, 80));
		tempColourPickerArrayN1.add(new ColourPicker(168,216,214, 80));
		tempColourPickerArrayN1.add(new ColourPicker(224,202,176, 80));
		ColourPickerArray.add(tempColourPickerArrayN1);
		
		
		/* 2 */
		tempColourPickerArrayN2.add(new ColourPicker(106,157,205, 80));
		tempColourPickerArrayN2.add(new ColourPicker(36,82,107, 80));
		tempColourPickerArrayN2.add(new ColourPicker(63,158,102, 80));
		tempColourPickerArrayN2.add(new ColourPicker(34,102,103, 80));
		tempColourPickerArrayN2.add(new ColourPicker(152,213,221, 80));
		ColourPickerArray.add(tempColourPickerArrayN2);
		
		

		/* 3 */
		tempColourPickerArrayN3.add(new ColourPicker(227,189,171, 80));
		tempColourPickerArrayN3.add(new ColourPicker(215,158,136, 80));
		tempColourPickerArrayN3.add(new ColourPicker(231,214,182, 80));
		tempColourPickerArrayN3.add(new ColourPicker(212,134,125, 80));
		tempColourPickerArrayN3.add(new ColourPicker(223,169,159, 80));
		ColourPickerArray.add(tempColourPickerArrayN3);
				
		
		/* 4 */
		tempColourPickerArrayN4.add(new ColourPicker(128,122,113, 80));
		tempColourPickerArrayN4.add(new ColourPicker(202,199,194, 80));
		tempColourPickerArrayN4.add(new ColourPicker(243,239,232, 80));
		tempColourPickerArrayN4.add(new ColourPicker(135,108,66, 80));
		tempColourPickerArrayN4.add(new ColourPicker(214,197,167, 80));
		ColourPickerArray.add(tempColourPickerArrayN4);
		
		/* 5 */
		tempColourPickerArrayN5.add(new ColourPicker(253,220,176, 80));
		tempColourPickerArrayN5.add(new ColourPicker(196,155,155, 80));
		tempColourPickerArrayN5.add(new ColourPicker(154,146,99, 80));
		tempColourPickerArrayN5.add(new ColourPicker(204,209,168, 80));
		tempColourPickerArrayN5.add(new ColourPicker(214,146,120, 80));
		ColourPickerArray.add(tempColourPickerArrayN5);

		/* 6 */

		tempColourPickerArrayN6.add(new ColourPicker(168,103,56, 80));
		tempColourPickerArrayN6.add(new ColourPicker(195,159,75, 80));
		tempColourPickerArrayN6.add(new ColourPicker(122,138,46, 80));
		tempColourPickerArrayN6.add(new ColourPicker(170,186,103, 80));
		tempColourPickerArrayN6.add(new ColourPicker(43,107,55, 80));
		ColourPickerArray.add(tempColourPickerArrayN6);
		
		/* 7 */
		tempColourPickerArrayN7.add(new ColourPicker(143,183,143, 80));
		tempColourPickerArrayN7.add(new ColourPicker(223,171,159, 80));
		tempColourPickerArrayN7.add(new ColourPicker(235,218,194, 80));
		tempColourPickerArrayN7.add(new ColourPicker(205,186,232, 80));
		tempColourPickerArrayN7.add( new ColourPicker(163,212,224, 80));
		ColourPickerArray.add(tempColourPickerArrayN);
		

		/* 8 */
		tempColourPickerArrayN8.add(new ColourPicker(190,163,193, 80));
		tempColourPickerArrayN8.add(new ColourPicker(159,168,223, 80));
		tempColourPickerArrayN8.add(new ColourPicker(194,233,235, 80));
		tempColourPickerArrayN8.add(new ColourPicker(224,182,231, 80));
		tempColourPickerArrayN8.add(new ColourPicker(184,167,226, 80));
		ColourPickerArray.add(tempColourPickerArrayN8);
		

		/*9*/
		tempColourPickerArrayN9.add(new ColourPicker(178,107,86, 80));
		tempColourPickerArrayN9.add(new ColourPicker(191,136,106, 80));
		tempColourPickerArrayN9.add(new ColourPicker(206,177,118, 80));
		tempColourPickerArrayN9.add(new ColourPicker(210,200,121, 80));
		tempColourPickerArrayN9.add(new ColourPicker(130,84,43, 80));
		ColourPickerArray.add(tempColourPickerArrayN9);
		

		/* 10 */
		tempColourPickerArrayN11.add(new ColourPicker(158,109,76, 80));
		tempColourPickerArrayN11.add(new ColourPicker(170,146,107, 80));
		tempColourPickerArrayN11.add(new ColourPicker(216,199,128, 80));
		tempColourPickerArrayN11.add(new ColourPicker(168,161,134, 80));
		tempColourPickerArrayN11.add(new ColourPicker(191,153,107, 80));
		ColourPickerArray.add(tempColourPickerArrayN11);
	

		/* 11 */
		tempColourPickerArrayN10.add(new ColourPicker(249,251,203, 80));
		tempColourPickerArrayN10.add(new ColourPicker(245,200,157, 80));
		tempColourPickerArrayN10.add(new ColourPicker(204,160,111, 80));
		tempColourPickerArrayN10.add(new ColourPicker(107,117,99, 80));
		tempColourPickerArrayN10.add(new ColourPicker(126,179,164, 80));
		ColourPickerArray.add(tempColourPickerArrayN10);
		

		/* 12 */
		tempColourPickerArrayN12.add(new ColourPicker(250,240,180, 80));
		tempColourPickerArrayN12.add(new ColourPicker(249,209,134, 80));
		tempColourPickerArrayN12.add(new ColourPicker(240,188,135, 80));
		tempColourPickerArrayN12.add(new ColourPicker(177,167,184, 80));
		tempColourPickerArrayN12.add(new ColourPicker(213,208,217, 80));
		ColourPickerArray.add(tempColourPickerArrayN12);
		

		/* 13 */
		tempColourPickerArrayN13.add(new ColourPicker(252,226,142, 80));
		tempColourPickerArrayN13.add(new ColourPicker(248,207,130, 80));
		tempColourPickerArrayN13.add(new ColourPicker(201,100,94, 80));
		tempColourPickerArrayN13.add(new ColourPicker(135,150,71, 80));
		tempColourPickerArrayN13.add(new ColourPicker(122,41,72, 80));
		ColourPickerArray.add(tempColourPickerArrayN13);
		

		/* 14 */
		tempColourPickerArrayN.clear();
		tempColourPickerArrayN14.add(new ColourPicker(244,245,169, 80));
		tempColourPickerArrayN14.add(new ColourPicker(230,224,119, 80));
		tempColourPickerArrayN14.add(new ColourPicker(214,201,105, 80));
		tempColourPickerArrayN14.add(new ColourPicker(87,145,97, 80));
		tempColourPickerArrayN14.add(new ColourPicker(171,208,160, 80));
		ColourPickerArray.add(tempColourPickerArrayN14);
		

		/* 15 */
		tempColourPickerArrayN15.add(new ColourPicker(211,231,232, 80));
		tempColourPickerArrayN15.add(new ColourPicker(238,242,176, 80));
		tempColourPickerArrayN15.add(new ColourPicker(185,195,135, 80));
		tempColourPickerArrayN15.add(new ColourPicker(206,230,166, 80));
		tempColourPickerArrayN15.add(new ColourPicker(163,199,145, 80));
		ColourPickerArray.add(tempColourPickerArrayN15);
		

		

		/* 16 */
		tempColourPickerArrayN16.add(new ColourPicker(105,161,121, 80));
		tempColourPickerArrayN16.add(new ColourPicker(185,209,152, 80));
		tempColourPickerArrayN16.add(new ColourPicker(219,232,179, 80));
		tempColourPickerArrayN16.add(new ColourPicker(227,220,215, 80));
		tempColourPickerArrayN16.add(new ColourPicker(128,181,165, 80));
		ColourPickerArray.add(tempColourPickerArrayN16);
		

		/* 17 */
		tempColourPickerArrayN17.add(new ColourPicker(110,89,91, 80));
		tempColourPickerArrayN17.add(new ColourPicker(168,166,152, 80));
		tempColourPickerArrayN17.add(new ColourPicker(209,200,180, 80));
		tempColourPickerArrayN17.add(new ColourPicker(128,113,111, 80));
		tempColourPickerArrayN17.add(new ColourPicker(55,50,61, 80));
		ColourPickerArray.add(tempColourPickerArrayN17);
		

		/* 18 */
		tempColourPickerArrayN18.add(new ColourPicker(168,166,177, 80));
		tempColourPickerArrayN18.add(new ColourPicker(127,120,130, 80));
		tempColourPickerArrayN18.add(new ColourPicker(189,160,155, 80));
		tempColourPickerArrayN18.add(new ColourPicker(193,191,194, 80));
		tempColourPickerArrayN18.add(new ColourPicker(150,156,169, 80));
		ColourPickerArray.add(tempColourPickerArrayN18);
		

		/* 19 */
		tempColourPickerArrayN19.add(new ColourPicker(166,143,159, 80));
		tempColourPickerArrayN19.add(new ColourPicker(103,62,72, 80));
		tempColourPickerArrayN19.add(new ColourPicker(179,178,196, 80));
		tempColourPickerArrayN19.add(new ColourPicker(225,220,213, 80));
		tempColourPickerArrayN19.add(new ColourPicker(196,188,188, 80));
		ColourPickerArray.add(tempColourPickerArrayN19);
			
			
		/* 20 */
		tempColourPickerArrayN20.add(new ColourPicker(249,208,232, 80));
		tempColourPickerArrayN20.add(new ColourPicker(233,202,204, 80));
		tempColourPickerArrayN20.add(new ColourPicker(175,108,125, 80));
		tempColourPickerArrayN20.add(new ColourPicker(152,91,120, 80));
		tempColourPickerArrayN20.add(new ColourPicker(229,227,227, 80));
		ColourPickerArray.add(tempColourPickerArrayN20);
		


		/* 21 */
		tempColourPickerArrayN21.add(new ColourPicker(233,180,185, 80));
		tempColourPickerArrayN21.add(new ColourPicker(204,104,98, 80));
		tempColourPickerArrayN21.add(new ColourPicker(170,60,59, 80));
		tempColourPickerArrayN21.add(new ColourPicker(233,120,86, 80));
		tempColourPickerArrayN21.add(new ColourPicker(253,212,194, 80));
		ColourPickerArray.add(tempColourPickerArrayN21);
		

		/* 22 */
		tempColourPickerArrayN22.add(new ColourPicker(245,220,213, 80));
		tempColourPickerArrayN22.add(new ColourPicker(184,221,207, 80));
		tempColourPickerArrayN22.add(new ColourPicker(174,208,209, 80));
		tempColourPickerArrayN22.add(new ColourPicker(174,217,180, 80));
		tempColourPickerArrayN22.add(new ColourPicker(234,232,172, 80));
		ColourPickerArray.add(tempColourPickerArrayN22);
		

		/* 23 */
		tempColourPickerArrayN23.add(new ColourPicker(240,188,184, 80));
		tempColourPickerArrayN23.add(new ColourPicker(166,147,166, 80));
		tempColourPickerArrayN23.add(new ColourPicker(99,51,63, 80));
		tempColourPickerArrayN23.add(new ColourPicker(135,124,123, 80));
		tempColourPickerArrayN23.add(new ColourPicker(198,197,201, 80));
		ColourPickerArray.add(tempColourPickerArrayN23);
		
		/* 24 */
		tempColourPickerArrayN24.add(new ColourPicker(209,222,216, 80));
		tempColourPickerArrayN24.add(new ColourPicker(182,214,212, 80));
		tempColourPickerArrayN24.add(new ColourPicker(168,222,218, 80));
		tempColourPickerArrayN24.add(new ColourPicker(64,158,166, 80));
		tempColourPickerArrayN24.add(new ColourPicker(160,183,196, 80));
		ColourPickerArray.add(tempColourPickerArrayN24);
		
		/* 25 */
		tempColourPickerArrayN.add(new ColourPicker(197,167,185, 80));
		tempColourPickerArrayN.add(new ColourPicker(185,189,202, 80));
		tempColourPickerArrayN.add(new ColourPicker(194,203,156, 80));
		tempColourPickerArrayN.add(new ColourPicker(142,174,179, 80));
		tempColourPickerArrayN.add(new ColourPicker(70,125,138, 80));
		ColourPickerArray.add(tempColourPickerArrayN);
		
		
		
		/* 26 */
		tempColourPickerArrayN26.add(new ColourPicker(234,118,105, 80));
		tempColourPickerArrayN26.add(new ColourPicker(245,179,157, 80));
		tempColourPickerArrayN26.add(new ColourPicker( 255, 190 ,148, 80));
		tempColourPickerArrayN26.add(new ColourPicker(255, 139, 140, 80));
		tempColourPickerArrayN26.add(new ColourPicker(255 ,138, 110, 80));
		ColourPickerArray.add(tempColourPickerArrayN26);
		
		
		/* 27 */
	
		tempColourPickerArrayN27.add(new ColourPicker(249, 187, 230, 80));
		tempColourPickerArrayN27.add(new ColourPicker(242 ,158, 200, 80));
		tempColourPickerArrayN27.add(new ColourPicker(238 ,115, 196, 80));
		tempColourPickerArrayN27.add(new ColourPicker( 244 ,83, 173, 80));
		tempColourPickerArrayN27.add(new ColourPicker(242 ,11 ,151 , 80));
		ColourPickerArray.add(tempColourPickerArrayN27);
		
		
		/* 28 */
		tempColourPickerArrayN28.add(new ColourPicker(186, 194, 255, 80));
		tempColourPickerArrayN28.add(new ColourPicker(78 ,145 ,253, 80));
		tempColourPickerArrayN28.add(new ColourPicker(44, 44 ,255, 80));
		tempColourPickerArrayN28.add(new ColourPicker( 2 ,41, 191, 80));
		tempColourPickerArrayN28.add(new ColourPicker( 0, 5, 159, 80));
		ColourPickerArray.add(tempColourPickerArrayN28);
		
		
		
ColourPickerAdapter ColourPickerAdapter = new ColourPickerAdapter();
		colorList.setAdapter(ColourPickerAdapter);
	}
	/*
	 * private void handleColorClick(ColourPicker ColourPicker) {
	 * colorSeekBar.setProgress(0); this.ColourPickerObject = ColourPicker; int
	 * r = ColourPicker.getRed(); int g = ColourPicker.getGreen(); int b =
	 * ColourPicker.getBlue(); int alpha = ColourPicker.getAlpha();
	 * imageView.setBackgroundColor(Color.argb(alpha, r, g, b));
	 * setColoronText(r, g, b); } private void setColoronText(int r , int g ,
	 * int b) { redTextView.setText("R:"+String.valueOf(r));
	 * greenTextView.setText("R:"+String.valueOf(g));
	 * blueTextView.setText("R:"+String.valueOf(b)); }
	 */
}