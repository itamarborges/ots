package br.borbi.ots.utility;

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
    public static final String SHOW_FOLLOWING_BUTTON = "SHOW_FOLLOWING_BUTTON";

    public static void goToResults(boolean foundCoordinates, Integer searchId, Context context){
        Intent intent = new Intent();
        intent.setClass(context, ResultActivity.class);
        intent.putExtra(COORDINATES_FOUND, foundCoordinates);
        intent.putExtra(SEARCH_ID, searchId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        TaskStackBuilder.create(context).addNextIntentWithParentStack(intent).startActivities();
    }

    public static void goToFilters(Context context){
        Intent intent = new Intent();
        intent.setClass(context, FiltersActivity.class);
        context.startActivity(intent);
    }

    public static void goToFailure(Context context, boolean errorInternetConnection){
        goToFailure(context, errorInternetConnection, true);
    }

    public static void goToFailure(Context context, boolean errorInternetConnection, boolean showFollowingButton){
        Intent intent = new Intent();
        intent.setClass(context, FailureActivity.class);
        intent.putExtra(ERROR_INTERNET_CONNECTION, errorInternetConnection);
        intent.putExtra(SHOW_FOLLOWING_BUTTON, showFollowingButton);
        context.startActivity(intent);
    }
}
