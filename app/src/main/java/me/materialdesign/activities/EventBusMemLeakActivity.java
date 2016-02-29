package me.materialdesign.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.materialdesign.R;
import me.materialdesign.events.EventBusMemLeakTestEvent;

/**
 * 用于测试在Activity中进行耗事件处理会不会导致内存泄漏
 */
public class EventBusMemLeakActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private String TAG = "EventBusMemLeakActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_mem_leak);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"start post event");
                EventBus.getDefault().post(new EventBusMemLeakTestEvent());
            }
        },2000);
    }

    //事实证明这样处理事件会导致内存泄漏
    public void onEventAsync(EventBusMemLeakTestEvent event){
        Thread.currentThread().setName("thread-"+event.hashCode());
        Log.i(TAG,"receive one event in leak test");
        while (true){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            Log.i(TAG,"handle event in: "+Thread.currentThread().getName());
        }
    }
}
