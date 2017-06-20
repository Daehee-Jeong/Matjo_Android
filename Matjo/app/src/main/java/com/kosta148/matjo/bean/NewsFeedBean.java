package com.kosta148.matjo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsFeedBean implements Parcelable {
	private String groupNo;		// 모임 번호	//모임//리뷰
	private String groupName;	// 모임 이름	//모임//리뷰
	private String groupLeader; // 모임장		//모임
	private String reviewNo;	// 리뷰 번호	//리뷰
	private String restaName;	// 업소 이름	//리뷰
	private String restaCate;	// 업소 카테고리	//리뷰
	private String imgPath;		// 이미지 경로	//모임//리뷰
	private String regDate;		// 날짜시간	//모임//리뷰
	
	private String type;		// 종류번호 - 모임:1/리뷰:2
	private String typeMsg;		// 종류 - 새 모임이 / 리뷰가 (등록되었습니다)


	protected NewsFeedBean(Parcel in) {
		groupNo = in.readString();
		groupName = in.readString();
		groupLeader = in.readString();
		reviewNo = in.readString();
		restaName = in.readString();
		restaCate = in.readString();
		imgPath = in.readString();
		regDate = in.readString();
		type = in.readString();
		typeMsg = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(groupNo);
		dest.writeString(groupName);
		dest.writeString(groupLeader);
		dest.writeString(reviewNo);
		dest.writeString(restaName);
		dest.writeString(restaCate);
		dest.writeString(imgPath);
		dest.writeString(regDate);
		dest.writeString(type);
		dest.writeString(typeMsg);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<NewsFeedBean> CREATOR = new Creator<NewsFeedBean>() {
		@Override
		public NewsFeedBean createFromParcel(Parcel in) {
			return new NewsFeedBean(in);
		}

		@Override
		public NewsFeedBean[] newArray(int size) {
			return new NewsFeedBean[size];
		}
	};

	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupLeader() {
		return groupLeader;
	}
	public void setGroupLeader(String groupLeader) {
		this.groupLeader = groupLeader;
	}
	public String getReviewNo() {
		return reviewNo;
	}
	public void setReviewNo(String reviewNo) {
		this.reviewNo = reviewNo;
	}
	public String getRestaName() {
		return restaName;
	}
	public void setRestaName(String restaName) {
		this.restaName = restaName;
	}
	public String getRestaCate() {
		return restaCate;
	}
	public void setRestaCate(String restaCate) {
		this.restaCate = restaCate;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeMsg() {
		return typeMsg;
	}
	public void setTypeMsg(String typeMsg) {
		this.typeMsg = typeMsg;
	}


}
