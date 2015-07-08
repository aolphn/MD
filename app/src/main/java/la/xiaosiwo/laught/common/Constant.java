package la.xiaosiwo.laught.common;

import android.os.Environment;

import la.xiaosiwo.laught.utils.FileUtil;

public class Constant {

	public static final String RECORD_VIDEO_KEY = "key";
	public static final String RECORD_VIDEO_PATH = "path";
	public static final String RECORD_VIDEO_CAPTURE = "capture";
	public static String SDCARD_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String APP_ROOT_PATH = SDCARD_ROOT_PATH+"/Laughter";
	public final static String CACHE_PATH = FileUtil.getAppRootPath() + "/AppData";
	public final static String LOG_PATH = FileUtil.getAppRootPath() + "/log";
	/**
	 * If SDcard usable space less than this value then we need hint low space.
	 */
	public final static long LOW_CAPACITY_THRESHOLD = 40*1024;
}
