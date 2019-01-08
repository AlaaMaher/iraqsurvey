package com.example.asamir.iraqproject.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asamir.iraqproject.R;

/**
 * Created by belal on 1/7/19.
 */

public class CustomToast {


    Toast toast ;
    View toastView ; // This'll return the default View of the Toast.

    TextView toastMessage ;


    public CustomToast(Context context, String message)
    {
        toast = Toast.makeText(context, ""+message, Toast.LENGTH_LONG);
        toastView = toast.getView();
      /* And now you can get the TextView of the default View of the Toast. */
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(24);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setCompoundDrawablePadding(16);
        toastMessage.setText(""+message);
        toastView.setBackgroundColor(Color.RED);
        toast.show();
    }



}
