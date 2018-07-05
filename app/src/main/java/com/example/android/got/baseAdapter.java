package com.example.android.got;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class baseAdapter extends ArrayAdapter<String> {

    public baseAdapter(Context context, ArrayList<String> words) {
        super(context, 0, words);
    }

    public baseAdapter(Context context) {
        super(context, 0);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        String currentWord = getItem(position);

        TextView name = (TextView) listItemView.findViewById(R.id.name);
        // Get the Miwok translation from the currentWord object and set this text on
        // the Miwok TextView.
        name.setText(currentWord);

        return listItemView;
    }
}