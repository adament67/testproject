package com.constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stylist.R;

/**
 * Created by adament on 7/4/16.
 */
public class LoogEditDIalog extends DialogFragment {


    private EditText name, occasion, comment;
    private Button ok, cancle;


    private OnLookEditDialogListener mListener;
    public interface OnLookEditDialogListener{

         void onPositiveClick(Bundle args, View view);
        void onNegiviteClick(View view);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{

            mListener = (OnLookEditDialogListener)activity;


        }
        catch (Error error)
        {
            error.printStackTrace();
        }
        catch (Exception e){

            Log.v("DIALOG_EXCETION", e.getMessage());

        }

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_look_edit_dialog, null);

        //init
        initialization(rootView);

        builder.setView(rootView);

        builder.setNegativeButton("", null);
        builder.setPositiveButton("",null);

        return builder.create();
    }

    private void initialization(View rootView) {


        name = (EditText)rootView.findViewById(R.id.lookbookNameText);
                occasion = (EditText)rootView.findViewById(R.id.ocassionText);
        comment = (EditText)rootView.findViewById(R.id.commentsText);

        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/lvnm.ttf");
        name.setTypeface(face);
        occasion.setTypeface(face);
        comment.setTypeface(face);

        ok = (Button)rootView.findViewById(R.id.saveButn);
        cancle = (Button)rootView.findViewById(R.id.cancelButnc);

        okListener();
        cancleListener();



        Bundle args = getArguments();
        Utils.write("lookdialog args="+args);


        if(args != null){
            Utils.write("lookdialog name="+args.getString(KEY_LOOKBOOK_NAME));
            Utils.write("look ocassion="+args.getString(KEY_OCCASION_NAME));
            Utils.write("look comments="+args.getString(KEY_COMMENTS));
            name.setText(args.getString(KEY_LOOKBOOK_NAME));
            occasion.setText(args.getString(KEY_OCCASION_NAME));
            comment.setText(args.getString(KEY_COMMENTS));

        }else{
            Toast.makeText(getActivity(), "Bundel is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancleListener() {

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);*/

                mListener.onNegiviteClick(cancle);

                dismiss();
            }
        });
    }


    public static final String KEY_LOOKBOOK_NAME = "look_name";
    public static final String KEY_OCCASION_NAME = "occasion_name";
    public static final String KEY_COMMENTS = "comments";
    private void okListener() {

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check all fields
                if(name.length()<=0) {

                    Toast.makeText(getActivity(), "Please enter lookbook name", Toast.LENGTH_SHORT).show();
                }
//                }else if(occasion.length()<=0){
//
//                    Toast.makeText(getActivity(), "Please enter occasion ", Toast.LENGTH_SHORT).show();
//
//                }else if(comment.length()<=0){
//
//                    Toast.makeText(getActivity(), "Please enter comments", Toast.LENGTH_SHORT).show();
//
//                }
else{

                    //get all data and bundle them
                    Bundle args = new Bundle();
                    args.putString(KEY_LOOKBOOK_NAME, name.getText().toString().trim());
                    args.putString(KEY_OCCASION_NAME, occasion.getText().toString().trim());
                    args.putString(KEY_COMMENTS, comment.getText().toString().trim());

                    mListener.onPositiveClick(args, ok);

                   /* final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);*/

                    dismiss();
                }
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        if(args != null){
            Utils.write("lookdialog name="+args.getString(KEY_LOOKBOOK_NAME));
            Utils.write("look ocassion="+args.getString(KEY_OCCASION_NAME));
            Utils.write("look comments="+args.getString(KEY_COMMENTS));
            name.setText(args.getString(KEY_LOOKBOOK_NAME));
                    occasion.setText(args.getString(KEY_OCCASION_NAME));
            comment.setText(args.getString(KEY_COMMENTS));

        }else{

            Toast.makeText(getActivity(), "Bundel is null", Toast.LENGTH_SHORT).show();

        }



    }
}
