package com.kosta148.matjo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ReviewBean implements Parcelable{
	
	private String reviewNo;
	private String reviewRestaImg;
	private String reviewGroupImg;
	private String reviewGroupNo;
	private String reviewRestaNo;
	private String reviewRestaName;
	private String reviewRestaCate;
	private String reviewIsOpen;
	private List<PereviewBean> pereviewList;
	private String pereviewJSArray;
	
	private String memberNo; // 리뷰목록 받아올때 현재 접속한 사용자 정보를 넘기는데 사용
	
	private String reviewDate;		// 리뷰 등록일자 (개인리뷰 최종 등록일자)
	
	private String reviewLike;		// 리뷰 좋아요 개수
	private String reviewGroupName;	// 리뷰 작성 모임 이름
	
	private String reviewNowMember;	// 리뷰를 보고 있는 현재 멤버
	private String reviewHasLike;	// 리뷰 좋아요 여부 (로그인 회원 기준)
	
	private String avgRating;
	
	/* Constructors */
	public ReviewBean() {
	}
	public ReviewBean(String reviewNo) { // 리뷰넘버와 그룹넘버를 받는 생성자
		super();
		this.reviewNo = reviewNo;
	}


	protected ReviewBean(Parcel in) {
		reviewNo = in.readString();
		reviewRestaImg = in.readString();
		reviewGroupImg = in.readString();
		reviewGroupNo = in.readString();
		reviewRestaNo = in.readString();
		reviewRestaName = in.readString();
		reviewRestaCate = in.readString();
		reviewIsOpen = in.readString();
		pereviewList = in.createTypedArrayList(PereviewBean.CREATOR);
		pereviewJSArray = in.readString();
		memberNo = in.readString();
		reviewDate = in.readString();
		reviewLike = in.readString();
		reviewGroupName = in.readString();
		reviewNowMember = in.readString();
		reviewHasLike = in.readString();
		avgRating = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(reviewNo);
		dest.writeString(reviewRestaImg);
		dest.writeString(reviewGroupImg);
		dest.writeString(reviewGroupNo);
		dest.writeString(reviewRestaNo);
		dest.writeString(reviewRestaName);
		dest.writeString(reviewRestaCate);
		dest.writeString(reviewIsOpen);
		dest.writeTypedList(pereviewList);
		dest.writeString(pereviewJSArray);
		dest.writeString(memberNo);
		dest.writeString(reviewDate);
		dest.writeString(reviewLike);
		dest.writeString(reviewGroupName);
		dest.writeString(reviewNowMember);
		dest.writeString(reviewHasLike);
		dest.writeString(avgRating);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ReviewBean> CREATOR = new Creator<ReviewBean>() {
		@Override
		public ReviewBean createFromParcel(Parcel in) {
			return new ReviewBean(in);
		}

		@Override
		public ReviewBean[] newArray(int size) {
			return new ReviewBean[size];
		}
	};

	public String getReviewNo() {
		return reviewNo;
	}
	public void setReviewNo(String reviewNo) {
		this.reviewNo = reviewNo;
	}
	public String getReviewGroupNo() {
		return reviewGroupNo;
	}
	public void setReviewGroupNo(String reviewGroupNo) {
		this.reviewGroupNo = reviewGroupNo;
	}
	public String getReviewRestaNo() {
		return reviewRestaNo;
	}
	public void setReviewRestaNo(String reviewRestaNo) {
		this.reviewRestaNo = reviewRestaNo;
	}
	public String getReviewRestaName() {
		return reviewRestaName;
	}
	public void setReviewRestaName(String reviewRestaName) {
		this.reviewRestaName = reviewRestaName;
	}
	public String getReviewRestaCate() {
		return reviewRestaCate;
	}
	public void setReviewRestaCate(String reviewRestaCate) {
		this.reviewRestaCate = reviewRestaCate;
	}
	public String getReviewIsOpen() {
		return reviewIsOpen;
	}
	public void setReviewIsOpen(String reviewIsOpen) {
		this.reviewIsOpen = reviewIsOpen;
	}
	public List<PereviewBean> getPereviewList() {
		return pereviewList;
	}
	public void setPereviewList(List<PereviewBean> pereviewList) {
		this.pereviewList = pereviewList;
		
		/* 평균 계산 */
		String avg = "0.0";
		double sum = 0;
		for (int i = 0; i < pereviewList.size(); i++) {
			sum += Double.parseDouble(pereviewList.get(i).getPereviewRating());
		}
		avg = String.format("%.1f", (sum/pereviewList.size()));
		this.avgRating = avg;
		
		/* 리뷰 최종 등록 날짜 설정 */
		if (pereviewList.size() > 0) {
			this.reviewDate = pereviewList.get(pereviewList.size()-1).getPereviewDate();
		} else {
			// 아직 개인리뷰가 하나도 없으면 null 설정
			this.reviewDate = null;
		}
	}
	public String getReviewGroupName() {
		return reviewGroupName;
	}
	public void setReviewGroupName(String reviewGroupName) {
		this.reviewGroupName = reviewGroupName;
	}
	public String getAvgRating() {
		return avgRating;
	}
	public void setAvgRating(String avgRating) {
		this.avgRating = avgRating;
	}
	
	public String getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
	public String getReviewLike() {
		return reviewLike;
	}
	public void setReviewLike(String reviewLike) {
		this.reviewLike = reviewLike;
	}
	public String getReviewNowMember() {
		return reviewNowMember;
	}
	public void setReviewNowMember(String reviewNowMember) {
		this.reviewNowMember = reviewNowMember;
	}
	public String getReviewHasLike() {
		return reviewHasLike;
	}
	public void setReviewHasLike(String reviewHasLike) {
		this.reviewHasLike = reviewHasLike;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public String getPereviewJSArray() {
		return pereviewJSArray;
	}
	public void setPereviewJSArray(String pereviewJSArray) {
		this.pereviewJSArray = pereviewJSArray;
	}

	public String getReviewGroupImg() {
		return reviewGroupImg;
	}

	public void setReviewGroupImg(String reviewGroupImg) {
		this.reviewGroupImg = reviewGroupImg;
	}

	public String getReviewRestaImg() {
		return reviewRestaImg;
	}

	public void setReviewRestaImg(String reviewRestaImg) {
		this.reviewRestaImg = reviewRestaImg;
	}
} // end of class
