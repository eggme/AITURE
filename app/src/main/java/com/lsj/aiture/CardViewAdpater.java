package com.lsj.aiture;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CardViewAdpater extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<MarketVO> list;
    private LayoutInflater inflater;

    public CardViewAdpater(Context context, int layout, ArrayList<MarketVO> list){
        this.context = context;
        this.layout = layout;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(layout, null);
        TextView title = (TextView)convertView.findViewById(R.id.title);
        title.setText(list.get(position).getTitle());
        TextView content = (TextView)convertView.findViewById(R.id.content);
        content.setText(list.get(position).getContent());
        ImageView img = (ImageView)convertView.findViewById(R.id.image);
        switch (position){
            case 0:
                img.setImageResource(R.drawable.image1);
                break;
            case 1:
                img.setImageResource(R.drawable.image2);
                break;
            case 2:
                img.setImageResource(R.drawable.image3);
                break;
        }
        return convertView;
    }
}
