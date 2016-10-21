package br.borbi.ots.utility;

import android.util.Log;

/**
 * Created by Gabriela on 20/08/2015.
 */
public class LogUtility {

    public static void printArray(String className, String[] arr) {
        if(arr==null){
            Log.v(className, "Empty array");
        }else {
            for (int i = 0; i < arr.length; i++) {
                Log.v(className, arr[i]);
            }
        }
    }

    public static void printArray(String className, int[] arr) {
        if(arr==null){
            Log.v(className, "Empty array");
        }else {
            for (int i = 0; i < arr.length; i++) {
                Log.v(className, "" + arr[i]);
            }
        }
    }
}
