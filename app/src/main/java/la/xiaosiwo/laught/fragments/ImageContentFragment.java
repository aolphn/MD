package la.xiaosiwo.laught.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ypy.eventbus.EventBus;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.adapters.ImageImtesAdapter;
import la.xiaosiwo.laught.events.UpdateImageContentUIEvent;
import la.xiaosiwo.laught.listener.ImageClickListener;
import la.xiaosiwo.laught.manager.ImagesManager;

public class ImageContentFragment extends Fragment {

	private RecyclerView mRecyclerView;
	private ImageImtesAdapter mAdapter;
	private TextView mNothingView;
	private String TAG = "ImageContentFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_layout, null);

		mNothingView = (TextView) view.findViewById(R.id.nothing_text);
		mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setItemViewCacheSize(50);

		registerEvent();
		EventBus.getDefault().post(new UpdateImageContentUIEvent());
		return view;
	}


	@Override
	public void onStop() {
		unregisterEvent();
		super.onStop();
	}


	public void onEventMainThread(UpdateImageContentUIEvent event){
		Log.i(TAG, "This is event-main-thread-in-image-fragment");
		if(mAdapter == null){
			mAdapter = new ImageImtesAdapter(getActivity(), ImagesManager.getInstance().getmImageItems(), ImageLoader.getInstance(), mRecyclerView);
			mAdapter.setmImageClickListener(new ImageClickListener(getActivity()));
			mRecyclerView.setAdapter(mAdapter);
		}

		if(mAdapter.getItemCount() == 0){
			mNothingView.setVisibility(View.VISIBLE);
			mRecyclerView.setVisibility(View.INVISIBLE);
		}else{
			mAdapter.notifyDataSetChanged();
			mNothingView.setVisibility(View.INVISIBLE);
			mRecyclerView.setVisibility(View.VISIBLE);
		}
	}

	private void registerEvent(){
		EventBus.getDefault().register(this);

	}
	private void unregisterEvent(){
		EventBus.getDefault().unregister(this);
	}
}
