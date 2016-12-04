package com.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;


public class KeyboardFragment extends Fragment implements View.OnClickListener {

    private View view;
    private AudioPlayer audioPlayer;
    private String loginString = "";
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_keyboard, container, false);
        this.view = view;

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        TextView aButton = (TextView) view.findViewById(R.id.textViewA);
        TextView bButton = (TextView) view.findViewById(R.id.textViewB);
        TextView cButton = (TextView) view.findViewById(R.id.textViewC);
        TextView dButton = (TextView) view.findViewById(R.id.textViewD);
        TextView eButton = (TextView) view.findViewById(R.id.textViewE);
        TextView fButton = (TextView) view.findViewById(R.id.textViewF);
        TextView gButton = (TextView) view.findViewById(R.id.textViewG);
        TextView hButton = (TextView) view.findViewById(R.id.textViewH);
        TextView iButton = (TextView) view.findViewById(R.id.textViewI);
        TextView jButton = (TextView) view.findViewById(R.id.textViewJ);
        TextView kButton = (TextView) view.findViewById(R.id.textViewK);
        TextView lButton = (TextView) view.findViewById(R.id.textViewL);
        TextView mButton = (TextView) view.findViewById(R.id.textViewM);
        TextView nButton = (TextView) view.findViewById(R.id.textViewN);
        TextView oButton = (TextView) view.findViewById(R.id.textViewO);
        TextView pButton = (TextView) view.findViewById(R.id.textViewP);
        TextView qButton = (TextView) view.findViewById(R.id.textViewQ);
        TextView rButton = (TextView) view.findViewById(R.id.textViewR);
        TextView sButton = (TextView) view.findViewById(R.id.textViewS);
        TextView tButton = (TextView) view.findViewById(R.id.textViewT);
        TextView uButton = (TextView) view.findViewById(R.id.textViewU);
        TextView vButton = (TextView) view.findViewById(R.id.textViewV);
        TextView wButton = (TextView) view.findViewById(R.id.textViewW);
        TextView xButton = (TextView) view.findViewById(R.id.textViewX);
        TextView yButton = (TextView) view.findViewById(R.id.textViewY);
        TextView zButton = (TextView) view.findViewById(R.id.textViewZ);
        TextView backButton = (TextView) view.findViewById(R.id.textViewBack);


        aButton.setOnClickListener(this);
        bButton.setOnClickListener(this);
        cButton.setOnClickListener(this);
        dButton.setOnClickListener(this);
        eButton.setOnClickListener(this);
        fButton.setOnClickListener(this);
        gButton.setOnClickListener(this);
        hButton.setOnClickListener(this);
        iButton.setOnClickListener(this);
        jButton.setOnClickListener(this);
        kButton.setOnClickListener(this);
        lButton.setOnClickListener(this);
        mButton.setOnClickListener(this);
        nButton.setOnClickListener(this);
        oButton.setOnClickListener(this);
        pButton.setOnClickListener(this);
        qButton.setOnClickListener(this);
        rButton.setOnClickListener(this);
        sButton.setOnClickListener(this);
        tButton.setOnClickListener(this);
        uButton.setOnClickListener(this);
        vButton.setOnClickListener(this);
        wButton.setOnClickListener(this);
        xButton.setOnClickListener(this);
        yButton.setOnClickListener(this);
        zButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        audioPlayer = new AudioPlayer(view.getContext());

        view.bringToFront();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (loginString.length() > 0 && v.getId() == R.id.textViewBack) {
            loginString = loginString.substring(0, loginString.length() - 1);
        }
        else if (v.getId() != R.id.textViewBack){
            String resourceName = getResources().getResourceName(v.getId());

            Log.d("Keyboard", getResources().getResourceName(v.getId()));

            v.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.view_clicked));

            resourceName = resourceName.substring(resourceName.length() - 1);

            loginString = loginString + resourceName;
        }

        audioPlayer.playAudioFromRaw(R.raw.click);
        Log.d("Keyboard", "Login string: " + loginString);

        preferences.edit().putString("player", loginString).apply();
    }
}
