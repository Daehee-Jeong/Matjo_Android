package com.kosta148.matjo.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosta148.matjo.R;
import com.kosta148.matjo.bean.GroupBean;

/**
 * Created by Daehee on 2017-06-22.
 */

public class GroupInsertActivity extends AppCompatActivity{
    EditText etGroupName;
    EditText etGroupInfo;
    ImageView ivGroupImg;
    Button btnInsert;

    public static final int PICK_FROM_ALBUM = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_insert);

        getSupportActionBar().setTitle("모임 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etGroupName = (EditText) findViewById(R.id.etGroupName);
        etGroupInfo = (EditText) findViewById(R.id.etGroupInfo);
        ivGroupImg = (ImageView) findViewById(R.id.ivGroupImg);
        ivGroupImg.setOnClickListener(mClickListener);
        btnInsert = (Button) findViewById(R.id.btnInsert);


    } // end of onCreate

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.ivGroupImg:
                    // 인텐트로 사진 받아오기
                    getPhotoFromGallary();
                    break;

                case R.id.btnInsert:
                    // 등록 함수 호출
                    break;

            } // end of switch
        } // end of onClick
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    } // end of onOptionItemSelected()

    public void getPhotoFromGallary() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    } // end of getPhotoFromGallary

    /**
     * 모임을 등록하는 함수 ( Multipart )
     * @param gBean
     */
    public void insertGroup(GroupBean gBean) {

    } // end of insertGroup()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_ALBUM:
                Uri selUri = data.getData(); // 갤러리에서 받아온 사진의 Uri
                String userImgPath = selUri.getPath();
                Toast.makeText(getApplicationContext(), "사진가져옴" + userImgPath,
                        Toast.LENGTH_SHORT).show();
//                uploadImage();
                break;
        } // end of switch
    } //  end of onActivityResult()

} // end of class
