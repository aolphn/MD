package me.materialdesign.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.materialdesign.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * RxJava探索之旅
 */
public class RxJavaActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rxjava_txt)
    TextView mRxjavaTxtView;
    private String TAG = "RxJavaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        init();
    }


    private void init(){
        rx.Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                Log.i(TAG, "call current thread:" + Thread.currentThread().getName());

                return Observable.just("one", "two", "three");
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber())
        ;
    }

    private Subscriber getSubscriber(){
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG,"complete current thread:"+Thread.currentThread().getName());
                Log.i(TAG,"this is complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG,"this is error");
            }

            @Override
            public void onNext(String o) {
                Log.i(TAG,"next current thread:"+Thread.currentThread().getName());
                Log.i(TAG,"this is next:"+o);

            }
        };
    }
}
