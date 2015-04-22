package com.poetry.platformtool.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

/**
 * @Description: 服务器上的字体数据
 * @Author: scwang
 * @Version: V1.0, 2015-3-12 下午3:14:31
 * @Modified: 初次创建AvBackground类
 */
@AVClassName("Font")
public class AvFont extends AVObject{

	public AvFont() {
		// TODO Auto-generated constructor stub
	}

	public AvFont(int point, String size, String name, AVFile faceFile,AVFile ImageFile,AVFile fontFile) {
		super();
		setPoint(point);
		setName(name);
		setSize(size);
		setFaceFile(faceFile);
		setImageFile(ImageFile);
		setFontFile(fontFile);
	}

	public int getPoint() {
		return getInt("Point");
	}
	
	public void setPoint(int point) {
		put("Point", point);
	}
	
	public String getName() {
		return getString("Name");
	}
	
	public void setName(String name) {
		put("Name", name);
	}
	
	public String getSize() {
		return getString("Size");
	}
	
	public void setSize(String size) {
		put("Size", size);
	}
	
	public AVFile getFaceFile() {
		return getAVFile("FaceFile");
	}
	
	public void setFaceFile(AVFile faceFile) {
		put("FaceFile", faceFile);
	}
	
	public AVFile getImageFile() {
		return getAVFile("ImageFile");
	}
	
	public void setImageFile(AVFile imageFile) {
		put("ImageFile", imageFile);
	}
	
	public AVFile getFontFile() {
		return getAVFile("FontFile");
	}
	
	public void setFontFile(AVFile fontFile) {
		put("FontFile", fontFile);
	}
	
}
