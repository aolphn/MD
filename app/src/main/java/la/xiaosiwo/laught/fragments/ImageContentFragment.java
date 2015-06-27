package la.xiaosiwo.laught.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ypy.eventbus.EventBus;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.adapters.ImageImtesAdapter;
import la.xiaosiwo.laught.events.PrepareTextContentEvent;
import la.xiaosiwo.laught.events.UpdateImageContentUIEvent;
import la.xiaosiwo.laught.events.UpdateTextContentUIEvent;
import la.xiaosiwo.laught.manager.ImagesManager;
import la.xiaosiwo.laught.views.PullDownList;
import la.xiaosiwo.laught.views.PullDownList.OnPDListListener;

public class ImageContentFragment extends Fragment {

	private PullDownList mImageListview;
	private ImageImtesAdapter mAdapter;
	private TextView mNothingView;
	private String TAG = "TextContentFragment";
	protected OnPDListListener mPullDownListener = new OnPDListListener() {
		@Override
		public void onloadMore() {
			EventBus.getDefault().post(new PrepareTextContentEvent(PrepareTextContentEvent.LOAD_MORE_DATA));
		}

		@Override
		public void onRefresh() {
			mImageListview.stopRefresh(true);
			EventBus.getDefault().post(new PrepareTextContentEvent(PrepareTextContentEvent.REFRESH_DATA));
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.text_fragment_layout, null);

		mNothingView = (TextView) view.findViewById(R.id.nothing_text);
		mImageListview = (PullDownList)view.findViewById(R.id.text_content_list);
		mImageListview.setOnPDListen(mPullDownListener);
		registerEvent();
		EventBus.getDefault().post(new UpdateTextContentUIEvent());
		return view;
	}


	@Override
	public void onStop() {
		unregisterEvent();
		super.onStop();
	}


	public void onEventMainThread(UpdateImageContentUIEvent event){
		Log.i(TAG, "This is event-main-thread");
		mAdapter = new ImageImtesAdapter(getActivity(), ImagesManager.getInstance().getmImageItems());
		mImageListview.setAdapter(mAdapter);
		if(mAdapter.getCount() == 0){
			mNothingView.setVisibility(View.VISIBLE);
			mImageListview.setVisibility(View.INVISIBLE);
		}else{
			mNothingView.setVisibility(View.INVISIBLE);
			mImageListview.setVisibility(View.VISIBLE);
		}
	}

	private void registerEvent(){
		EventBus.getDefault().register(this);

	}
	private void unregisterEvent(){
		EventBus.getDefault().unregister(this);
	}
}
