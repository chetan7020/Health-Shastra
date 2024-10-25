package com.example.test1.utils;

import android.content.Context;
import android.widget.Toast;

public class Constant {
    public static void showToast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
