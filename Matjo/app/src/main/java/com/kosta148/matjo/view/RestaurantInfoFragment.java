package com.kosta148.matjo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosta148.matjo.R;
import com.kosta148.matjo.bean.GroupBean;
import com.kosta148.matjo.data.DaumLocalBean;

/**
 * Created by Daehee on 2017-05-14.
 */

public class RestaurantInfoFragment extends Fragment {

    DaumLocalBean dlBean;

    private GoogleMap mMap;

    TextView tvRestaId;
    TextView tvRestaCate;
    TextView tvRestaAddr;
    TextView tvRestaPhone;
    TextView tvRestaUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant_info, container, false);

        dlBean = (DaumLocalBean) getArguments().get("dlBean");

        tvRestaId = (TextView) v.findViewById(R.id.tvRestaId);
        tvRestaCate = (TextView) v.findViewById(R.id.tvRestaCate);
        tvRestaAddr = (TextView) v.findViewById(R.id.tvRestaAddr);
        tvRestaPhone = (TextView) v.findViewById(R.id.tvRestaPhone);
        tvRestaUrl = (TextView) v.findViewById(R.id.tvRestaUrl);

        tvRestaId.setText(dlBean.getRestaId());
        tvRestaCate.setText(dlBean.getRestaCate());
        tvRestaAddr.setText(dlBean.getRestaAddr());
        tvRestaPhone.setText(dlBean.getRestaPhone());
        tvRestaUrl.setText(dlBean.getRestaUrl());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(mapReadyCallback);

        return v;
    } // end of onCreateView

    OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(Double.parseDouble(dlBean.getRestaLat()), Double.parseDouble(dlBean.getRestaLng()));
        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(dlBean.getRestaTitle()));
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    };
} // end of class
