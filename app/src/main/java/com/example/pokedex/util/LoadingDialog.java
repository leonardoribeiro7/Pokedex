package com.example.pokedex.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.fragment.app.DialogFragment;

import com.example.pokedex.R;

public class LoadingDialog {

    //Loading POP UP

    Dialog dialog;

    //Requires a context and a boolean to determine if the user can cancel or not the loading
    public void displayLoading(Context context, Boolean cancel) {
        dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_screen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(cancel);
        try {
            dialog.show();
        } catch (Exception ignored) {
        }
    }

    //Function to hide the loading pop UP
    public void hideLoading() {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
        } catch (Exception ignore) {

        }

    }
}
