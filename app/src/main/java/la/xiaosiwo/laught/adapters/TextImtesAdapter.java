package la.xiaosiwo.laught.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.models.TextLaughterItem;

/**
 * Created by 克虎 on 2015/6/24 0024.
 */
public class TextImtesAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<TextLaughterItem> mList;
    public TextImtesAdapter(Context context,ArrayList<TextLaughterItem> list){
        mInflater = LayoutInflater.from(context);
        if(list == null){
            mList = new ArrayList<TextLaughterItem>();
        }else{
            mList = list;
        }
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public TextLaughterItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        TextLaughterItem item = getItem(position);
        if(convertView == null){
            view = mInflater.inflate(R.layout.text_item_layout,null);
            holder = new ViewHolder();
            holder.content = (TextView)view.findViewById(R.id.text_content);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }
        holder.content.setText(item.getmContent());
        return view;
    }
    class ViewHolder{
        TextView content;
    }
}
