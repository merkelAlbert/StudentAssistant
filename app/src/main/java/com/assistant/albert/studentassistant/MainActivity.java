package com.assistant.albert.studentassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.assistant.albert.studentassistant.studentassistant.homework.HomeWorkItem;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout root;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (!item.isChecked()) {
                        ConstraintLayout homework = (ConstraintLayout) getLayoutInflater().inflate(R.layout.homework, root, false);
                        root.addView(homework);
                        HomeWorkItem.onSelected(MainActivity.this);
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if (!item.isChecked()) {
                        root.removeViewInLayout(findViewById(R.id.homework));
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        root = findViewById(R.id.root);
        ConstraintLayout homework = (ConstraintLayout) getLayoutInflater().inflate(R.layout.homework, root, false);
        root.addView(homework);
        HomeWorkItem.onSelected(MainActivity.this);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
