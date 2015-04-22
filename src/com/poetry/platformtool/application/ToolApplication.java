package com.poetry.platformtool.application;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.andframe.activity.AfMainActivity;
import com.andframe.application.AfExceptionHandler;
import com.poetry.application.AdvertAdapter;
import com.poetry.application.Application;
import com.poetry.application.ExceptionHandler;
import com.poetry.platformtool.model.AvBackground;
import com.poetry.platformtool.model.AvFont;

public class ToolApplication extends Application{

	@Override
	public boolean isDebug() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public Class<? extends AfMainActivity> getForegroundClass() {
		// TODO Auto-generated method stub
		return super.getForegroundClass();
	}
	
	@Override
	public String getAppName() {
		// TODO Auto-generated method stub
		return "Ϊ��дʫ";
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		try {
			AVObject.registerSubclass(AvFont.class);
			AVObject.registerSubclass(AvBackground.class);
		    // ��ʼ��Ӧ�� Id �� Ӧ�� Key����������Ӧ�����ò˵����ҵ���Щ��Ϣ
		    AVOSCloud.initialize(this, "44a596ro8884hmuwxiayw0t0bh079gua5afkddh25oxei5n4",
		        "uqwwrq6rl0m15w0u7mhxlssenls6q009ekq9ob9i1u90s1k8");
		} catch (Exception e) {
			// TODO: handle exception
			AfExceptionHandler.handler(e, "Application.onCreate");
		}
	}
	
	@Override
	protected AdvertAdapter newAdvertAdapter() {
		// TODO Auto-generated method stub
		return new AdvertAdapter(){
			@Override
			public boolean isHide() {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			public String getCurrency() {
				// TODO Auto-generated method stub
				return "���";
			}
			@Override
			public String getChannel() {
				// TODO Auto-generated method stub
				return "����";
			}
		};
	}
	
	@Override
	public AfExceptionHandler getExceptionHandler() {
		// TODO Auto-generated method stub
		return new ExceptionHandler(){
			@Override
			@SuppressLint("HandlerLeak")
			public void uncaughtException(Thread thread, Throwable ex) {
				// TODO Auto-generated method stub
				super.uncaughtException(thread, ex);
			}
			@Override
			protected void onHandlerException(Throwable ex, String remark,
					String handlerid) {
				// TODO Auto-generated method stub
				super.onHandlerException(ex, remark, handlerid);
			}
		};
	}
}
