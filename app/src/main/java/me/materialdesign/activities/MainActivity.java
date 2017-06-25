package me.materialdesign.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.materialdesign.R;

/**
 * author:OF,time:2015-06-28 21:51:12.
 */
public class MainActivity extends BaseActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    ActionBarDrawerToggle drawerToggle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.fabBtn)
    FloatingActionButton fabBtn;
    @Bind(R.id.rootLayout)
    CoordinatorLayout rootLayout;
    @Bind(R.id.navigation)
    NavigationView navigation;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    Button mAboutApp;
    @Bind(R.id.about_app)
    Button aboutApp;
    @Bind(R.id.component_preview)
    Button mComponentPreview;
    @Bind(R.id.event_bus_mem_leak_test_btn)
    Button eventBusMemLeakTestBtn;
    @Bind(R.id.rxjava)
    Button mRxjavaBtn;
    @Bind(R.id.btn_hook)
    Button mHookBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);
        initToolbar();
        initInstances();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @OnClick({R.id.about_app})
    public void aboutApp() {
        Intent intent = new Intent(this, AboutAppActivity.class);
        startActivity(intent);
    }

//    @OnClick({R.id.event_bus_mem_leak_test_btn})
//    public void eventBusMemLeakTest() {
//
//    }

    @OnClick({R.id.component_preview})
    public void previewComponent() {
        Intent intent = new Intent(this, ComponentPreviewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fabBtn)
    public void clickFabBtn() {
        Snackbar.make(rootLayout, "Hello. I am Snackbar!", Snackbar.LENGTH_SHORT)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }

    private void initInstances() {
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(getString(R.string.app_name));
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_pattern_lock) {
            Intent intent = new Intent(MainActivity.this, SetPatternPwdActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.auto_zoom_effect) {
            Intent intent = new Intent(MainActivity.this, AutoZoomImageActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick ({R.id.rxjava,R.id.event_bus_mem_leak_test_btn,R.id.btn_hook})
    public void btnClickEvent(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.event_bus_mem_leak_test_btn:
                intent = new Intent(this, EventBusMemLeakActivity.class);
                startActivity(intent);
                break;
            case R.id.rxjava:
                intent = new Intent(this, RxJavaActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_hook:
                intent = new Intent(this, ProxyOtherActivity.class);
                startActivity(intent);
                break;
        }

    }
}
