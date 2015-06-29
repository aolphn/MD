package la.xiaosiwo.laught.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import la.xiaosiwo.laught.R;

/**
 * Created by OF on 23:16.
 */
public class MenusAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private String[] mList;
    private Context mContext;
    public int getmSelectedPosition() {
        return mSelectedPosition;
    }

    public void setmSelectedPosition(int mSelectedPosition) {
        this.mSelectedPosition = mSelectedPosition;
    }

    private int mSelectedPosition;
    public MenusAdapter(Context context, String[]list){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        if(list == null){
            mList = new String[]{};
        }else{
            mList = list;
        }
    }
    @Override
    public int getCount() {
        return mList.length;
    }
    @Override
    public String getItem(int position) {
        return mList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        String content = getItem(position);
        if(convertView == null){
            view = mInflater.inflate(R.layout.drawer_list_item,null);
            holder = new ViewHolder();
            holder.content = (TextView)view.findViewById(R.id.menu_text);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }
        holder.content.setText(content);
        if(getmSelectedPosition() == position){
            view.setBackgroundResource(R.drawable.black_frame_white_content_radio);
            holder.content.setTextColor(mContext.getResources().getColor(R.color.pure_black));
        }else{
            view.setBackgroundResource(R.drawable.white_frame_black_content_radio);
            holder.content.setTextColor(mContext.getResources().getColor(R.color.pure_white));
        }
        return view;
    }
    class ViewHolder{
        TextView content;
    }
}
