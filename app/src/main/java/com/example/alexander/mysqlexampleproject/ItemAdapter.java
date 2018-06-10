package com.example.alexander.mysqlexampleproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class ItemAdapter extends BaseAdapter {

    LayoutInflater mInflator;
    Map<String, Double> map;
    ArrayList<String> names;
    ArrayList<Double> prices;

    public ItemAdapter(Context c, Map<String, Double> map) {
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.map = map;
        this.names = new ArrayList<>(map.keySet());
        this.prices = new ArrayList<>(map.values());
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mInflator.inflate(R.layout.item_layout, null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);
        TextView priceTextView = (TextView) v.findViewById(R.id.priceTextView);

        nameTextView.setText(names.get(position));
        priceTextView.setText("$" + prices.get(position).toString());

        return v;
    }
}
