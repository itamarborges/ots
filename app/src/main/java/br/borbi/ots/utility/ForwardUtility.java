package br.borbi.ots.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;

import br.borbi.ots.FailureActivity;
import br.borbi.ots.FiltersActivity;
import br.borbi.ots.ResultActivity;

/**
 * Created by Gabriela on 16/07/2015.
 */
public class ForwardUtility {

    public static final String SEARCH_ID = "SEARCH_ID";
    public static final String COORDINATES_FOUND = "COORDINATES_FOUND";
    public static final String ERROR_INTERNET_CONNECTION = "ERROR_INTERNET_CONNECTION";

    public static void goToResults(boolean foundCoordinates, Integer searchId, Context context){
        Intent intent = new Intent();
        intent.setClass(context, ResultActivity.class);
        intent.putExtra(COORDINATES_FOUND, foundCoordinates);
        intent.putExtra(SEARCH_ID, searchId);
        TaskStackBuilder.create(context).addNextIntentWithParentStack(intent).startActivities();
    }

    public static void goToFilters(Context context){
        Intent intent = new Intent();
        intent.setClass(context, FiltersActivity.class);
        context.startActivity(intent);
    }

    public static void goToFailure(Context context, boolean errorInternetConnection){
        Intent intent = new Intent();
        intent.setClass(context, FailureActivity.class);
        intent.putExtra(ERROR_INTERNET_CONNECTION, errorInternetConnection);
        context.startActivity(intent);
    }
}
