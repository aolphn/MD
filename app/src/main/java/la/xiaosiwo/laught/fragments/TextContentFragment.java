package la.xiaosiwo.laught.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ypy.eventbus.EventBus;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.adapters.TextImtesAdapter;
import la.xiaosiwo.laught.events.UpdateTextContentUIEvent;
import la.xiaosiwo.laught.manager.TextsManager;

public class TextContentFragment extends Fragment {

	private RecyclerView recyclerView;
	private TextImtesAdapter mAdapter;
	private TextView mNothingView;
	private String TAG = "TextContentFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_layout, null);
		Button tv = (Button) view.findViewById(R.id.fragment_tag);
		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new UpdateTextContentUIEvent());
			}
		});
		mNothingView = (TextView) view.findViewById(R.id.nothing_text);
		recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
		recyclerView.setAdapter(mAdapter);
		if(mAdapter.getItemCount() == 0){
			mNothingView.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.INVISIBLE);
		}else{
			mNothingView.setVisibility(View.INVISIBLE);
			recyclerView.setVisibility(View.VISIBLE);
		}
	}

	private void registerEvent(){
		EventBus.getDefault().register(this);

	}
	private void unregisterEvent(){
		EventBus.getDefault().unregister(this);
	}
}
