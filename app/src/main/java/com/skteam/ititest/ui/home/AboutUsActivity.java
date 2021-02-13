package com.skteam.ititest.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.skteam.ititest.R;

import java.net.URI;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        findViewById(R.id.btnItizone).setOnClickListener(v -> {

            openWebPage("https://www.itizone.com/");

        });
        findViewById(R.id.btnElectricianBlog).setOnClickListener(v -> {
            openWebPage("https://electrician-in-hindi.blogspot.com/");
        });
    }
    public void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request. Please install a web browser or check your URL.",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}