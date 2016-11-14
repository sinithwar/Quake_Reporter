package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.net.ConnectException;
import java.util.List;

/**
 * Created by Tommy on 11/13/2016.
 */
public class QuakeAdapter extends ArrayAdapter<Quake> {
    public QuakeAdapter(Context context, List<Quake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View quakeItemView = convertView;

        if (quakeItemView == null) {
            quakeItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }
        Quake quakePos = getItem(position);
        TextView mag = (TextView) quakeItemView.findViewById(R.id.mag);
        mag.setText(Double.toString(quakePos.getMag()));
        TextView city = (TextView) quakeItemView.findViewById(R.id.city);
        city.setText(quakePos.getName());
        TextView date = (TextView) quakeItemView.findViewById(R.id.date);
        date.setText(quakePos.getDateOccured());
        return quakeItemView;
    }
}