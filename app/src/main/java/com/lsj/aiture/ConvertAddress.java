package com.lsj.aiture;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ConvertAddress {

    private Context context;

    public ConvertAddress(Context context){
        this.context = context;
    }

    public String getAddress(double lat, double lng){
            String nowAddress ="현재 위치를 확인 할 수 없습니다.";
            Geocoder geocoder = new Geocoder(context, Locale.KOREA);
            List<Address> convertAddresses;
            try {
                if (geocoder != null) {
                    //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                    //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                    convertAddresses = geocoder.getFromLocation(lat, lng, 1);

                    if (convertAddresses != null && convertAddresses.size() > 0) {
                        // 주소 받아오기
                        String currentLocationAddress = convertAddresses.get(0).getAddressLine(0).toString();
                        nowAddress  = currentLocationAddress;

                    }
                }

            } catch (IOException e) {
                Log.i("ConvertAddress", e.getMessage());
            }
            return nowAddress;
    }
}
