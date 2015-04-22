package com.poetry.platformtool.application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Message;

import com.avos.avoscloud.AVFile;
import com.andframe.application.AfApplication;
import com.andframe.thread.AfHandlerTask;
import com.poetry.entity.Background;
import com.poetry.platformtool.domain.AvBackgroundDomain;
import com.poetry.platformtool.model.AvBackground;

public class AvBackService {

	public static abstract class UploadTask extends AfHandlerTask{
		
		List<AvBackground> backs = null;
		
		public UploadTask(List<Background> backs) {
			// TODO Auto-generated constructor stub
			this.backs = toAvBackgrounds(backs);
		}
		
		@Override
		public boolean onPrepare() {
			// TODO Auto-generated method stub
			if (backs.size() == 0) {
				AfApplication.getApp().getCurActivity().makeToastLong("您选择的项在本地没有副本，不能上传！");
				return false;
			}
			return super.onPrepare();
		}

		@Override
		protected void onWorking(Message msg) throws Exception {
			// TODO Auto-generated method stub
			AvBackgroundDomain dao = new AvBackgroundDomain();
			for (AvBackground background : backs) {
				dao.add(background);
			}
		}
		
	}

	public static List<AvBackground> toAvBackgrounds(List<Background> backs) {
		// TODO Auto-generated method stub
		List<AvBackground> ltbacks = new ArrayList<AvBackground>();
		for (Background background : backs) {
			if (background.isDownloaded()) {
				AvBackground avback = new AvBackground();
				avback.setName(background.Name);
				avback.setPoint(background.Point);
				avback.setSize(background.Size);
				try {
					avback.setFaceFile(AVFile.withFile(background.Name+"-face.jpg", new File(background.getFaceUrl())));
					avback.setBackFile(AVFile.withFile(background.Name+"-back.jpg", new File(background.getBackUrl())));
					ltbacks.add(avback);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ltbacks;
	}
}
