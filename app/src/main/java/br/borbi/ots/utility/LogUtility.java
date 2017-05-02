package br.borbi.ots.utility;

import android.util.Log;

/**
 * Created by Gabriela on 20/08/2015.
 */
public class LogUtility {

    private static final String LOG_PREFIX = "ots";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static boolean LOGGING_ENABLED = true;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    public static void printArray(String className, String[] arr) {
        if(arr==null){
            Log.v(className, "Empty array");
        }else {
            for (String anArr : arr) {
                Log.v(className, anArr);
            }
        }
    }

    public static void printArray(String className, int[] arr) {
        if(arr==null){
            Log.v(className, "Empty array");
        }else {
            for (int anArr : arr) {
                Log.v(className, "" + anArr);
            }
        }
    }

    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }
}
