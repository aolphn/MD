package la.xiaosiwo.laught.utils;


import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import la.xiaosiwo.laught.common.Constant;

/**
 * 
 *
 */
public class FileUtil {
	private static final String TAG = "FileUtil";

	/**
	 *
	 * @param fromFile
	 * @param toFile
	 * @throws IOException
	 */
	public static void copyFile(File fromFile, String toFile)
			throws IOException {

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead); // write
		} finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
		}
	}
	
	public static void copyFileWithLen(File fromFile, String toFile, long len) {

		long totalLen = 0;
		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[1024];
			int bytesRead = 0;

			while ((bytesRead = from.read(buffer)) != -1) {
				totalLen += bytesRead;
				if (totalLen > len) {
					to.write(buffer, 0, (int)(bytesRead - (totalLen - len))); // write
					break;
				}
				else {
					to.write(buffer, 0, bytesRead); // write
				}
			}
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
		}
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public static File createNewFile(File file) {

		try {

			if (file.exists()) {
				return file;
			}

			File dir = file.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			Log.e(TAG, "", e);
			return null;
		}
		return file;
	}

	/**
	 *
	 * @param path
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		return createNewFile(file);
	}// end method createText()

	/**
	 *
	 * @param path
	 */
	public static void deleteFile(String path) {
		if(path==null){
			return;
		}
		File file = new File(path);
		deleteFile(file);
	}

	/**
	 *
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	}

	/**
	 *
	 * @param content
	 * @return
	 */
	public static boolean write(String path, String content) {
		return write(path, content, false);
	}

	public static boolean write(String path, String content, boolean append) {
		return write(new File(path), content, append);
	}

	public static boolean write(File file, String content) {
		return write(file, content, false);
	}

	public static boolean write(File file, String content, boolean append) {
		if (file == null || StringUtil.empty(content)) {
			return false;
		}
		if (!file.exists()) {
			file = createNewFile(file);
		}
		FileOutputStream ops = null;
		try {
			ops = new FileOutputStream(file, append);
			ops.write(content.getBytes());
			ops.flush();
		} catch (Exception e) {
			Log.e(TAG, "", e);
			return false;
		} finally {
			try {
				ops.close();
			} catch (IOException e) {
				Log.e(TAG, "", e);
			}
			ops = null;
		}

		return true;
	}

	/**
	 * 锟斤拷锟斤拷募锟斤拷锟�
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		if (StringUtil.empty(path)) {
			return null;
		}
		File f = new File(path);
		String name = f.getName();
		f = null;
		return name;
	}

	/**
	 * 锟斤拷取锟侥硷拷锟斤拷锟捷ｏ拷锟接碉拷startLine锟叫匡拷始锟斤拷锟斤拷取lineCount锟斤拷
	 * 
	 * @param file
	 * @param startLine
	 * @param lineCount
	 * @return 锟斤拷锟斤拷锟斤拷锟街碉拷list,锟斤拷锟�list.size<lineCount锟斤拷说锟斤拷锟斤拷锟斤拷锟侥硷拷末尾锟斤拷
	 */
	public static List<String> readFile(File file, int startLine, int lineCount) {
		if (file == null || startLine < 1 || lineCount < 1) {
			return null;
		}
		if (!file.exists()) {
			return null;
		}
		FileReader fileReader = null;
		List<String> list = null;
		try {
			list = new ArrayList<String>();
			fileReader = new FileReader(file);
			LineNumberReader lnr = new LineNumberReader(fileReader);
			boolean end = false;
			for (int i = 1; i < startLine; i++) {
				if (lnr.readLine() == null) {
					end = true;
					break;
				}
			}
			if (end == false) {
				for (int i = startLine; i < startLine + lineCount; i++) {
					String line = lnr.readLine();
					if (line == null) {
						break;
					}
					list.add(line);

				}
			}
		} catch (Exception e) {
			Log.e(TAG, "read log error!", e);
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * 锟斤拷锟斤拷锟侥硷拷锟斤拷
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean createDir(File dir) {
		try {
			if (!dir.exists()) {
				return dir.mkdirs();
			}
			return true;
		} catch (Exception e) {
			Log.e(TAG, "create dir error", e);
			return false;
		}
	}

	/**
	 * 锟斤拷SD锟斤拷锟较达拷锟斤拷目录
	 * 
	 * @param dirName
	 */
	public static File creatSDDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 锟叫讹拷SD锟斤拷锟较碉拷锟侥硷拷锟角凤拷锟斤拷锟�
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
	public static long getFileSize(String fileName) {
		File file = new File(fileName);
		return file.exists()?file.length():0;
	}
	/**
	 * 锟斤拷一锟斤拷InputStream锟斤拷锟斤拷锟斤拷锟斤拷锟叫达拷氲�SD锟斤拷锟斤拷
	 */
	public static File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = createNewFile(path + "/" + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[1024];
			int len = -1;
			while ((len = input.read(buffer)) != -1) {
				output.write(buffer, 0, len);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static String readFile(File file) {
		Reader read = null;
		String content = "";
		String string = "";
		BufferedReader br = null;
		try {
			read = new FileReader(file);
			br = new BufferedReader(read);
			while ((content = br.readLine()) != null) {
				string += content.trim() + "\r\n";
			}
		} catch (Exception e) {
//			e.printStackTrace();
		} finally {
			try {
				if(read != null){
					read.close();
				}
				if(br != null){
					br.close();
				}
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
//		System.out.println("string=" + string);
		return string.toString();
	}
	
	public static String readFileCompact(File file) {
		Reader read = null;
		String content = "";
		String string = "";
		BufferedReader br = null;
		try {
			read = new FileReader(file);
			br = new BufferedReader(read);
			while ((content = br.readLine()) != null) {
				string += content.trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				read.close();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("string=" + string);
		return string.toString();
	}
	

	public static String getAKeyDeviceInfo() {
		
		String cmd = "cat /proc/bus/usb/devices";
        Log.d(TAG, cmd);

        Runtime run = Runtime.getRuntime();
        try {
            Process p = run.exec(cmd);
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 锟斤拷锟斤拷锟斤拷锟街达拷泻锟斤拷诳锟斤拷锟教�锟斤拷锟斤拷锟斤拷锟较�
                Log.d(TAG, lineStr);
//                if (lineStr.contains("usb") || lineStr.contains("udisk")) {
//                    String[] strArray = lineStr.split(" ");
//                    if (strArray != null && strArray.length >= 3) {
//                        String result = strArray[1].trim();
//                        return result;
//                    }
//                }
                // 锟斤拷锟斤拷锟斤拷锟斤拷欠锟街达拷锟绞э拷堋锟�
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷1锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
                    Log.e(TAG, "check mount info failed");
                    return null;
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
        
        return null;
	}
	
	public static String getAKeyPath() {
		
		String cmd = "cat /proc/mounts";
        Log.d(TAG, cmd);

        Runtime run = Runtime.getRuntime();
        try {
            Process p = run.exec(cmd);
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 锟斤拷锟斤拷锟斤拷锟街达拷泻锟斤拷诳锟斤拷锟教�锟斤拷锟斤拷锟斤拷锟较�
                Log.d(TAG, lineStr);
                if (lineStr.toLowerCase().contains("usb") || lineStr.toLowerCase().contains("udisk")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 3) {
                        String result = strArray[1].trim();
                        return result;
                    }
                }
                // 锟斤拷锟斤拷锟斤拷锟斤拷欠锟街达拷锟绞э拷堋锟�
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷1锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
                    Log.e(TAG, "check mount info failed");
                    return null;
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
        
        return null;
	}
	
	public static String getSDCardRootDirectory() {
		
		/* 锟斤拷锟斤拷锟斤拷锟� SDCard 锟斤拷锟矫ｏ拷直锟接凤拷锟斤拷锟斤拷路锟斤拷 */
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		
		/* 锟斤拷锟斤拷通锟斤拷 mount 锟斤拷录锟斤拷锟斤拷锟斤拷锟斤拷 SDCard */
		String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 锟斤拷锟斤拷锟诫当前 Java 应锟矫筹拷锟斤拷锟斤拷氐锟斤拷锟斤拷锟绞憋拷锟斤拷锟�
        try {
            Process p = run.exec(cmd);// 锟斤拷锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷执锟斤拷锟斤拷锟斤拷
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 锟斤拷锟斤拷锟斤拷锟街达拷泻锟斤拷诳锟斤拷锟教�锟斤拷锟斤拷锟斤拷锟较�
                Log.d(TAG, lineStr);
                if (lineStr.contains("sdcard")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 3) {
                        String result = strArray[1].trim();
                        return result;
                    }
                }
                // 锟斤拷锟斤拷锟斤拷锟斤拷欠锟街达拷锟绞э拷堋锟�
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0锟斤拷示锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷1锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
                    Log.e(TAG, "check mount info failed");
                    return null;
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
        
        return null;
	}
	
	public static String getExternalSDCardRootDirectory() {
		
		
		String cmdDF = "df";
		String cmdMMCLBK = "";
		String cmdMOUNT = "cat /proc/mounts";
		
        Runtime run = Runtime.getRuntime();
        List<String> paths = new ArrayList<String>();
        try {
            Process p = run.exec(cmdMOUNT);
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                Log.d(TAG, lineStr);
                if (lineStr.toLowerCase().contains("sdcard") || lineStr.toLowerCase().contains("ext") ) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 3 &&
                    		(!strArray[1].contains("/system") && 
                    		!strArray[1].contains("/data") &&
                    		!strArray[1].contains("/cache") &&
                    		!strArray[1].contains("/persist") 
                    		)) {
                    
                        String result = strArray[1].trim();
                        
                        if((result.contains("ext") || result.contains("1")) && result.contains("storage")) {
                        	paths.add(result);
                        }
                        //return result;
                    }
                }
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    Log.e(TAG, "check mount info failed");
                    return null;
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
        
        if (paths.size() > 0) {
        	return paths.get(0);
        }
        else {
        	return null;
        }
	}
	
	
	public static boolean checkPathValid(String path) {
		StatFs s = null;
		try {
			s = new StatFs(path);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		if (s.getBlockCount() * s.getBlockSize() != 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean checkFileValid(String path, String size, String sha1) {
		File downloadFile = new File(path);
		
		
		if (downloadFile.length() == Long.parseLong(size)) {
			StringBuffer hexString = new StringBuffer();
			
			try {
	            MessageDigest md = MessageDigest.getInstance("SHA-1");
	            FileInputStream fis = new FileInputStream(path);
	            byte[] dataBytes = new byte[4096];
	            int nread = 0;
	            while ((nread = fis.read(dataBytes)) != -1) {
	                md.update(dataBytes, 0, nread);
	            }
	            byte[] mdbytes = md.digest();
	            
	            for (int i = 0; i < mdbytes.length; i++) {
	                hexString.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	            }
	            //compare with sha hash code
	            Log.d(TAG, "rule SHA-1: " + hexString.toString()  + ", download SHA1 = " + sha1);
	            
	        } catch (NoSuchAlgorithmException e) {
	            Log.e(TAG, "There is no SHA-1 algorithm");
	            e.printStackTrace();
	        } catch (FileNotFoundException e) {
	            Log.e(TAG, "Update package does not exists");
	            e.printStackTrace();
	        } catch (IOException e) {
	            Log.e(TAG, "Read file error. Maybe file is corrupted");
	            e.printStackTrace();
	        }
			
			if(sha1 != null && sha1.equalsIgnoreCase(hexString.toString())){
                return true;
            } else {
                return false;
            }
		}
		else {
            Log.e(TAG, "rule size: " + size  + ", download size = " + downloadFile.length());

			return false;
		}
	}
	
	public static String getGlobalLogPath() {
		if (Constant.SDCARD_ROOT_PATH == null) {
			return null;
		}
		return Constant.SDCARD_ROOT_PATH + Constant.LOG_PATH + File.separator;
	}

	public static String getGlobalCachePath() {
		if (Constant.SDCARD_ROOT_PATH == null) {
			return null;
		}

		return Constant.SDCARD_ROOT_PATH + Constant.CACHE_PATH + File.separator;
	}
	

	


	

	
	/**
	 * get a file the each row text
	 * @param inputStream
	 * @return a  list the element class is String
	 */
	public static List<String> getFileEachRow(InputStream  inputStream) {
		List<String> list=new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String tempStr=null;
		if(null==inputStream){
			Log.e(TAG, "getFileEachRow  the param inputStream is null");
			return list;
		}
		try {
			while((tempStr=reader.readLine())!=null){
				list.add(tempStr);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "getFileEachRow() IOException!");
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, "getFileEachRow() IOException!");
			}
		}
		return list;
	}
	public static boolean checkSdcardCapability(){
		String sdCardDirString = getSDCardRootDirectory();
		if(null ==sdCardDirString){
			return false;
		}
		File sdFile = new File(sdCardDirString);
		if(null == sdFile){
			return false;
		}
		if(sdFile.getUsableSpace() < Constant.LOW_CAPACITY_THRESHOLD){
			Log.d(TAG, "check sdcard capacity,availabe space:"+sdFile.getUsableSpace()
					+",Constant.LOW_CAPACITY_THRESHOLD:"+Constant.LOW_CAPACITY_THRESHOLD);
			return false;
		}
		Log.d(TAG, "enought sdcard capability.");
		return true;
	}
	public static String longSizeToStr(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return String.format("%d B", size);
	}
	public static int getDirs(String path) {
		File f = new File(path);
		if (f.exists()) {
			File files[] = f.listFiles();
			int hideFilecount = 0;
			for(File checkhiden : files ){
				if(checkhiden.isHidden())hideFilecount++;
			}
			int size = files.length;
			files = null;
			f = null;
			return size-hideFilecount;
		} else {
			f = null;
			return 0;
		}
	}


	public static final String[][] MIME_MapTable =  
		{  
		// {后缀名， MIME类型}
		  { ".3gp", "video/3gpp" },  
		    { ".3gpp", "video/3gpp" },  
		    { ".aac", "audio/x-mpeg" },  
		    { ".amr", "audio/x-mpeg" },  
		    { ".apk", "application/vnd.android.package-archive" },  
		    { ".avi", "video/x-msvideo" },  
		    { ".aab", "application/x-authoware-bin" },  
		    { ".aam", "application/x-authoware-map" },  
		    { ".aas", "application/x-authoware-seg" },  
		    { ".ai", "application/postscript" },  
		    { ".aif", "audio/x-aiff" },  
		    { ".aifc", "audio/x-aiff" },  
		    { ".aiff", "audio/x-aiff" },  
		    { ".als", "audio/X-Alpha5" },  
		    { ".amc", "application/x-mpeg" },  
		    { ".ani", "application/octet-stream" },  
		    { ".asc", "text/plain" },  
		    { ".asd", "application/astound" },  
		    { ".asf", "video/x-ms-asf" },  
		    { ".asn", "application/astound" },  
		    { ".asp", "application/x-asap" },  
		    { ".asx", " video/x-ms-asf" },  
		    { ".au", "audio/basic" },  
		    { ".avb", "application/octet-stream" },  
		    { ".awb", "audio/amr-wb" },  
		    { ".bcpio", "application/x-bcpio" },  
		    { ".bld", "application/bld" },  
		    { ".bld2", "application/bld2" },  
		    { ".bpk", "application/octet-stream" },  
		    { ".bz2", "application/x-bzip2" },  
		    { ".bin", "application/octet-stream" },  
		    { ".bmp", "image/bmp" },  
		    { ".c", "text/plain" },  
		    { ".class", "application/octet-stream" },  
		    { ".conf", "text/plain" },  
		    { ".cpp", "text/plain" },  
		    { ".cal", "image/x-cals" },  
		    { ".ccn", "application/x-cnc" },  
		    { ".cco", "application/x-cocoa" },  
		    { ".cdf", "application/x-netcdf" },  
		    { ".cgi", "magnus-internal/cgi" },  
		    { ".chat", "application/x-chat" },  
		    { ".clp", "application/x-msclip" },  
		    { ".cmx", "application/x-cmx" },  
		    { ".co", "application/x-cult3d-object" },  
		    { ".cod", "image/cis-cod" },  
		    { ".cpio", "application/x-cpio" },  
		    { ".cpt", "application/mac-compactpro" },  
		    { ".crd", "application/x-mscardfile" },  
		    { ".csh", "application/x-csh" },  
		    { ".csm", "chemical/x-csml" },  
		    { ".csml", "chemical/x-csml" },  
		    { ".css", "text/css" },  
		    { ".cur", "application/octet-stream" },  
		    { ".doc", "application/msword" },  
		    { ".dcm", "x-lml/x-evm" },  
		    { ".dcr", "application/x-director" },  
		    { ".dcx", "image/x-dcx" },  
		    { ".dhtml", "text/html" },  
		    { ".dir", "application/x-director" },  
		    { ".dll", "application/octet-stream" },  
		    { ".dmg", "application/octet-stream" },  
		    { ".dms", "application/octet-stream" },  
		    { ".dot", "application/x-dot" },  
		    { ".dvi", "application/x-dvi" },  
		    { ".dwf", "drawing/x-dwf" },  
		    { ".dwg", "application/x-autocad" },  
		    { ".dxf", "application/x-autocad" },  
		    { ".dxr", "application/x-director" },  
		    { ".ebk", "application/x-expandedbook" },  
		    { ".emb", "chemical/x-embl-dl-nucleotide" },  
		    { ".embl", "chemical/x-embl-dl-nucleotide" },  
		    { ".eps", "application/postscript" },  
		    { ".epub", "application/epub+zip" },  
		    { ".eri", "image/x-eri" },  
		    { ".es", "audio/echospeech" },  
		    { ".esl", "audio/echospeech" },  
		    { ".etc", "application/x-earthtime" },  
		    { ".etx", "text/x-setext" },  
		    { ".evm", "x-lml/x-evm" },  
		    { ".evy", "application/x-envoy" },  
		    { ".exe", "application/octet-stream" },  
		    { ".fh4", "image/x-freehand" },  
		    { ".fh5", "image/x-freehand" },  
		    { ".fhc", "image/x-freehand" },  
		    { ".fif", "image/fif" },  
		    { ".fm", "application/x-maker" },  
		    { ".fpx", "image/x-fpx" },  
		    { ".fvi", "video/isivideo" },  
		    { ".flv", "video/x-msvideo" },  
		    { ".gau", "chemical/x-gaussian-input" },  
		    { ".gca", "application/x-gca-compressed" },  
		    { ".gdb", "x-lml/x-gdb" },  
		    { ".gif", "image/gif" },  
		    { ".gps", "application/x-gps" },  
		    { ".gtar", "application/x-gtar" },  
		    { ".gz", "application/x-gzip" },  
		    { ".gif", "image/gif" },  
		    { ".gtar", "application/x-gtar" },  
		    { ".gz", "application/x-gzip" },  
		    { ".h", "text/plain" },  
		    { ".hdf", "application/x-hdf" },  
		    { ".hdm", "text/x-hdml" },  
		    { ".hdml", "text/x-hdml" },  
		    { ".htm", "text/html" },  
		    { ".html", "text/html" },  
		    { ".hlp", "application/winhlp" },  
		    { ".hqx", "application/mac-binhex40" },  
		    { ".hts", "text/html" },  
		    { ".ice", "x-conference/x-cooltalk" },  
		    { ".ico", "application/octet-stream" },  
		    { ".ief", "image/ief" },  
		    { ".ifm", "image/gif" },  
		    { ".ifs", "image/ifs" },  
		    { ".imy", "audio/melody" },  
		    { ".ins", "application/x-NET-Install" },  
		    { ".ips", "application/x-ipscript" },  
		    { ".ipx", "application/x-ipix" },  
		    { ".it", "audio/x-mod" },  
		    { ".itz", "audio/x-mod" },  
		    { ".ivr", "i-world/i-vrml" },  
		    { ".j2k", "image/j2k" },  
		    { ".jad", "text/vnd.sun.j2me.app-descriptor" },  
		    { ".jam", "application/x-jam" },  
		    { ".jnlp", "application/x-java-jnlp-file" },  
		    { ".jpe", "image/jpeg" },  
		    { ".jpz", "image/jpeg" },  
		    { ".jwc", "application/jwc" },  
		    { ".jar", "application/java-archive" },  
		    { ".java", "text/plain" },  
		    { ".jpeg", "image/jpeg" },  
		    { ".jpg", "image/jpeg" },  
		    { ".js", "application/x-javascript" },  
		    { ".kjx", "application/x-kjx" },  
		    { ".lak", "x-lml/x-lak" },  
		    { ".latex", "application/x-latex" },  
		    { ".lcc", "application/fastman" },  
		    { ".lcl", "application/x-digitalloca" },  
		    { ".lcr", "application/x-digitalloca" },  
		    { ".lgh", "application/lgh" },  
		    { ".lha", "application/octet-stream" },  
		    { ".lml", "x-lml/x-lml" },  
		    { ".lmlpack", "x-lml/x-lmlpack" },  
		    { ".log", "text/plain" },  
		    { ".lsf", "video/x-ms-asf" },  
		    { ".lsx", "video/x-ms-asf" },  
		    { ".lzh", "application/x-lzh " },  
		    { ".m13", "application/x-msmediaview" },  
		    { ".m14", "application/x-msmediaview" },  
		    { ".m15", "audio/x-mod" },  
		    { ".m3u", "audio/x-mpegurl" },  
		    { ".m3url", "audio/x-mpegurl" },  
		    { ".ma1", "audio/ma1" },  
		    { ".ma2", "audio/ma2" },  
		    { ".ma3", "audio/ma3" },  
		    { ".ma5", "audio/ma5" },  
		    { ".man", "application/x-troff-man" },  
		    { ".map", "magnus-internal/imagemap" },  
		    { ".mbd", "application/mbedlet" },  
		    { ".mct", "application/x-mascot" },  
		    { ".mdb", "application/x-msaccess" },  
		    { ".mdz", "audio/x-mod" },  
		    { ".me", "application/x-troff-me" },  
		    { ".mel", "text/x-vmel" },  
		    { ".mi", "application/x-mif" },  
		    { ".mid", "audio/midi" },  
		    { ".midi", "audio/midi" },  
		    { ".m4a", "audio/mp4a-latm" },  
		    { ".m4b", "audio/mp4a-latm" },  
		    { ".m4p", "audio/mp4a-latm" },  
		    { ".m4u", "video/vnd.mpegurl" },  
		    { ".m4v", "video/x-m4v" },  
		    { ".mov", "video/quicktime" },  
		    { ".mp2", "audio/x-mpeg" },  
		    { ".mp3", "audio/x-mpeg" },  
		    { ".mp4", "video/mp4" },  
		    { ".mpc", "application/vnd.mpohun.certificate" },  
		    { ".mpe", "video/mpeg" },  
		    { ".mpeg", "video/mpeg" },  
		    { ".mpg", "video/mpeg" },  
		    { ".mpg4", "video/mp4" },  
		    { ".mpga", "audio/mpeg" },  
		    { ".msg", "application/vnd.ms-outlook" },  
		    { ".mif", "application/x-mif" },  
		    { ".mil", "image/x-cals" },  
		    { ".mio", "audio/x-mio" },  
		    { ".mmf", "application/x-skt-lbs" },  
		    { ".mng", "video/x-mng" },  
		    { ".mny", "application/x-msmoney" },  
		    { ".moc", "application/x-mocha" },  
		    { ".mocha", "application/x-mocha" },  
		    { ".mod", "audio/x-mod" },  
		    { ".mof", "application/x-yumekara" },  
		    { ".mol", "chemical/x-mdl-molfile" },  
		    { ".mop", "chemical/x-mopac-input" },  
		    { ".movie", "video/x-sgi-movie" },  
		    { ".mpn", "application/vnd.mophun.application" },  
		    { ".mpp", "application/vnd.ms-project" },  
		    { ".mps", "application/x-mapserver" },  
		    { ".mrl", "text/x-mrml" },  
		    { ".mrm", "application/x-mrm" },  
		    { ".ms", "application/x-troff-ms" },  
		    { ".mts", "application/metastream" },  
		    { ".mtx", "application/metastream" },  
		    { ".mtz", "application/metastream" },  
		    { ".mzv", "application/metastream" },  
		    { ".nar", "application/zip" },  
		    { ".nbmp", "image/nbmp" },  
		    { ".nc", "application/x-netcdf" },  
		    { ".ndb", "x-lml/x-ndb" },  
		    { ".ndwn", "application/ndwn" },  
		    { ".nif", "application/x-nif" },  
		    { ".nmz", "application/x-scream" },  
		    { ".nokia-op-logo", "image/vnd.nok-oplogo-color" },  
		    { ".npx", "application/x-netfpx" },  
		    { ".nsnd", "audio/nsnd" },  
		    { ".nva", "application/x-neva1" },  
		    { ".oda", "application/oda" },  
		    { ".oom", "application/x-AtlasMate-Plugin" },  
		    { ".ogg", "audio/ogg" },  
		    { ".pac", "audio/x-pac" },  
		    { ".pae", "audio/x-epac" },  
		    { ".pan", "application/x-pan" },  
		    { ".pbm", "image/x-portable-bitmap" },  
		    { ".pcx", "image/x-pcx" },  
		    { ".pda", "image/x-pda" },  
		    { ".pdb", "chemical/x-pdb" },  
		    { ".pdf", "application/pdf" },  
		    { ".pfr", "application/font-tdpfr" },  
		    { ".pgm", "image/x-portable-graymap" },  
		    { ".pict", "image/x-pict" },  
		    { ".pm", "application/x-perl" },  
		    { ".pmd", "application/x-pmd" },  
		    { ".png", "image/png" },  
		    { ".pnm", "image/x-portable-anymap" },  
		    { ".pnz", "image/png" },  
		    { ".pot", "application/vnd.ms-powerpoint" },
		    { ".potm", "application/vnd.ms-powerpoint" },
		    { ".potx", "application/vnd.ms-powerpoint" },
		    { ".ppam", "application/vnd.ms-powerpoint" },
		    { ".ppm", "image/x-portable-pixmap" },  
		    { ".pps", "application/vnd.ms-powerpoint" },
		    { ".ppsx", "application/vnd.ms-powerpoint" },
		    { ".ppt", "application/vnd.ms-powerpoint" },
		    { ".pptm", "application/vnd.ms-powerpoint" },
		    { ".pptx", "application/vnd.ms-powerpoint" },
		    { ".pqf", "application/x-cprplayer" },  
		    { ".pqi", "application/cprplayer" },  
		    { ".prc", "application/x-prc" },  
		    { ".proxy", "application/x-ns-proxy-autoconfig" },  
		    { ".prop", "text/plain" },  
		    { ".ps", "application/postscript" },  
		    { ".ptlk", "application/listenup" },  
		    { ".pub", "application/x-mspublisher" },  
		    { ".pvx", "video/x-pv-pvx" },  
		    { ".qcp", "audio/vnd.qcelp" },  
		    { ".qt", "video/quicktime" },  
		    { ".qti", "image/x-quicktime" },  
		    { ".qtif", "image/x-quicktime" },  
		    { ".r3t", "text/vnd.rn-realtext3d" },  
		    { ".ra", "audio/x-pn-realaudio" },  
		    { ".ram", "audio/x-pn-realaudio" },  
		    { ".ras", "image/x-cmu-raster" },  
		    { ".rdf", "application/rdf+xml" },  
		    { ".rf", "image/vnd.rn-realflash" },  
		    { ".rgb", "image/x-rgb" },  
		    { ".rlf", "application/x-richlink" },  
		    { ".rm", "audio/x-pn-realaudio" },  
		    { ".rmf", "audio/x-rmf" },  
		    { ".rmm", "audio/x-pn-realaudio" },  
		    { ".rnx", "application/vnd.rn-realplayer" },  
		    { ".roff", "application/x-troff" },  
		    { ".rp", "image/vnd.rn-realpix" },  
		    { ".rpm", "audio/x-pn-realaudio-plugin" },  
		    { ".rt", "text/vnd.rn-realtext" },  
		    { ".rte", "x-lml/x-gps" },  
		    { ".rtf", "application/rtf" },  
		    { ".rtg", "application/metastream" },  
		    { ".rtx", "text/richtext" },  
		    { ".rv", "video/vnd.rn-realvideo" },  
		    { ".rwc", "application/x-rogerwilco" },  
		    { ".rar", "application/x-rar-compressed" },  
		    { ".rc", "text/plain" },  
		    { ".rmvb", "audio/x-pn-realaudio" },  
		    { ".s3m", "audio/x-mod" },  
		    { ".s3z", "audio/x-mod" },  
		    { ".sca", "application/x-supercard" },  
		    { ".scd", "application/x-msschedule" },  
		    { ".sdf", "application/e-score" },  
		    { ".sea", "application/x-stuffit" },  
		    { ".sgm", "text/x-sgml" },  
		    { ".sgml", "text/x-sgml" },  
		    { ".shar", "application/x-shar" },  
		    { ".shtml", "magnus-internal/parsed-html" },  
		    { ".shw", "application/presentations" },  
		    { ".si6", "image/si6" },  
		    { ".si7", "image/vnd.stiwap.sis" },  
		    { ".si9", "image/vnd.lgtwap.sis" },  
		    { ".sis", "application/vnd.symbian.install" },  
		    { ".sit", "application/x-stuffit" },  
		    { ".skd", "application/x-Koan" },  
		    { ".skm", "application/x-Koan" },  
		    { ".skp", "application/x-Koan" },  
		    { ".skt", "application/x-Koan" },  
		    { ".slc", "application/x-salsa" },  
		    { ".smd", "audio/x-smd" },  
		    { ".smi", "application/smil" },  
		    { ".smil", "application/smil" },  
		    { ".smp", "application/studiom" },  
		    { ".smz", "audio/x-smd" },  
		    { ".sh", "application/x-sh" },  
		    { ".snd", "audio/basic" },  
		    { ".spc", "text/x-speech" },  
		    { ".spl", "application/futuresplash" },  
		    { ".spr", "application/x-sprite" },  
		    { ".sprite", "application/x-sprite" },  
		    { ".sdp", "application/sdp" },  
		    { ".spt", "application/x-spt" },  
		    { ".src", "application/x-wais-source" },  
		    { ".stk", "application/hyperstudio" },  
		    { ".stm", "audio/x-mod" },  
		    { ".sv4cpio", "application/x-sv4cpio" },  
		    { ".sv4crc", "application/x-sv4crc" },  
		    { ".svf", "image/vnd" },  
		    { ".svg", "image/svg-xml" },  
		    { ".svh", "image/svh" },  
		    { ".svr", "x-world/x-svr" },  
		    { ".swf", "application/x-shockwave-flash" },  
		    { ".swfl", "application/x-shockwave-flash" },  
		    { ".t", "application/x-troff" },  
		    { ".tad", "application/octet-stream" },  
		    { ".talk", "text/x-speech" },  
		    { ".tar", "application/x-tar" },  
		    { ".taz", "application/x-tar" },  
		    { ".tbp", "application/x-timbuktu" },  
		    { ".tbt", "application/x-timbuktu" },  
		    { ".tcl", "application/x-tcl" },  
		    { ".tex", "application/x-tex" },  
		    { ".texi", "application/x-texinfo" },  
		    { ".texinfo", "application/x-texinfo" },  
		    { ".tgz", "application/x-tar" },  
		    { ".thm", "application/vnd.eri.thm" },  
		    { ".tif", "image/tiff" },  
		    { ".tiff", "image/tiff" },  
		    { ".tki", "application/x-tkined" },  
		    { ".tkined", "application/x-tkined" },  
		    { ".toc", "application/toc" },  
		    { ".toy", "image/toy" },  
		    { ".tr", "application/x-troff" },  
		    { ".trk", "x-lml/x-gps" },  
		    { ".trm", "application/x-msterminal" },  
		    { ".tsi", "audio/tsplayer" },  
		    { ".tsp", "application/dsptype" },  
		    { ".tsv", "text/tab-separated-values" },  
		    { ".ttf", "application/octet-stream" },  
		    { ".ttz", "application/t-time" },  
		    { ".txt", "text/plain" },  
		    { ".ult", "audio/x-mod" },  
		    { ".ustar", "application/x-ustar" },  
		    { ".uu", "application/x-uuencode" },  
		    { ".uue", "application/x-uuencode" },  
		    { ".vcd", "application/x-cdlink" },  
		    { ".vcf", "text/x-vcard" },  
		    { ".vdo", "video/vdo" },  
		    { ".vib", "audio/vib" },  
		    { ".viv", "video/vivo" },  
		    { ".vivo", "video/vivo" },  
		    { ".vmd", "application/vocaltec-media-desc" },  
		    { ".vmf", "application/vocaltec-media-file" },  
		    { ".vmi", "application/x-dreamcast-vms-info" },  
		    { ".vms", "application/x-dreamcast-vms" },  
		    { ".vox", "audio/voxware" },  
		    { ".vqe", "audio/x-twinvq-plugin" },  
		    { ".vqf", "audio/x-twinvq" },  
		    { ".vql", "audio/x-twinvq" },  
		    { ".vre", "x-world/x-vream" },  
		    { ".vrml", "x-world/x-vrml" },  
		    { ".vrt", "x-world/x-vrt" },  
		    { ".vrw", "x-world/x-vream" },  
		    { ".vts", "workbook/formulaone" },  
		    { ".wax", "audio/x-ms-wax" },  
		    { ".wbmp", "image/vnd.wap.wbmp" },  
		    { ".web", "application/vnd.xara" },  
		    { ".wav", "audio/x-wav" },  
		    { ".wma", "audio/x-ms-wma" },  
		    { ".wmv", "audio/x-ms-wmv" },  
		    { ".wi", "image/wavelet" },  
		    { ".wis", "application/x-InstallShield" },  
		    { ".wm", "video/x-ms-wm" },  
		    { ".wmd", "application/x-ms-wmd" },  
		    { ".wmf", "application/x-msmetafile" },  
		    { ".wml", "text/vnd.wap.wml" },  
		    { ".wmlc", "application/vnd.wap.wmlc" },  
		    { ".wmls", "text/vnd.wap.wmlscript" },  
		    { ".wmlsc", "application/vnd.wap.wmlscriptc" },  
		    { ".wmlscript", "text/vnd.wap.wmlscript" },  
		    { ".wmv", "video/x-ms-wmv" },  
		    { ".wmx", "video/x-ms-wmx" },  
		    { ".wmz", "application/x-ms-wmz" },  
		    { ".wpng", "image/x-up-wpng" },  
		    { ".wps", "application/vnd.ms-works" },  
		    { ".wpt", "x-lml/x-gps" },  
		    { ".wri", "application/x-mswrite" },  
		    { ".wrl", "x-world/x-vrml" },  
		    { ".wrz", "x-world/x-vrml" },  
		    { ".ws", "text/vnd.wap.wmlscript" },  
		    { ".wsc", "application/vnd.wap.wmlscriptc" },  
		    { ".wv", "video/wavelet" },  
		    { ".wvx", "video/x-ms-wvx" },  
		    { ".wxl", "application/x-wxl" },  
		    { ".x-gzip", "application/x-gzip" },  
		    { ".xar", "application/vnd.xara" },  
		    { ".xbm", "image/x-xbitmap" },  
		    { ".xdm", "application/x-xdma" },  
		    { ".xdma", "application/x-xdma" },  
		    { ".xdw", "application/vnd.fujixerox.docuworks" },  
		    { ".xht", "application/xhtml+xml" },  
		    { ".xhtm", "application/xhtml+xml" },  
		    { ".xhtml", "application/xhtml+xml" },  
		    { ".xla", "application/vnd.ms-excel" },
		    { ".xlam", "application/vnd.ms-excel" },
		    { ".xlc", "application/vnd.ms-excel" },  
		    { ".xll", "application/x-excel" },  
		    { ".xlm", "application/vnd.ms-excel" },  
		    { ".xls", "application/vnd.ms-excel" }, 
		    { ".xlsm", "application/vnd.ms-excel" },
		    { ".xlsx", "application/vnd.ms-excel" },
		    { ".xlsb", "application/vnd.ms-excel" },
		    { ".xlt", "application/vnd.ms-excel" },
		    { ".xltx", "application/vnd.ms-excel" },
		    { ".xlw", "application/vnd.ms-excel" }, 
		    { ".xm", "audio/x-mod" },  
		    { ".xml", "text/xml" },  
		    { ".xmz", "audio/x-mod" },  
		    { ".xpi", "application/x-xpinstall" },  
		    { ".xpm", "image/x-xpixmap" },  
		    { ".xsit", "text/xml" },  
		    { ".xsl", "text/xml" },  
		    { ".xul", "text/xul" },  
		    { ".xwd", "image/x-xwindowdump" },  
		    { ".xyz", "chemical/x-pdb" },  
		    { ".yz1", "application/x-yz1" },  
		    { ".z", "application/x-compress" },  
		    { ".zac", "application/x-zaurus-zac" },  
		    { ".zip", "application/zip" },  
		    { "", "*/*" }   
		};  
	public static String getMIMEType(String filePath)  
	{  
	    String type = "*/*";  
	    String fName = filePath;  
	    int dotIndex = fName.lastIndexOf(".");  
	    if (dotIndex < 0)  
	    {  
	    	return type;  
	    }  
	    String end = fName.substring(dotIndex, fName.length()).toLowerCase();  
	    if (end == "")  
	    {  
	    	return type;  
	    }  
	    for (int i = 0; i < MIME_MapTable.length; i++)  
	    {  
	    	if (end.equals(MIME_MapTable[i][0]))  
	        {  
	            type = MIME_MapTable[i][1];  
	        }  
	    }  
	    return type;  
	}
	public static String getAppRootPath(){
		return Constant.APP_ROOT_PATH;
	}
}
