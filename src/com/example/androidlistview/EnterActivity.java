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
import android.widget.Toast;

public class EnterActivity extends Activity {
	private GridView mGview;
	// 存放所有选择的照片
	private ArrayList<String> allSelectedPicture = new ArrayList<String>();
	private static final int REQUESTCODE = 0x000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inter);
		mGview = (GridView) findViewById(R.id.gridv);

		mGview.setAdapter(new baseGridAda());
	}

	class baseGridAda extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allSelectedPicture.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(EnterActivity.this, R.layout.item_, null);
				viewHolder.imgV = (ImageView) convertView.findViewById(R.id.id_item_image);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (allSelectedPicture.size() == position) {

				viewHolder.imgV.setImageBitmap(
						BitmapFactory.decodeResource(EnterActivity.this.getResources(), R.drawable.picadd));
				viewHolder.imgV.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), position + "-" + "", 5).show();
						SelectPic();
					}
				});
			}

			return convertView;
		}

		public void SelectPic() {

			Intent intent = new Intent(EnterActivity.this, SelectPicActivity.class);

			Bundle bundle = new Bundle();
			bundle.putStringArrayList("allSelectedPicture", allSelectedPicture);
			intent.putExtras(bundle);

			startActivityForResult(intent, REQUESTCODE);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {

		}
	}

	class ViewHolder {
		private ImageView imgV;
	}
}
