package com.poetry.platformtool.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

/**
 * @Description: 服务器上的背景数据
 * @Author: scwang
 * @Version: V1.0, 2015-3-12 下午3:14:31
 * @Modified: 初次创建AvBackground类
 */
@AVClassName("Background")
public class AvBackground extends AVObject{

	public AvBackground() {
		// TODO Auto-generated constructor stub
	}

	public AvBackground(int point, String size, String name, AVFile faceFile,AVFile backFile) {
		super();
		setPoint(point);
		setName(name);
		setSize(size);
		setFaceFile(faceFile);
		setBackFile(backFile);
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
	
	public AVFile getBackFile() {
		return getAVFile("BackFile");
	}
	
	public void setBackFile(AVFile backFile) {
		put("BackFile", backFile);
	}
	
}
