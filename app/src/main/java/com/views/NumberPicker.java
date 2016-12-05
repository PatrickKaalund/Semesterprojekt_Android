package com.views;

import java.lang.reflect.Method;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.patrickkaalund.semesterprojekt_android.R;

public class NumberPicker {
    private Object picker;
    private Class<?> classPicker;


    public NumberPicker(RelativeLayout numberPickerView) {
        picker = numberPickerView;
        classPicker = picker.getClass();

        // Кнопка '+', тип - NumberPickerButton
    //    View upButton = numberPickerView.getChildAt(0);
        //upButton.setBackgroundResource(R.drawable.);

        // Текстовое поле, тип - EditText
        TextView edDate = (TextView) numberPickerView.getChildAt(1);
        edDate.setTextSize(17);
        edDate.setBackgroundResource(R.drawable.selector_options);

        // Кнопка '-', тип - NumberPickerButton
       View downButton = numberPickerView.getChildAt(2);
       // downButton.setBackgroundResource(R.drawable.ak);
    }

    public void setRange(int start, int end) {
        try {
            Method m = classPicker.getMethod("setRange", int.class, int.class);
            m.invoke(picker, start, end);
        } catch (Exception e) {
        }
    }

    public Integer getCurrent() {
        Integer current = -1;
        try {
            Method m = classPicker.getMethod("getCurrent");
            current = (Integer) m.invoke(picker);
        } catch (Exception e) {
        }
        return current;
    }

    public void setCurrent(int current) {
        try {
            Method m = classPicker.getMethod("setCurrent", int.class);
            m.invoke(picker, current);
        } catch (Exception e) {
        }
    }
}