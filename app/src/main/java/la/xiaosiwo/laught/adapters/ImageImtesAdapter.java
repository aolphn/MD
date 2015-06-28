package la.xiaosiwo.laught.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;

import la.xiaosiwo.laught.R;
import la.xiaosiwo.laught.models.ImageLaughterItem;

/**
 * Created by OF on 2015/6/24 0024.
 */
public class ImageImtesAdapter extends BaseAdapter {

    private String TAG = "ImageImtesAdapter";
    private LayoutInflater mInflater;
    private ArrayList<ImageLaughterItem> mList;
    private final ListView mListview;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    public void setmImageClickListener(View.OnClickListener mImageClickListener) {
        this.mImageClickListener = mImageClickListener;
    }

    private View.OnClickListener mImageClickListener;
    public ImageImtesAdapter(Context context, ArrayList<ImageLaughterItem> list, ImageLoader loder, ListView lv){
        mInflater = LayoutInflater.from(context);
        if(list == null){
            mList = new ArrayList<ImageLaughterItem>();
        }else{
            mList = list;
        }
        mImageLoader = loder;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.default_img)
                .showImageOnFail(R.drawable.default_img)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)

                .build();
        mListview = lv;
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public ImageLaughterItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        final ImageLaughterItem item = getItem(position);
        if(convertView == null){
            view = mInflater.inflate(R.layout.image_item_layout,null);
            holder = new ViewHolder();
            holder.content = (TextView)view.findViewById(R.id.text_description);
            holder.image = (ImageView)view.findViewById(R.id.image_content);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }
        final ImageView leftImage = holder.image;

        leftImage.setImageResource(R.drawable.default_img);
        Log.i(TAG,"position:"+position);
        holder.image.setTag("file://" + item.getmUrl());
        holder.content.setText("file://" + item.getmUrl());
        if (mImageClickListener != null){
            holder.image.setOnClickListener(mImageClickListener);
        }
//        mImageLoader.displayImage("file://"+item.getmUrl(),new ImageViewAware(leftImage));
        mImageLoader.loadImage("file://" + item.getmUrl(), null, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.i(TAG, "image uri:" + imageUri + ",holder tag:" + leftImage.getTag());
                if (imageUri.equals( leftImage.getTag())) {
                   ImageView img  = ((ImageView) mListview.findViewWithTag(imageUri));
                    if (img !=null){
                        img.setImageBitmap(loadedImage);
                        img.setTag("");
                        img.setTag(R.drawable.default_img,item.getmUrl());
                    }
//                    leftImage.setImageBitmap(loadedImage);
                }
            }
        });
//        holder.image.setImageResource(R.drawable.default_img);
        return view;
    }
    class ViewHolder{
        TextView content;
        ImageView image;
    }
}
