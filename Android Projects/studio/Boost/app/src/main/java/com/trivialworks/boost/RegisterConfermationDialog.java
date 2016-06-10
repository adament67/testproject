package com.trivialworks.boost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by adament on 24/5/16.
 */
public class RegisterConfermationDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_register_confermation, null);
        initialization(rootView);

        builder.setView(rootView);



        return builder.create();
    }

    private void initialization(View rootView) {


    }

}
