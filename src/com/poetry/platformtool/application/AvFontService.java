package com.poetry.platformtool.application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Message;

import com.avos.avoscloud.AVFile;
import com.andframe.application.AfApplication;
import com.andframe.thread.AfHandlerTask;
import com.poetry.entity.Font;
import com.poetry.platformtool.domain.AvFontDomain;
import com.poetry.platformtool.model.AvFont;

public class AvFontService {

	public static abstract class UploadTask extends AfHandlerTask{
		
		List<AvFont> fonts = null;
		
		public UploadTask(List<Font> fonts) {
			// TODO Auto-generated constructor stub
			this.fonts = toAvFonts(fonts);
		}
		
		@Override
		public boolean onPrepare() {
			// TODO Auto-generated method stub
			if (fonts.size() == 0) {
				AfApplication.getApp().getCurActivity().makeToastLong("您选择的项在本地没有副本，不能上传！");
				return false;
			}
			return super.onPrepare();
		}

		@Override
		protected void onWorking(Message msg) throws Exception {
			// TODO Auto-generated method stub
			AvFontDomain dao = new AvFontDomain();
			for (AvFont font : fonts) {
				dao.add(font);
			}
		}
		
	}

	public static List<AvFont> toAvFonts(List<Font> fonts) {
		// TODO Auto-generated method stub
		List<AvFont> ltfonts = new ArrayList<AvFont>();
		for (Font font : fonts) {
			if (font.isDownloaded()) {
				AvFont avfont = new AvFont();
				avfont.setName(font.Name);
				avfont.setPoint(font.Point);
				avfont.setSize(font.Size);
				try {
					avfont.setFaceFile(AVFile.withFile(font.Name+"-face.jpg", new File(font.getFaceUrl())));
					avfont.setImageFile(AVFile.withFile(font.Name+"-image.jpg", new File(font.getImageUrl())));
					avfont.setFontFile(AVFile.withFile(font.Name+"-font.ttf", new File(font.getFontUrl())));
					ltfonts.add(avfont);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				font.toString();
			}
		}
		return ltfonts;
	}
}
