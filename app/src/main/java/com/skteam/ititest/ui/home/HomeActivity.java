
package com.skteam.ititest.ui.home;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.skteam.ititest.R;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

         drawer = findViewById(R.id.drawer_layout);

setNavigationViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }




    /**
     * Navigation drawer listener
     */
    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft = ft.replace(R.id.nav_host_fragment, new HomeFragment());
                ft.addToBackStack(null);
                ft.commit();
                break;
//
//            case R.id.nav_notifications:
//                startActivity(new Intent(activity, NotificationActivity.class));
//                break;
//
//            case R.id.nav_logout:
//                Toast.makeText(activity, "clicked on logout", Toast.LENGTH_SHORT).show();
//                Helper.setLoggedInUser(sharedPreferenceUtil, null);
//                startActivity(new Intent(activity, LogRegActivity.class));
//                finish();
//                break;

        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}