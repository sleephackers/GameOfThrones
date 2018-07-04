package com.example.android.got;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.got.data.GotContract;


public class GotCursorAdapter extends CursorAdapter {


    public GotCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);

        int nameColumnIndex = cursor.getColumnIndex(GotContract.GotEntry.COLUMN_NAME);

        String team1 = cursor.getString(nameColumnIndex);

        nameTextView.setText(team1);
    }
}

