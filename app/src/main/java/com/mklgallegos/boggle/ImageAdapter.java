package com.mklgallegos.boggle;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ImageAdapter extends ArrayAdapter<Object>
{

    private int resource;
    private int mImgGrid;
    private ArrayList<String> mTitleText;

    public ImageAdapter(Context context, int resource, int imgGrid, ArrayList<String> titleText)
    {
        super(context, resource, imgGrid);
        this.resource = resource;
        this.mImgGrid = imgGrid;
        this.mTitleText = titleText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView  = layoutInflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
            holder.imgGrid = (ImageView)convertView.findViewById(R.id.imgGrid);

            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        try
        {
            holder.txtTitle.setText(mTitleText.get(position));
            holder.imgGrid.setImageResource(mImgGrid);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return convertView;
    }

    public static class ViewHolder
    {
        private TextView txtTitle;
        private ImageView imgGrid;
    }
}