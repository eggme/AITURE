package com.lsj.aiture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

public class MarketActivity extends AppCompatActivity implements NoActionBar{

    private GetMarketServerData marketServerData;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        gridView = (GridView)findViewById(R.id.gridview);
        marketServerData = new GetMarketServerData();
        marketServerData.execute();
        try {
            final ArrayList<MarketVO> list  = marketServerData.get();
            CardViewAdpater adpater = new CardViewAdpater(getApplicationContext(), R.layout.custom_cardview, list);
            gridView.setAdapter(adpater);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "구매완료", Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = getSharedPreferences("list", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("item",list.get(position).getTitle());
                    editor.commit();
                    setResult(RESULT_OK);
                    finish();
                }
            });

        }catch (Exception e){
            Log.i("asdasd", "파싱오류");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        HideActionbar();
    }

    @Override
    public void HideActionbar() {
        View view = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}
