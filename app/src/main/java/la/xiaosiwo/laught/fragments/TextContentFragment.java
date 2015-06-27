package la.xiaosiwo.laught.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ypy.eventbus.EventBus;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.adapters.TextImtesAdapter;
import la.xiaosiwo.laught.events.PrepareTextContentEvent;
import la.xiaosiwo.laught.events.TextsManager;
import la.xiaosiwo.laught.events.UpdateTextContentUIEvent;
import la.xiaosiwo.laught.views.PullDownList;
import la.xiaosiwo.laught.views.PullDownList.OnPDListListener;

public class TextContentFragment extends Fragment {

	private PullDownList mTextListview;
	private TextImtesAdapter mAdapter;
	private TextView mNothingView;
	private String TAG = "TextContentFragment";
	protected OnPDListListener mPullDownListener = new OnPDListListener() {
		@Override
		public void onloadMore() {
			EventBus.getDefault().post(new PrepareTextContentEvent(PrepareTextContentEvent.LOAD_MORE_DATA));
		}

		@Override
		public void onRefresh() {
			mTextListview.stopRefresh(true);
			EventBus.getDefault().post(new PrepareTextContentEvent(PrepareTextContentEvent.REFRESH_DATA));
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.text_fragment_layout, null);
		Button tv = (Button) view.findViewById(R.id.fragment_tag);
		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new UpdateTextContentUIEvent());
			}
		});
		mNothingView = (TextView) view.findViewById(R.id.nothing_text);
		mTextListview = (PullDownList)view.findViewById(R.id.text_content_list);
		mTextListview.setOnPDListen(mPullDownListener);
//		String tag = this.getArguments().getStr\ing("key");
		tv.setText(getString(R.string.text_laughter));
		registerEvent();
		EventBus.getDefault().post(new UpdateTextContentUIEvent());
		return view;
	}


	@Override
	public void onStop() {
		unregisterEvent();
		super.onStop();
	}


	public void onEventMainThread(UpdateTextContentUIEvent event){
		Log.i(TAG, "This is event-main-thread");
		mAdapter = new TextImtesAdapter(getActivity(),TextsManager.getInstance().getmTextItems());
		mTextListview.setAdapter(mAdapter);
		if(mAdapter.getCount() == 0){
			mNothingView.setVisibility(View.VISIBLE);
			mTextListview.setVisibility(View.INVISIBLE);
		}else{
			mNothingView.setVisibility(View.INVISIBLE);
			mTextListview.setVisibility(View.VISIBLE);
		}
	}

	private void registerEvent(){
		EventBus.getDefault().register(this);

	}
	private void unregisterEvent(){
		EventBus.getDefault().unregister(this);
	}
}
