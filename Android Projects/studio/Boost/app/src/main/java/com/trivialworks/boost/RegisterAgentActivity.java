package com.trivialworks.boost;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by adament on 24/5/16.
 */
public class RegisterAgentActivity extends BaseActivity {

    TextView licencAgreement;

    String licence = "By proceeding I agree that Boost or its representative may contact me by email, phone, sms(including my automatic telephone dialing system) at the email address or number I provided, including for purpose. I have read and understand the relevant ";
    String licence1 = "Driver Private Statement";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_agent);

        licencAgreement = (TextView)findViewById(R.id.licence_agreement);

        Spannable word = new SpannableString(licence);
        word.setSpan(new ForegroundColorSpan(Color.parseColor("#aaa7a7")), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Spannable word1 = new SpannableString(licence1);
        word1.setSpan(new ForegroundColorSpan(Color.parseColor("#06bebd")), 0, word1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        licencAgreement.setText(word);
        licencAgreement.append(word1);

    }
}
