package com.devandroid.tmsearch.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.devandroid.tmsearch.Preferences.Preferences;
import com.devandroid.tmsearch.R;

import java.util.ArrayList;

class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<String> listItemList = new ArrayList<>();
    Context mContext;

    public ListRemoteViewFactory(Context applicationContext) {

        mContext = applicationContext;
    }

    @Override
    public void onDataSetChanged() {
        listItemList = Preferences.restoreStringList(mContext);
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_row);
        String strItem = listItemList.get(position);
        views.setTextViewText(R.id.tv_row_content, strItem);

        return views;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}



