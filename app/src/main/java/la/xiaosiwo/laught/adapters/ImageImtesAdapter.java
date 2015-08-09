package la.xiaosiwo.laught.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.models.ImageLaughterItem;

/**
 * Created by OF on 2015/6/24 0024.
 */
public class ImageImtesAdapter extends RecyclerView.Adapter {

    private String TAG = "ImageImtesAdapter";
    private LayoutInflater mInflater;
    private ArrayList<ImageLaughterItem> mList;
    private final RecyclerView recyclerView;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    public void setmImageClickListener(View.OnClickListener mImageClickListener) {
        this.mImageClickListener = mImageClickListener;
    }

    private View.OnClickListener mImageClickListener;
    public ImageImtesAdapter(Context context, ArrayList<ImageLaughterItem> list, ImageLoader loder, RecyclerView lv){
        mInflater = LayoutInflater.from(context);
        if(list == null){
            mList = new ArrayList<>();
        }else{
            mList = list;
        }
        mImageLoader = loder;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.default_img)
                .showImageOnFail(R.drawable.default_img)
                .cacheInMemory(false)
                .cacheOnDisc(false)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        recyclerView = lv;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    // 怒刷存在感


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder)viewHolder;
        final ImageLaughterItem item = mList.get(i);
        final ImageView leftImage = holder.bitmap;
        holder.bitmap.setImageResource(R.drawable.default_img);
        Log.i(TAG, "position:" + i);
        holder.bitmap.setTag("file://" + item.getmUrl());
        holder.textView.setText("file://" + item.getmUrl());
        if (mImageClickListener != null){
            holder.bitmap.setOnClickListener(mImageClickListener);
        }
//        mImageLoader.loadImage("file://" + item.getmUrl(), null, options, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                Log.i(TAG, "image uri:" + imageUri + ",holder tag:" + leftImage.getTag());
//                if (imageUri.equals(leftImage.getTag())) {
//                    ImageView img = ((ImageView) recyclerView.findViewWithTag(imageUri));
//                    if (img != null) {
//                        img.setImageBitmap(loadedImage);
//
//                        img.setTag("");
//                        img.setTag(R.drawable.default_img, item.getmUrl());
//                    }
//                }
//            }
//        });
        mImageLoader.displayImage("file://" + item.getmUrl(),holder.bitmap,options);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item_layout,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        holder.textView = (TextView)view.findViewById(R.id.text_description);
        holder.bitmap = (ImageView) view.findViewById(R.id.image_content);
        holder.setIsRecyclable(false);
        return holder;
    }


    public  static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView bitmap;
        public ViewHolder(View view){
         super(view);
        }
    }
}
