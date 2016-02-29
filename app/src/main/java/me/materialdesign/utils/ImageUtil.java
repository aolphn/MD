/**   
 * @title ImageUtil.java
 * @package com.viewiot.asim.util
 * @author xuwenan  
 * @update 2014/4/12
 */
package me.materialdesign.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.Log;

import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author  xuweinan
 */

public class ImageUtil {
	/**
	 */
	public static byte[] bitmapToByte(Bitmap b) {
	    if (b == null) {
	        return null;
	    }

	    ByteArrayOutputStream o = new ByteArrayOutputStream();
	    // 压锟斤拷锟斤拷锟狡讹拷锟斤拷.PNG锟斤拷式
	    b.compress(CompressFormat.PNG, 100, o);
	    return o.toByteArray();
	}

	/**
	 */
	public static Bitmap byteToBitmap(byte[] b) {
	    return (b == null || b.length == 0) ? null : BitmapFactory
	            .decodeByteArray(b, 0, b.length);
	}

	/**
	 */
	public static Bitmap drawableToBitmap(Drawable d) {
	    return d == null ? null : ((BitmapDrawable) d).getBitmap();
	}

	/**
	 */
	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap b) {
	    return b == null ? null : new BitmapDrawable(b);
	}

	/**
	 */
	public static byte[] drawableToByte(Drawable d) {
	    return bitmapToByte(drawableToBitmap(d));
	}

	/**
	 *
	 * @return Drawable
	 */
	public static Drawable byteToDrawable(byte[] b) {
	    return bitmapToDrawable(byteToBitmap(b));
	}

	public static InputStream getInputStreamFromUrl(String imageUrl,
	        int readTimeOutMillis) {
	    InputStream stream = null;
	    Log.i("ImageUtil", "getInputStreamFromUrl(...),download image url:" + imageUrl);
	    try {
	        URL url = new URL(imageUrl);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        if (readTimeOutMillis > 0) {
	        	con.setConnectTimeout(readTimeOutMillis);
	            con.setReadTimeout(readTimeOutMillis);
	        }
	        stream = con.getInputStream();
	    } catch (MalformedURLException e) {
	        closeInputStream(stream);
	        throw new RuntimeException("MalformedURLException occurred. ", e);
	    } catch (IOException e) {
	        closeInputStream(stream);
	        e.printStackTrace();
	        throw new RuntimeException("IOException occurred. ", e);
	    }
	    return stream;
	}

	/**
	 * author:OF,time:2015-06-28 22:20:38.
	 * @param absolutePath
	 * @return
	 */
	public static InputStream getInputStreamFromLocalFile(String absolutePath) {
		File f = new File(absolutePath);
		if(f == null || !f.exists()){
			return null;
		}
		InputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return stream;
	}

	/**
	 * author:OF,time:2015-06-29 20:49:52.
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapFromLocal(String filePath){
		InputStream is = getInputStreamFromLocalFile(filePath);
		Bitmap bitmap = null;
		if(is != null){
			bitmap = BitmapFactory.decodeStream(is);
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	/**
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static Drawable getDrawableFromUrl(String imageUrl,
	        int readTimeOutMillis) {
	    InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOutMillis);
	    Drawable d = Drawable.createFromStream(stream, "src");
	    closeInputStream(stream);
	    return d;
	}

	/**
	 * 
	 * @param readTimeOut milliseconds
	 */
	public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut) {
	    InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOut);
	    Bitmap b = BitmapFactory.decodeStream(stream);
	    closeInputStream(stream);
	    return b;
	}
	
	public static byte[] getBytesFromUrl(String fileUrl, int readTimeOut) {
		 ByteArrayOutputStream outStream;
		 
		try {
		    InputStream inputStream = getInputStreamFromUrl(fileUrl, readTimeOut);
		    
		    outStream = new ByteArrayOutputStream();  
	        byte[] buffer = new byte[1024];  
	        int len = 0;  
	        while((len = inputStream.read(buffer)) != -1){  
	            outStream.write(buffer, 0, len);  
	        }  
		    closeInputStream(inputStream);
		    outStream.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        return outStream.toByteArray();  
	}
	
	/**
	 */
	public static ImageSize getVideoDisplaySizeByAvInfo(String info) {
		try {
			JSONObject avinfo = new JSONObject(info);
			JSONArray streams = avinfo.getJSONArray("streams");
			for (int i = 0; i < streams.length(); i ++) {
				JSONObject stream = streams.getJSONObject(i);
				if (stream.has("width") && stream.has("height")) {
					ImageSize is = new ImageSize(stream.getInt("width"), stream.getInt("height"));
					return is;
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	
	 /** 
     * @param bm 
     * @param fileName 
     * @throws IOException 
     */  
    public static void saveImage(Bitmap bm, String fileName) {  
    	try {
	        File myCaptureFile = new File(fileName);  
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
	        bm.compress(CompressFormat.JPEG, 85, bos);
	        bos.flush();  
	        bos.close();  
    	} catch (IOException e) {
	        throw new RuntimeException("IOException occurred when saving image: " + fileName, e);
    	}
    } 
    
	 /** 
     * @param fileName
     * @throws IOException 
     */  
    public static void saveFile(Context cntx, byte[] bytes, String fileName) throws IOException { 
    	try {
    		FileOutputStream fout = null;
    		String parentFolder = fileName.substring(0, fileName.lastIndexOf("/"));
    		File pFile = new File(parentFolder);
    		if(!pFile.exists()){
    			pFile.mkdirs();
    		}
	    	File file = new File(fileName);
	    	fout = new FileOutputStream(file);
	    	fout.write(bytes);
	    	fout.close(); 
    	} catch (IOException e) {
    		e.printStackTrace();
	        throw new RuntimeException("IOException occurred when saving file: " + fileName, e);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    } 
    
	/**
	 * 
	 */
	public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
	    return scaleImage(org, (float) newWidth / org.getWidth(),
	            (float) newHeight / org.getHeight());
	}

	/**
	 */
	public static Bitmap scaleImage(Bitmap org, float scaleWidth,
	        float scaleHeight) {
	    if (org == null) {
	        return null;
	    }

	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(),
				matrix, true);
	}
	
	/**
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}
	
	/**
	 * @param imgPath
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String imgPath,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(imgPath, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(imgPath, options);
	}
	
	
	/**
	*/
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; 
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
		ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
		ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	
	public static Bitmap getVideoThumbnail(String videoPath, int kind) {
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		return bitmap;
	}
	private static void closeInputStream(InputStream s) {
	    if (s == null) {
	        return;
	    }
	    try {
	        s.close();
	    } catch (IOException e) {
	        throw new RuntimeException("IOException occurred. ", e);
	    }
	}
	/**
	 * bug fix 1864
	 * @param bitmap
	 * @return
	 */
	public static Bitmap checkBitmapSize(Bitmap bitmap){
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		if(w<=4096&&h<=4096){
			return bitmap;
		}else{
			if(h>4096){
				float scale = (float)fix((double)4000/h, 100000000);
				return resizeBitmapByScale(bitmap,scale,false);
			}else if(w>4096){
				float scale = (float)fix((double)4000/w, 100000000);
				return resizeBitmapByScale(bitmap,scale,false);
			}
		}
		return bitmap;
	}
  public static Bitmap resizeBitmapByScale(
            Bitmap bitmap, float scale, boolean recycle) {
        int width = Math.round(bitmap.getWidth() * scale);
        int height = Math.round(bitmap.getHeight() * scale);
        if (width == bitmap.getWidth()&& height == bitmap.getHeight()) return bitmap;
        Bitmap target = Bitmap.createBitmap(width, height, getConfig(bitmap));
        Canvas canvas = new Canvas(target);
        canvas.scale(scale, scale);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (recycle) bitmap.recycle();
        return target;
    }

	 private static Bitmap.Config getConfig(Bitmap bitmap) {
	        Bitmap.Config config = bitmap.getConfig();
	        if (config == null) {
	            config = Bitmap.Config.ARGB_8888;
	        }
	        return config;
	    }
	private static  double fix(double val, int fix) {
		if (val == 0)
			return (double)val;
		int p = (int) Math.pow(10, fix);
		return (double) ((int) (val * p)) / p;
	}
	


    /**
	 * 根据路径获得突破并压缩返回bitmap
	 * 
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		int degree = readPictureDegree(filePath); 
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap cbitmap=BitmapFactory.decodeFile(filePath, options);
		Bitmap newbitmap = ImageUtil.rotaingImageView(degree, cbitmap);
		return newbitmap;
	}
	
	/**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
    /*
     * 旋转图片 
     * @param angle 
     * @param bitmap 
     * @return Bitmap 
     */ 
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
        //旋转图片 动作   
        Matrix matrix = new Matrix();;  
        matrix.postRotate(angle);  
//        System.out.println("angle2=" + angle);  
        // 创建新的图片   
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
        return resizedBitmap;  
    }


	public static Bitmap zoomBitmap(Bitmap bitmap,float scaleWFactor,float scaleHFactor) {
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWFactor,scaleHFactor); //长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		return resizeBmp;
	}
}
