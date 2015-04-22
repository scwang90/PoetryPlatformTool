package com.poetry.platformtool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.andframe.activity.framework.AfView;
import com.andframe.application.AfExceptionHandler;
import com.andframe.feature.AfDensity;
import com.andframe.feature.AfIntent;
import com.andframe.layoutbind.AfFrameSelector;
import com.andframe.network.AfImageService;
import com.andframe.thread.AfDownloader.DownloadViewerListener;
import com.andframe.thread.AfHandlerTask;
import com.andframe.view.AfMultiGridView;
import com.andframe.view.multichoice.AfMultiChoiceAdapter;
import com.andframe.view.multichoice.AfMultiChoiceItem;
import com.andframe.view.pulltorefresh.AfPullToRefreshBase.OnRefreshListener;
import com.andframe.widget.popupmenu.OnMenuItemClickListener;
import com.poetry.FontDetailActivity;
import com.poetry.FontsActivity;
import com.poetry.R;
import com.poetry.application.AdvertAdapter;
import com.poetry.application.BackService;
import com.poetry.application.FontService;
import com.poetry.entity.Font;
import com.poetry.layoutbind.ModuleNodata;
import com.poetry.layoutbind.ModuleProgress;
import com.poetry.layoutbind.ModuleTitlebar;
import com.poetry.platformtool.application.AvFontService.UploadTask;
import com.poetry.platformtool.domain.AvFontDomain;
import com.poetry.platformtool.model.AvFont;

public class FontsManageActivity  extends FontsActivity implements OnMenuItemClickListener, OnRefreshListener {

	private static final int ID_SELECT = 10;
	private static final int ID_UPLOAD = 11;
	
	protected AfMultiGridView mMultiGridView = null;
	protected MultiFontAdapter mMultimAdapter = null;
	
