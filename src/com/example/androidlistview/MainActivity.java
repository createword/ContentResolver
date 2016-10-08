package com.example.androidlistview;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.example.androidlistview.ImageLoader.Type;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private GridView gridView;
	private TextView all, count;
	private String firstImage;
	private ImageFloder imageFloder;
	private int totalCount;
	/**
	 * 所有的图片
	 */
	private List<String> mImgs;

	/**
	 * 存储文件夹中的图片数量
	 */
	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;
	private int mPicsSize;
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				dataView();
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item2);
		gridView = (GridView) findViewById(R.id.gridView);
		all = (TextView) findViewById(R.id.all);
		count = (TextView) findViewById(R.id.count);
		getImage();
	}

	public void getImage() {

		if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
			{
				Toast.makeText(MainActivity.this, "暂无外部存储", Toast.LENGTH_SHORT).show();
				return;
			}

		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 获取图片的URL
				Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = MainActivity.this.getContentResolver();
				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(imageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);
				System.out.println(mCursor.getCount() + "-------");
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.e("TAG", path);
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath)) {
						continue;
					} else {
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					int picSize = parentFile.list(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String filename) {
							if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					}).length;
					totalCount += picSize;

					imageFloder.setCount(totalCount);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize) {
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(100);
			}
		}).start();
	}

	private void dataView() {
		// TODO Auto-generated method stub
		if (mImgDir == null) {
			Toast.makeText(MainActivity.this, "擦，一张图片没扫描到", Toast.LENGTH_SHORT).show();
			return;
		}

		mImgs = Arrays.asList(mImgDir.list());
		count.setText(imageFloder.getCount()+"张");
		Log.e("TAG", mImgs.get(0).toString() + "dsdsds");
		gridView.setAdapter(new GridAdapter(mImgs, mImgDir.getAbsolutePath()));
	
	
	
	};

	class GridAdapter extends BaseAdapter {
		List<String> mDatas;
		String dirPath;

		public GridAdapter(List<String> mDatas, String dirPath) {
			this.mDatas = mDatas;
			this.dirPath = dirPath;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(MainActivity.this, R.layout.item_, null);
				viewHolder.img = (ImageView) convertView.findViewById(R.id.id_item_image);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if(position==0){
				viewHolder.img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
			}else{
			
				ImageLoader.getInstance(3, Type.LIFO).loadImage(dirPath + "/" + getItem(position), viewHolder.img);
			
			}
			/*
			 * FileInputStream fis; try { fis = new FileInputStream(dirPath +
			 * "/" + getItem(position)); Bitmap bm =
			 * BitmapFactory.decodeStream(fis);
			 * viewHolder.img.setImageBitmap(bm); } catch (FileNotFoundException
			 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
			 */
			return convertView;
		}
	}

	class ViewHolder {

		private ImageView img;

	}

}