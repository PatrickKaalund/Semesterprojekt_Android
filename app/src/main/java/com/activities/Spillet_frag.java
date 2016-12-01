package com.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * @author Jacob Nordfalk
 */
public class Spillet_frag extends Fragment implements View.OnClickListener {

  private TextView info;
  private Button spilKnap;
  private EditText et;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d("Velkomst_frag", "fragmentet blev vist!");

    // Programmatisk fragment_multiplayer
    TableLayout tl = new TableLayout(getActivity());

    info = new TextView(getActivity());
    info.setText("Velkommen til mit fantastiske spil." +
            "\nDu skal gætte dette ord: "+
            "\nSkriv et bogstav herunder og tryk 'Spil'.\n");

    tl.addView(info);

    et = new EditText(getActivity());
    et.setHint("Skriv et bogstav her.");
    tl.addView(et);

    spilKnap = new Button(getActivity());
    spilKnap.setText("Spil");
    spilKnap.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0);
    tl.addView(spilKnap);

    spilKnap.setOnClickListener(this);

    return tl;
  }

  @Override
  public void onClick(View view) {
    String bogstav = et.getText().toString();
    if (bogstav.length() != 1) {
      et.setError("Skriv præcis ét bogstav");
      return;
    }

    et.setText("");
    et.setError(null);
  }

}