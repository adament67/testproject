package com.stylist.lookbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.constants.CustomTextView;
import com.stylist.R;

/**
 * Created by adament on 2/4/16.
 */
public class UploadLookbookDialog extends DialogFragment {

    EditText nameText, ocassionText, commentsText;
    com.constants.CustomTextView  bottomText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_upload_look_book, null);
        builder.setView(rootView);

        initViews(rootView);

        return builder.create();
    }

    private void initViews(View rootView) {

        bottomText = (com.constants.CustomTextView) rootView.findViewById(R.id.bottomText);
        nameText = (EditText) rootView.findViewById(R.id.nameText);
        ocassionText = (EditText) rootView.findViewById(R.id.ocassionText);
        commentsText = (EditText) rootView.findViewById(R.id.commentsText);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }
}
