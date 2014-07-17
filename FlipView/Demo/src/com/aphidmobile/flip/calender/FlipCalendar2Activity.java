/*
Copyright 2012 Aphid Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.aphidmobile.flip.calender;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flip.FlipViewController.ViewFlipListener;
import com.aphidmobile.flipview.demo.R;

public class FlipCalendar2Activity extends Activity {
	public static final String TAG = FlipCalendar2Activity.class
			.getSimpleName();

	LayoutInflater mLayoutInflater;
	/** view content */
	LinearLayout calender_content;
	
	/**Calender views*/
	FlipViewController flip_big;
	LinearLayout flip_small_content;
	FlipViewController flip_small1;
	FlipViewController flip_small2;

	Bitmap bmpL,bmpR,nextBmpL,nextBmpR;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filp_calender);

		calender_content = (LinearLayout) findViewById(R.id.calender_contnet);
		mLayoutInflater = LayoutInflater.from(this);
		View calenderView = mLayoutInflater.inflate(R.layout.calender2_mail, null);
		flip_big = (FlipViewController) calenderView .findViewById(R.id.flip_big);
		flip_small_content = (LinearLayout) calenderView .findViewById(R.id.flip_small_content);
		flip_small1 = (FlipViewController) calenderView .findViewById(R.id.flip_small1);
		flip_small2 = (FlipViewController) calenderView .findViewById(R.id.flip_small2);
		flip_small_content.setVisibility(View.INVISIBLE);

		bmpL= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		bmpR= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		nextBmpL= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		nextBmpR= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		
		
		initFlipBig(flip_big, bmpL, bmpR,nextBmpL,nextBmpR);
		initFlip(flip_small1, bmpL, nextBmpL);
		initFlip(flip_small2, bmpR, nextBmpR);

		flip_small1.setSelection(1);
		flip_small2.setSelection(1);
		flip_big.setSelection(1);

		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				flip_big.onAutoEvent(true);
			}
		}, 3000);

		addmFlipViewController(flip_big);
		addmFlipViewController(flip_small1);
		addmFlipViewController(flip_small2);
		addmFlipViewController(flip_big);
		addmFlipViewController(flip_small1);
		addmFlipViewController(flip_small2);
		
		calender_content.addView(calenderView);
		setTitle(R.string.activity_title);

	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				FlipViewController mFlipViewController = (FlipViewController) msg.obj;
				if (msg.obj.equals(flip_small2)) {
					mFlipViewController.onAutoEvent(true);
				} else if (msg.obj.equals(flip_big)) {
					if (mFlipViewController.getOrientation() == mFlipViewController.HORIZONTAL) {
						flip_big.onAutoEvent(true);
					} else {
						flip_big.onAutoEvent(false);
					}
				} else {
					mFlipViewController.onAutoEvent(false);
				}
			}
			super.handleMessage(msg);
		}
	};

	int curent = 0;
	List<FlipViewController> mFlipViewControllers = new ArrayList<FlipViewController>();

	ViewFlipListener mViewFlipListener = new ViewFlipListener() {

		@Override
		public void onViewFlipped(FlipViewController mFlipViewController,
				View view, int position) {
			if (mFlipViewController.equals(flip_big)) {// 左到右 和下到上 交替
				flip_big.setVisibility(View.INVISIBLE);
				flip_small_content.setVisibility(View.VISIBLE);
				if (flip_big.getOrientation() == FlipViewController.HORIZONTAL) {
					flip_big.setOrientation(FlipViewController.VERTICAL);
					flip_small1.setSelection(0);
					flip_small2.setSelection(1);
				} else {
					flip_big.setOrientation(FlipViewController.HORIZONTAL);
					flip_small1.setSelection(0);
					flip_small2.setSelection(1);
				}
			} else if (mFlipViewController.equals(flip_small2)) {
				flip_big.setVisibility(View.VISIBLE);
				flip_small_content.setVisibility(View.INVISIBLE);
			}
			curent++;
			if (curent >= mFlipViewControllers.size()) {
				curent = 0;
			}
			Message message = mHandler.obtainMessage();
			if (curent < mFlipViewControllers.size()) {
				message.obj = mFlipViewControllers.get(curent);
			}
			mHandler.sendMessageDelayed(message, 2000);
		}
	};

	private void addmFlipViewController(FlipViewController mFlipViewController) {
		mFlipViewControllers.add(mFlipViewController);
		mFlipViewController.setOnViewFlipListener(mViewFlipListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		flip_big.onResume();
		flip_small1.onResume();
		flip_small2.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		flip_big.onPause();
		flip_small1.onPause();
		flip_small2.onPause();
	}

	public void initFlip(FlipViewController flipView, final Bitmap item1,
			final Bitmap item2) {

		flipView.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return 2;// Integer.MAX_VALUE;
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = mLayoutInflater.inflate(
							R.layout.calender2_small_item, null);
				}
				if (position % 2 == 0) {
					((ImageView) convertView).setImageBitmap(item1);
				} else {
					((ImageView) convertView).setImageBitmap(item1);
				}
				return convertView;
			}
		});

	}

	public void initFlipBig(FlipViewController flipView, final Bitmap item1,
			final Bitmap item2, final Bitmap item3,
			final Bitmap item4) {

		flipView.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return 2;// Integer.MAX_VALUE;
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = mLayoutInflater.inflate(
							R.layout.calender2_big_item, null);
				}
				if (position % 2 == 0) {
					((ImageView) convertView.findViewById(R.id.iv1))
							.setImageBitmap(item1);
					((ImageView) convertView.findViewById(R.id.iv2))
							.setImageBitmap(item2);
				} else {
					((ImageView) convertView.findViewById(R.id.iv1))
							.setImageBitmap(item3);
					((ImageView) convertView.findViewById(R.id.iv2))
							.setImageBitmap(item4);
				}
				return convertView;
			}
		});

	}
}
