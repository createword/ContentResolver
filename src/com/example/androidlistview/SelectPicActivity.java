package com.example.androidlistview;

import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectPicActivity extends Activity {
	private GridView mGview;
	private GridView mGridView;
	// 存放所有选择的照片
	private ArrayList<String> existedPictureList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic);
		Bundle bundle = getIntent().getExtras();
		existedPictureList = bundle.getStringArrayList("allSelectedPicture");
		mGridView = (GridView) findViewById(R.id.gridView);
		existedPictureList.add("1");
		existedPictureList.add("1");
		mGridView.setAdapter(new GridAdapter());
	}

	class GridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return existedPictureList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return existedPictureList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(getApplication(), R.layout.item_, null);
				viewHolder = new ViewHolder();
				viewHolder.img = (ImageView) convertView.findViewById(R.id.id_item_image);
		//		viewHolder.txt = (TextView) convertView.findViewById(R.id.txt);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (position == 0) {
				viewHolder.img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
			} 
			return convertView;
		}

		class ViewHolder {
			private ImageView img;
			private TextView txt;
		}
	}
}
