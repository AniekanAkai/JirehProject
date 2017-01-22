package com.appspot.aniekanedwardakai.jireh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Teddy on 1/9/2017.
 */

public class ServiceProviderRequestStatusSpinnerAdapter extends ArrayAdapter<ServiceProviderRequestStatus> {

    Context context;
    ServiceProviderRequestStatus[] options;

    public ServiceProviderRequestStatusSpinnerAdapter(Context context, int textViewResourceId,
                                                      ServiceProviderRequestStatus[] myOptions) {
        super(context, textViewResourceId, myOptions);
        this.context = context;
        options = myOptions;
    }

    public int getCount(){
        return options.length;
    }

    public ServiceProviderRequestStatus getItem(int position){
        return options[position];
    }

    public long getItemId(int position){
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        TextView label = new TextView(context);
        label.setText(options[position].getRequestStatusValue());
        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        return super.getDropDownView(position, convertView, parent);
        TextView label = new TextView(context);
        label.setText(options[position].getRequestStatusValue());
        return label;
    }

}
