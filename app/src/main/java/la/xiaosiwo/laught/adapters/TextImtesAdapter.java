package la.xiaosiwo.laught.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.models.TextLaughterItem;

/**
 * Created by OF on 2015/6/24 0024.
 */
public class TextImtesAdapter extends RecyclerView.Adapter {

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
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_item_layout,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder)viewHolder;
        final TextLaughterItem item = mList.get(i);
        holder.content.setText(item.getmContent());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        public ViewHolder(View view){
            super(view);
        }
    }
}
