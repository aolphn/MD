package me.materialdesign.activities;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.materialdesign.BuildConfig;
import me.materialdesign.R;

/**
 * 关于APP界面
 */

public class AboutAppActivity extends BaseActivity {

    @Bind(R.id.package_name)
    TextView mPackageNameTxt;
    @Bind(R.id.channel_name)
    TextView mChannelNameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);
        mPackageNameTxt.setText(getPackageName());
        mChannelNameTxt.setText(BuildConfig.CHANNEL);
    }


}
