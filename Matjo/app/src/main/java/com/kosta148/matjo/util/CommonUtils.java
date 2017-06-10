package com.kosta148.matjo.util;

import android.content.Context;

/**
 * 안드로이드 UI 상에서 사용되는 공통적인 기능들을 CommonsUtil 이라고 정의, 이를 관리하는 클래스
 * Created by Daehee on 2017-06-10.
 */

/**
 *
 */
public class CommonUtils {
    // Variables
    Context context;

    // Constructors
    public CommonUtils(Context context) {
        this.context = context;
    }

    // Methods
    /**
     * 입력된 dp 값으로부터 기기의 화면에 맞추어 px 값을 리턴하는 함수
     * @param dimensionDp
     * @return px 값을 리턴
     */
    public static int getPx(Context context, int dimensionDp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    } // end of getPx()


} // end of class
