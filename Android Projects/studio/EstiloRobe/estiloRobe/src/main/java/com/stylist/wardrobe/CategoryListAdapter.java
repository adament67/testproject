package com.stylist.wardrobe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.stylist.R;

import java.util.ArrayList;

/**
 * Created by adament on 31/3/16.
 */
public class CategoryListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<CategoryModel> dataSet;

    public CategoryListAdapter(Context context, ArrayList<CategoryModel> dataSet){

        this.dataSet = dataSet;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataSet.indexOf(dataSet.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){

            convertView = inflater.inflate(R.layout.mycloset_vertical_list_items, parent, false);

        }

        return convertView;
    }

    private class ViewHolder {
        ImageView i1, i2, i3, i4, edit, delete;
        com.constants.CustomTextView mytitle;
    }

}
