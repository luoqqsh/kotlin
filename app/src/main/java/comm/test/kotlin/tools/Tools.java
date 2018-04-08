package comm.test.kotlin.tools;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    public static boolean getConnectNetState(ConnectivityManager connManager) {
        try {
            NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
            if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
                return false;
            } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                State state = connManager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI).getState();
                if (State.CONNECTED != state) {
                    return false;
                }
            } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                State state = connManager.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE).getState();
                if (State.CONNECTED != state) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPenGPS(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
        /*// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）  
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  
        if (gps || network) {  
            return true;  
        } */
        if (gps) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    public static String getMD5Str(String value) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(value.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] bytearr = md5.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytearr.length; i++) {
            if (Integer.toHexString(0xFF & bytearr[i]).length() == 1) {
                sb.append("0").append(Integer.toHexString(0xFF & bytearr[i]));
            } else
                sb.append(Integer.toHexString(0xFF & bytearr[i]));
        }
        // Log.i("noxus", sb.toString());
        return sb.toString();
    }

    // public static void WriteDBtoSD(){
    // String DATABASE_PATH =
    // android.os.Environment.getExternalStorageDirectory().getAbsolutePath() +
    // "/testdb"; //将要存放于的文件夹
    // String DATABASE_FILENAME = "vehicle_dict.db"; //文件名
    // String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
    // File dir = new File(DATABASE_PATH);
    // // 如果/sdcard/testdb目录中存在，创建这个目录
    // if (!dir.exists())
    // dir.mkdir();
    // // 如果在/sdcard/testdb目录中不存在
    // // test.db文件，则从asset\db目录中复制这个文件到
    // // SD卡的目录（/sdcard/testdb）
    // if (!(new File(databaseFilename)).exists()) {
    // // 获得封装testDatabase.db文件的InputStream对象
    // AssetManager asset = getAssets();
    // InputStream is=asset.open("db/testDatabase.db");
    // FileOutputStream fos = new FileOutputStream(databaseFilename);
    // byte[] buffer = newbyte[8192];
    // int count = 0;
    // // 开始复制testDatabase.db文件
    // while ((count = is.read(buffer)) > 0) {
    // fos.write(buffer, 0, count);
    // }
    // fos.close();
    // is.close();
    // asset.close();
    // }
    // SQLiteDatabase mSQLiteDatabase=openOrCreateDatabase(databaseFilename,
    // Activity.MODE_PRIVATE, null);//有则打开，没有创建
    // Cursor cur=mSQLiteDatabase.rawQuery("select * from table1", null);
    // if(cur!=null){
    // if(cur.moveToFirst()){
    // do{
    // int idColumnIndex=cur.getColumnIndex("id");
    // int numColumnIndex=cur.getColumnIndex("num");
    // int dataColumnIndex=cur.getColumnIndex("data");
    // int id=cur.getInt(idColumnIndex);
    // int num=cur.getInt(numColumnIndex);
    // String data=cur.getString(dataColumnIndex);
    // System.out.println("id:"+id+";num:"+num+";data:"+data);
    // }while(cur.moveToNext());
    // cur.close();
    // }
    // }
    // mSQLiteDatabase.close();//关闭数据库连接
    // }catch(Exception e){
    // e.printStackTrace();
    // }
    // //deleteDatabase("testDatabase.db");//删除数据库
    // }

    public static String getFileNameFromUrl(String url) {
        String[] _strTemp = url.split("/");
        int _count = _strTemp.length;
        return _strTemp[_count - 1];
    }

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    static final double PII = 0.017453292519943295769236907684883;

    public static float calcDistFromAToB(float lonA, float latA, float lonB,
                                         float latB) {
        double r = Math.cos(latB * PII);
        double dist = Math
                .sqrt(((lonB - lonA) * (lonB - lonA) * (r * r)) + (latB - latA)
                        * (latB - latA));
        return (float) (dist / 32.287 * 3600000);
    }

    public static double getLon(double lonA, double latA, double dist) {
        double r = Math.cos(latA * PII);
        double result = dist * 32.287 / 3600000;
        return (double) ((result / r) + lonA);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);

    }

    public static void setListViewHeightBasedOnChildren(GridView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount() / 4; i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight;

        listView.setLayoutParams(params);

    }

    /**
     * 将Bitmap转换成经过Base64加密的字符串
     *
     * @param bitmap
     * @return
     */
    @SuppressLint("NewApi")
    public static String bitmapToString(Bitmap bitmap) {
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * 将经过Base64加密的字符串转换成Bitmap类型
     *
     * @param string
     * @return
     */
    @SuppressLint("NewApi")
    public Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    @SuppressLint("NewApi")
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    @SuppressLint("NewApi")
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static boolean passwordMatch(String password) {
        String letterRegex = "[a-zA-Z]";
        String numRegex = "[0-9]";
        Pattern letterPattern = Pattern.compile(letterRegex);
        Pattern numPattern = Pattern.compile(numRegex);
        Matcher letterMatcher = letterPattern.matcher(password);
        Matcher numMatcher = numPattern.matcher(password);
        if (password.length() < 6 || password.length() > 20) {
            return false;
        } else if (!letterMatcher.find()) {
            return false;
        } else if (!numMatcher.find()) {
            return false;
        } else {
            return true;
        }
    }

    public static String getConstellation(String time) {
        if (time.equals("")) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getConstellation(c);
    }

    /**
     * 查询星座
     *
     * @param calendar 具体日期
     * @return 星座
     */
    public static String getConstellation(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        switch (month) {
            case 1:
                if (day >= 20)
                    return "水瓶座";
                return "摩羯座";
            case 2:
                if (day >= 19)
                    return "双鱼座";
                return "水瓶座";
            case 3:
                if (day >= 21)
                    return "白羊座";
                return "双鱼座";
            case 4:
                if (day >= 20)
                    return "金牛座";
                return "白羊座";
            case 5:
                if (day >= 20)
                    return "双子座";
                return "金牛座";
            case 6:
                if (day >= 22)
                    return "巨蟹座";
                return "双子座";
            case 7:
                if (day >= 23)
                    return "狮子座";
                return "巨蟹座";
            case 8:
                if (day >= 23)
                    return "处女座";
                return "狮子座";
            case 9:
                if (day >= 23)
                    return "天秤座";
                return "处女座";
            case 10:
                if (day >= 24)
                    return "天蝎座";
                return "天秤座";
            case 11:
                if (day >= 23)
                    return "射手座";
                return "天蝎座";
            case 12:
                if (day >= 22)
                    return "摩羯座";
                return "射手座";
        }
        return "日期出错";
    }

    /**
     * @param path
     * @return
     * @throws Exception String
     * @说明:将文件转成base64 字符串
     * @作者:yxy @日期：2015-1-13
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        return encodeBase64File(file);
    }

    /**
     * @param
     * @return
     * @throws Exception String
     * @说明:将文件转成base64 字符串
     * @作者:yxy @日期：2015-1-13
     */
    public static String encodeBase64File(File file) throws Exception {
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 获取版本名称
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return " 未知版本号";
        }
    }

    public static final int QR_WIDTH = 200, QR_HEIGHT = 200;

    /**
     * @param context
     * @return
     * @author 刘波
     * @TODO 获取屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    // 获取屏幕的密度
    public static float getScreenDensity(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.density;
    }

    public static String SceneList2String(List SceneList) throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    @SuppressWarnings("unchecked")
    public static List String2SceneList(String SceneListString)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List SceneList = (List) objectInputStream.readObject();
        objectInputStream.close();
        return SceneList;
    }

    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
