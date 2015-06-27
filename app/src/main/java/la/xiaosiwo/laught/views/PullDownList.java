package la.xiaosiwo.laught.views;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import la.xiaosiwo.laught.R;

public class PullDownList extends ListView implements
		AbsListView.OnScrollListener {
	public PullDownList(Context context) {
		super(context);
		init();
	}

	public PullDownList(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullDownList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	private ImageView imageView_top = null;
	private TextView mHintText = null;
	private LinearLayout topViewItem = null;
	private int topViewItemHeight = 0;
	private boolean isFirstItemShow = false;
	private boolean isWantToExtrude = true;
	private boolean isExtruded = false;
	private boolean isLoading = false;
	private static final int REFRESH_THRESHOLD = 30;
	private void init() {
		setOnScrollListener(this);
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		topViewItem = (LinearLayout) layoutInflater.inflate(
				R.layout.base_list_item_top, this, false);
		topViewItem.setOnClickListener(null);
		imageView_top = (ImageView) topViewItem.findViewById(R.id.load_more_history_msg_img);
		mHintText = (TextView) topViewItem.findViewById(R.id.load_more_text_hint);
		measureView(topViewItem);
		topViewItemHeight = topViewItem.getMeasuredHeight();
		topViewItem.setPadding(topViewItem.getPaddingLeft(), -1
				* topViewItemHeight, topViewItem.getPaddingRight(),
				topViewItem.getPaddingBottom());
		addHeaderView(topViewItem);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (isLoading)
		{
			return;
		}
		if (scrollState == SCROLL_STATE_IDLE) {
			if (isFirstItemShow) {
				isWantToExtrude = true;
			} else {
				isWantToExtrude = false;
			}
		}
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && onPDListListener != null) {
			if ((view.getLastVisiblePosition() == view
					.getCount() - 1)
					&& view.getCount()> 1) {
				this.onPDListListener.onloadMore();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (isLoading)
		{
			return;
		}
		if (firstVisibleItem == 0) {
			isFirstItemShow = true;
			isWantToExtrude = true;
		} else {
			isFirstItemShow = false;
			isWantToExtrude = false;
		}
	}

	private float lastActionDown_Y = 0;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean result = super.onTouchEvent(ev);
		if (isLoading || null == this.onPDListListener)
		{
			return result;
		}

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
			lastActionDown_Y = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float gapValue_y = ev.getY() - lastActionDown_Y;

			if (isWantToExtrude ) {
				if (gapValue_y > 0) {
					isExtruded = true;
					int newPaddingTop = (int) ((int) gapValue_y/2.7 - topViewItemHeight);
					if(newPaddingTop >=REFRESH_THRESHOLD){
						newPaddingTop = REFRESH_THRESHOLD;
					}
					topViewItem.setPadding(0, newPaddingTop, 0, 0);
					if (newPaddingTop >= REFRESH_THRESHOLD) {
						setControl(2);
					} else {
						setControl(1);
					}
				} else {
				}
			} else {
				if (isExtruded) {
					isExtruded = false;
					topViewItem.setPadding(0, -1 * topViewItemHeight, 0, 0);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (isExtruded) {
				if (topViewItem.getPaddingTop() >= REFRESH_THRESHOLD) {
					if (this.onPDListListener != null) {
						isLoading = true;
						topViewItem.setPadding(0, 0, 0, 0);
						setControl(3);
						this.onPDListListener.onRefresh();
					} else {
						topViewItem.setPadding(0, -1 * topViewItemHeight, 0, 0);
						isExtruded = false;
					}
				} else {
					topViewItem.setPadding(0, -1 * topViewItemHeight, 0, 0);
					isExtruded = false;
				}
			}
			break;

		default:
			break;
		}

		return result;
	}

	private int controlState = -1;
	private void setControl(int state) {
		if (controlState != state) {
			controlState = state;
			if (state == 0) {
				topViewItem.setVisibility(View.INVISIBLE);
				((AnimationDrawable) imageView_top.getBackground()).stop();
			} else if (state == 1) {
				topViewItem.setVisibility(View.VISIBLE);
				mHintText.setText(getContext().getString(R.string.pull_down_for_refresh));
				((AnimationDrawable) imageView_top.getBackground()).stop();
			} else if (state == 2) {
				topViewItem.setVisibility(View.VISIBLE);
				mHintText.setText(getContext().getString(R.string.release_refresh));
				((AnimationDrawable) imageView_top.getBackground()).stop();
			} else if (state == 3) {
				//refreshing
				topViewItem.setVisibility(View.VISIBLE);
				mHintText.setText(getContext().getString(R.string.in_refresh));
				((AnimationDrawable) imageView_top.getBackground()).start();
			}
		}

	}

	public void setOnPDListen(OnPDListListener onPDListListener) {
		if(null == this.onPDListListener ){
			this.onPDListListener = onPDListListener;
		}
	}

	public void removePDListen() {
		this.onPDListListener = null;
	}

	
	public void startRefresh() {
		if (!isLoading && this.onPDListListener != null && controlState == 2) {
			isLoading = true;
			topViewItem.setPadding(0, 0, 0, 0);
			setControl(3);
//			setSelection(0);
			this.onPDListListener.onRefresh();
		}
	}

	public void stopRefresh(boolean isUpdateTime) {
		if (isLoading) {
			isFirstItemShow = false;
			isWantToExtrude = true;
			isExtruded = false;
			isLoading = false;
			setControl(0);
			topViewItem.setPadding(0, -1 * topViewItemHeight, 0, 0);
//			setSelection(0);
		}
	}

	private static void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
	
	private OnPDListListener onPDListListener = null;

	public interface OnPDListListener {
		public void onRefresh();
		public void onloadMore();
	}

}