	@Override
	protected void onCreate(Bundle bundle, AfIntent intent) throws Exception {
		// TODO Auto-generated method stub
//		super.onCreate(bundle, intent);

		setContentView(R.layout.layout_fonts);

		mTitlebar = new ModuleTitlebar(this);
		mNodata = new ModuleNodata(this);
		mProgress = new ModuleProgress(this);
		mSelector = new AfFrameSelector(this, R.id.fonts_frame);

		mMultiGridView = new AfMultiGridView(this,(R.id.fonts_gridview));
		mMultiGridView.setOnItemClickListener(this);
		mMultiGridView.setOnRefreshListener(this);

		if (Build.VERSION.SDK_INT < 11) {
			mMultiGridView.getRefreshableView().setPadding(0, 0, 0, 0);
		}

		this.setLoading();
		mTitlebar.setTitle("字体管理");
		mTitlebar.setFunction(ModuleTitlebar.FUNCTION_MENU);
		mTitlebar.putMenu("选择", ID_SELECT);
		mTitlebar.putMenu("上传", ID_UPLOAD);
		mTitlebar.setMenuItemListener(this);
		mNodata.setButtonText("点击刷新");
		mNodata.setOnRefreshListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setLoading();
				postTask(new ServiceFontTask());
			}
		});

		postTask(new LocalFontTask());

		FontService.fontHasReaded();
		mTitlebar.getLayout().setBackgroundColor(0x99000000);
		BackService.SetBackground(this);
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case ID_SELECT:
			if (mMultimAdapter != null && mMultimAdapter.isMultiChoiceMode()) {
				mMultimAdapter.closeMultiChoice();
			}else if (mMultimAdapter != null && !mMultimAdapter.isMultiChoiceMode()) {
				mMultimAdapter.beginMultiChoice();
			}
			break;
		case ID_UPLOAD:
			if (mMultimAdapter != null) {
				List<Font> ltSelect = mMultimAdapter.peekSelectedItems();
				if (ltSelect.size() > 0) {
					postTask(new UploadTask(ltSelect){
						@Override
						public boolean onPrepare() {
							// TODO Auto-generated method stub
							if (super.onPrepare()) {
								showProgressDialog("正在上传...");
								return true;
							}
							return false;
						}
						@Override
						protected boolean onHandle(Message msg) {
							// TODO Auto-generated method stub
							hideProgressDialog();
							if (isFinish()) {
								mMultimAdapter.closeMultiChoice();
								doShowDialog("", "上传完成！");
							}else {
								AfExceptionHandler.handler(mException, mErrors);
							}
							return false;
						}});
					return true;
				}
			}
			makeToastLong("请选择需要上传的项");
			break;
		default:
			break;
		}
		return false;
	}
	
	@Override
	public boolean onMore() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onRefresh() {
		// TODO Auto-generated method stub
		postTask(new AfHandlerTask() {
			AvFontDomain dao = new AvFontDomain();
			private List<AvFont> models = new ArrayList<AvFont>();
			@Override
			protected void onWorking(Message msg) throws Exception {
				// TODO Auto-generated method stub
				models  = dao.list(0, 100);
			}
			
			@Override
			protected boolean onHandle(Message msg) {
				// TODO Auto-generated method stub
				if (isFinish()) {
					mMultiGridView.finishRefresh();
					if (models.size() > 0) {
						String[] names = new String[models.size()];
						for (int i = 0; i < models.size(); i++) {
							names[i] = models.get(i).getName();
						}
						doSelectItem("服务器列表", names, null);
					}else {
						doShowDialog("", "服务器数据为空");
					}
				}else {
					mMultiGridView.finishRefreshFail();
					makeToastLong(mErrors,mException);
				}
				return false;
			}
		});
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(com.poetry.platformtool.R.menu.fonts_manage, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adview, View view, long id, int index) {
		// TODO Auto-generated method stub
		final Font font = mAdapter.getItemAt(index);
		if (font.isDownloaded()) {
			AfIntent intent = new AfIntent(this,FontDetailActivity.class);
			intent.put(EXTRA_DATA, font);
			startActivityForResult(intent,REQUEST_FONT);
		} else if (FontService.isDownloading(font)) {
			makeToastLong("正在下载，请稍候..");
		} else {
			doShowDialog("确认下载", "是否确认下载“" + font.Name + "”背景？", "下载",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int index) {
							// TODO Auto-generated method stub
							doDownloadFont(font);
						}
					}, "取消", null);
		}
	}
	
	protected void doDownloadFont(Font font) {
		// TODO Auto-generated method stub
		FontService.downloadFont(font, null);
		setAdapterData(mltFont);
	}
	

	@Override
	@SuppressWarnings("deprecation")
	protected void setAdapterData(List<Font> ltdata) {
		// TODO Auto-generated method stub
		if (mAdapter == null) {
			mMultimAdapter = new MultiFontAdapter(getActivity(), ltdata);
			mAdapter = mMultimAdapter;
			mMultiGridView.setAdapter(mAdapter);
			mSelector.SelectFrame(mMultiGridView);
		} else {
			mAdapter.setData(ltdata);
		}
	}
	
	protected class MultiFontAdapter extends AfMultiChoiceAdapter<Font>{

		public MultiFontAdapter(Context context, List<Font> ltdata) {
			super(context, ltdata);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return super.getCount();
		}

		protected IAfLayoutItem<Font> getItemLayout(
				List<Font> ltarray, int position) {
			// TODO Auto-generated method stub
			if (ltarray.size() <= position) {
				return getItemLayout(null);
			}
			return getItemLayout(ltarray.get(position));
		}

		@Override
		protected AfMultiChoiceItem<Font> getMultiChoiceItem(Font data) {
			// TODO Auto-generated method stub
			return new AfMultiChoiceItem<Font>() {

				private TextView mTvName;
				private TextView mTvPoint;
				private TextView mTvDefault;
				private ImageView mIvFace;
				private ImageView mIvCorner;
				private Font mModel;

				@Override
				public void onHandle(AfView view) {
					// TODO Auto-generated method stub 
					mIvFace = view.findViewByID(R.id.listitem_font_face);
					mIvCorner = view.findViewByID(R.id.listitem_font_corner);
					mTvName = view.findViewByID(R.id.listitem_font_name);
					mTvPoint = view.findViewByID(R.id.listitem_font_point);
					mTvDefault = view.findViewByID(R.id.listitem_font_default);
					
					AfDensity density = new AfDensity(view.getContext());
					LayoutParams param = mIvFace.getLayoutParams();
					param.width = (density.getWidthPixels()-3*density.dip2px(5))/2;
					param.height =  param.width*600/2520;
					mIvFace.setLayoutParams(param);
					mTvDefault.setLayoutParams(param);
				}

				@Override
				protected boolean onBinding(Font model,int index,SelectStatus status) {
					// TODO Auto-generated method stub
					mModel = model;
					mTvName.setText(model.Name);
					AdvertAdapter adapter = AdvertAdapter.getInstance();
					if (model.isDownloaded()) {
						mTvPoint.setText("已下载");
						if (new File(model.getFaceUrl()).exists()) {
							Bitmap face = BitmapFactory.decodeFile(model.getFaceUrl());
							mIvFace.setImageBitmap(face);
							mIvFace.setVisibility(View.VISIBLE);
							mTvDefault.setVisibility(View.GONE);
						}else {
							mIvFace.setVisibility(View.GONE);
							mTvDefault.setVisibility(View.VISIBLE);
						}
					}else {
						if (!adapter.isHide()) {
							if (model.Point > 0) {
								String currency = adapter.getCurrency();
								mTvPoint.setText(model.Point + currency);
							}else {
								mTvPoint.setText("免费");
							}
						}else {
							mTvPoint.setText("点击详情");
						}
						mIvFace.setVisibility(View.VISIBLE);
						mTvDefault.setVisibility(View.GONE);
						AfImageService.bindImage(model.getFaceUrl(), mIvFace);
					}

					FontService.setDownloadListener(model, new DownloadViewerListener() {
						@Override
						public void onDownloadStart() {
							// TODO Auto-generated method stub
							mTvPoint.setText("正在下载");
						}
						
						@Override
						public void onDownloadProgress(float rate, long loaded, long total) {
							// TODO Auto-generated method stub
							mTvPoint.setText("已下载"+(int)(100*rate)+"%");
						}
						
						@Override
						public boolean onDownloadFinish() {
							// TODO Auto-generated method stub
							mTvPoint.setText("已下载");
							return false;
						}
						
						@Override
						public boolean onDownloadFail(String error, Throwable e) {
							// TODO Auto-generated method stub
							onBreakAway();
							return false;
						}
						
						@Override
						public boolean onBreakAway() {
							// TODO Auto-generated method stub
							String currency = AdvertAdapter.getInstance().getCurrency();
							mTvPoint.setText(mModel.Point + currency);
							return false;
						}

					});
					
					if (model.Name.equals(FontService.getFont().Name) && model.isDownloaded()) {
						mIvCorner.setVisibility(View.VISIBLE);
					}else {
						mIvCorner.setVisibility(View.GONE);
					}
					return false;
				}

				@Override
				public int getLayoutId() {
					// TODO Auto-generated method stub
					return R.layout.listitem_font;
				}

			};
		}

	}
}

