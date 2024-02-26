package com.project_five.serverconnection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.project_five.serverconnection.R;
import com.project_five.serverconnection.model.Country;
import java.util.List;

public class CustomListViewArrayAdapter<T> extends ArrayAdapter<Country> {
    Context context;
    List<Country> countries;
    int resource;
    public CustomListViewArrayAdapter(@NonNull Context context, int resource, List<Country> countries) {
        super(context, resource, countries);
        this.context = context;
        this.countries = countries;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(resource, null);
        Country country = countries.get(position);
        ImageView photo = (ImageView) view.findViewById(R.id.lv_item_iv);
        TextView text = (TextView) view.findViewById(R.id.lv_item_tv);
        text.setText(country.getName()+":"+country.getCode()+":"+country.getRank());
        photo.setImageBitmap(country.getBitmap());
        return view;
    }
}
