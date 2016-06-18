package de.toerb.apps.moodlight_android;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.slider.LightnessSlider;
import com.flask.colorpicker.slider.OnValueChangedListener;

import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class Moodlight extends AppCompatActivity {
    private static TextView textbox;
    private ColorPickerView colorPickerView;
    private LightnessSlider lightnessSlider;
    private int color;
    private float brightness = 1;





    OnColorSelectedListener onColorSelectedListener = new OnColorSelectedListener() {
        @Override
        public void onColorSelected(int selectedColor) {
            color = selectedColor;
            new SendRequest().execute();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moodlight);
        textbox = (TextView) findViewById(R.id.textView);
        textbox.setText("test");
        colorPickerView = (ColorPickerView) findViewById(R.id.color_picker_view);
        colorPickerView.addOnColorSelectedListener(onColorSelectedListener);
        lightnessSlider = (LightnessSlider) findViewById(R.id.v_lightness_slider);
        lightnessSlider.setOnValueChangedListener(onValueChangedListener);
    }

    OnValueChangedListener onValueChangedListener = new OnValueChangedListener() {
        @Override
        public void onValueChanged(float v) {
            brightness = v;
            new SendRequest().execute();
        }
    };

    private class SendRequest extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] = brightness;
            String h = Integer.toString((int) (hsv[0] / 360 * 255));
            String s = Integer.toString((int) (hsv[1] * 255));
            String v = Integer.toString((int) (hsv[2] * 255));
            try {
                new URL("http://192.168.0.105/hsv?h=" + h + "&s=" + s + "&v=" + v).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}