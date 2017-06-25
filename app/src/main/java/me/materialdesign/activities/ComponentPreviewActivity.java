package me.materialdesign.activities;

import android.os.Bundle;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.materialdesign.R;
import me.materialdesign.views.IndicatorView;


public class ComponentPreviewActivity extends BaseActivity {

    @Bind(R.id.indicator_view)
    IndicatorView indicatorView;
    @Bind(R.id.changed_btn)
    Button changedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_preview);
        ButterKnife.bind(this);
        changedBtn.setOnClickListener(v->indicatorView.setSelectedIndex());
    }
}
