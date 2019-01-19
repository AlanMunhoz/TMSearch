package com.devandroid.tmsearch.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.devandroid.tmsearch.R;


public class WidgetService extends IntentService {


    public static final String ACTION_UPDATE_WIDGET_ = "com.devandroid.tmsearch.action.update_movies";

    public WidgetService(String name) {
        super(name);
    }

    public static void startUpdateWidget(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET_);
        context.startService(intent);

        handleActionUpdateWidget(intent, context);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET_.equals(action)) {
                handleActionUpdateWidget(intent, this);
            }
        }
    }

    private static void handleActionUpdateWidget(@Nullable Intent intent, Context context) {

        if (intent != null)
        {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);

            WidgetProvider.updateWidgets(context, appWidgetManager, appWidgetIds);
        }
    }

}
