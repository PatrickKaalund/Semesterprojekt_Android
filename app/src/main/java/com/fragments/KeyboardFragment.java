package com.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.OurGLSurfaceView;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;


public class KeyboardFragment extends Fragment implements View.OnClickListener {

    private View view;
    private AudioPlayer audioPlayer;
    static private String loginString = "";
    private SharedPreferences preferences;
    private GLSurfaceView glSurfaceView;
    static private SpriteEntityFactory spriteEntityFactory;
    static private ArrayList<Entity> drawers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_keyboard, container, false);
        this.view = view;

        glSurfaceView = new OurGLSurfaceView(getActivity().getApplicationContext());

        // Inflate glSurfaceView on top of running activity
        ((Activity) view.getContext()).getWindow().addContentView(glSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        glSurfaceView.setZOrderOnTop(true);

        spriteEntityFactory = new SpriteEntityFactory(R.drawable.letters_red, 100,100, 16, 2, new PointF(0,0));

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        audioPlayer = new AudioPlayer(view.getContext());

        drawers = new ArrayList<>();
        startDrawers(drawers, 0, 80);

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

        return view;
    }

    @Override
    public void onDestroy() {
        // Delete sprite factory and remove glSurfaceView
        spriteEntityFactory.delete();
        glSurfaceView.setVisibility(View.INVISIBLE);
        loginString = "";
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (loginString.length() > 0 && v.getId() == R.id.textViewBack) {
            drawers.get(loginString.length() - 1).setCurrentSprite(31);
            loginString = loginString.substring(0, loginString.length() - 1);
        }
        else if (v.getId() != R.id.textViewBack && loginString.length() < 6){
            String resourceName = getResources().getResourceName(v.getId());

            Log.d("Keyboard", getResources().getResourceName(v.getId()));

            resourceName = resourceName.substring(resourceName.length() - 1);

            loginString += resourceName;
            drawers.get(loginString.length() - 1).setCurrentSprite(loginString.charAt(loginString.length() - 1) - 65);
        }
        v.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.view_clicked));
        audioPlayer.playAudioFromRaw(R.raw.click);
        Log.d("Keyboard", "Login string: " + loginString);
        glSurfaceView.requestRender();
        preferences.edit().putString("player", loginString).apply();
    }

    public void startDrawers(ArrayList<Entity> drawers, int xOffset, int yOffset) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();

        for (int i = 0; i < 6; i++) {
            Entity drawer = spriteEntityFactory.createEntity();
            drawer.placeAt(displayMetrics.widthPixels / 2 - 185 + (i * yOffset), displayMetrics.heightPixels / 2 + (i * xOffset));
            drawer.setCurrentSprite(31);
            drawers.add(drawer);
        }
    }
}
