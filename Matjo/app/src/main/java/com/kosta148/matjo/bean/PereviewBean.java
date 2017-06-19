package com.kosta148.matjo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PereviewBean implements Parcelable{
	private String pereviewNo;
	private String pereviewMemId;
	private String pereviewMemName;
	private String pereviewMemImg;
	private String pereviewReviewNo;
	private String pereviewContent;
	private String pereviewRating;
	private String pereviewImgUrl;
	private String pereviewDate;

	protected PereviewBean(Parcel in) {
		pereviewNo = in.readString();
		pereviewMemId = in.readString();
		pereviewMemName = in.readString();
		pereviewMemImg = in.readString();
		pereviewReviewNo = in.readString();
		pereviewContent = in.readString();
		pereviewRating = in.readString();
		pereviewImgUrl = in.readString();
		pereviewDate = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(pereviewNo);
		dest.writeString(pereviewMemId);
		dest.writeString(pereviewMemName);
		dest.writeString(pereviewMemImg);
		dest.writeString(pereviewReviewNo);
		dest.writeString(pereviewContent);
		dest.writeString(pereviewRating);
		dest.writeString(pereviewImgUrl);
		dest.writeString(pereviewDate);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public static final Creator<PereviewBean> CREATOR = new Creator<PereviewBean>() {
		@Override
		public PereviewBean createFromParcel(Parcel in) {
			return new PereviewBean(in);
		}

		@Override
		public PereviewBean[] newArray(int size) {
			return new PereviewBean[size];
		}
	};

	public String getPereviewNo() {
		return pereviewNo;
	}
	public void setPereviewNo(String pereviewNo) {
		this.pereviewNo = pereviewNo;
	}
	public String getPereviewMemId() {
		return pereviewMemId;
	}
	public void setPereviewMemId(String pereviewMemId) {
		this.pereviewMemId = pereviewMemId;
	}
	public String getPereviewMemName() {
		return pereviewMemName;
	}
	public void setPereviewMemName(String pereviewMemName) {
		this.pereviewMemName = pereviewMemName;
	}
	public String getPereviewMemImg() {
		return pereviewMemImg;
	}
	public void setPereviewMemImg(String pereviewMemImg) {
		this.pereviewMemImg = pereviewMemImg;
	}
	public String getPereviewReviewNo() {
		return pereviewReviewNo;
	}
	public void setPereviewReviewNo(String pereviewReviewNo) {
		this.pereviewReviewNo = pereviewReviewNo;
	}
	public String getPereviewContent() {
		return pereviewContent;
	}
	public void setPereviewContent(String pereviewContent) {
		this.pereviewContent = pereviewContent;
	}
	public String getPereviewRating() {
		return pereviewRating;
	}
	public void setPereviewRating(String pereviewRating) {
		this.pereviewRating = pereviewRating;
	}
	public String getPereviewImgUrl() {
		return pereviewImgUrl;
	}
	public void setPereviewImgUrl(String pereviewImgUrl) {
		this.pereviewImgUrl = pereviewImgUrl;
	}
	public String getPereviewDate() {
		return pereviewDate;
	}
	public void setPereviewDate(String pereviewDate) {
		this.pereviewDate = pereviewDate;
	}
	
	
} // end of class
