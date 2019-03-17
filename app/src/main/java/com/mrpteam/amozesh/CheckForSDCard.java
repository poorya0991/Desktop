package com.mrpteam.amozesh;

import android.os.Environment;

/**
 * Created by poory on 3/17/2019.
 */

public class CheckForSDCard {
    //Method to Check If SD Card is mounted or not
    public static boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}