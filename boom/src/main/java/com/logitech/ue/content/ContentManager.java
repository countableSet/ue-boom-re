// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.content;

import java.net.URLConnection;
import com.logitech.ue.download.DownloadManager;
import com.logitech.ue.download.LoadDataTask;
import java.net.UnknownHostException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import com.logitech.ue.download.LoadDataTaskListener;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.util.Map;
import org.json.JSONObject;
import java.util.HashMap;
import java.net.HttpURLConnection;
import java.net.URL;
import com.logitech.ue.service.ServiceInfo;
import android.os.AsyncTask;
import com.logitech.ue.manifest.ManifestManager;
import java.io.StringWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;
import android.util.Log;
import com.logitech.ue.manifest.DeviceInfo;
import android.graphics.BitmapFactory;
import com.caverock.androidsvg.SVG;
import android.graphics.Bitmap;
import android.os.Bundle;
import java.util.concurrent.Executors;
import android.content.Context;
import java.util.concurrent.ExecutorService;
import android.util.LruCache;
import com.logitech.ue.service.IUEService;

public class ContentManager implements IUEService
{
    private static final String ASSETS_PREFIX = "assets_of_colour_";
    public static final String BASE_URL_KEY = "base_url";
    public static final int CACHE_SIZE = 128;
    private static final int RETRY_LIMIT = 10;
    private static final String TAG;
    private static ContentManager instance;
    public static String mBaseUrl;
    private static int mRetry;
    final LruCache<String, Object> contentCache;
    final ExecutorService executorService;
    private Context mContext;
    private String mServiceLocation;
    final LruCache<String, byte[]> memoryCache;
    
    static {
        TAG = ContentManager.class.getSimpleName();
        ContentManager.mBaseUrl = "";
        ContentManager.instance = null;
        ContentManager.mRetry = 0;
    }
    
    private ContentManager() {
        this.memoryCache = (LruCache<String, byte[]>)new LruCache(128);
        this.contentCache = (LruCache<String, Object>)new LruCache(128);
        this.executorService = Executors.newFixedThreadPool(5);
    }
    
    public static Bundle buildParamBundle(final String s, final String s2) {
        final Bundle bundle = new Bundle();
        bundle.putString("base_url", s2);
        return bundle;
    }
    
    private Bitmap checkBitmapInFileCache(final String s, final boolean b) {
        final Bitmap decodeBitmap = decodeBitmap(this.checkDataInFileCache(s, b));
        if (decodeBitmap != null && b) {
            this.contentCache.put((Object)s, (Object)decodeBitmap);
        }
        return decodeBitmap;
    }
    
    private SVG checkSVGInFileCache(final String s, final boolean b) {
        final SVG decodeSVG = decodeSVG(this.checkDataInFileCache(s, b));
        if (decodeSVG != null && b) {
            this.contentCache.put((Object)s, (Object)decodeSVG);
        }
        return decodeSVG;
    }
    
    public static Bitmap decodeBitmap(final byte[] array) {
        Bitmap decodeByteArray;
        if (array == null) {
            decodeByteArray = null;
        }
        else {
            decodeByteArray = BitmapFactory.decodeByteArray(array, 0, array.length);
        }
        return decodeByteArray;
    }
    
    public static SVG decodeSVG(final byte[] bytes) {
        final SVG svg = null;
        SVG fromString;
        if (bytes == null) {
            fromString = svg;
        }
        else {
            try {
                fromString = SVG.getFromString(new String(bytes));
            }
            catch (Exception ex) {
                ex.printStackTrace();
                fromString = svg;
            }
        }
        return fromString;
    }
    
    public static ContentManager getInstance() {
        Label_0030: {
            if (ContentManager.instance != null) {
                break Label_0030;
            }
            synchronized (ContentManager.class) {
                if (ContentManager.instance == null) {
                    ContentManager.instance = new ContentManager();
                }
                return ContentManager.instance;
            }
        }
    }
    
    private boolean isDifferentVersionAvailable(final DeviceInfo deviceInfo) {
        final boolean b = false;
        final String string = this.mContext.getSharedPreferences(ContentManager.TAG, 0).getString("assets_of_colour_" + deviceInfo.hexCode, (String)null);
        boolean b2 = b;
        if (deviceInfo.version != null) {
            b2 = b;
            if (!deviceInfo.version.equals(string)) {
                Log.d(ContentManager.TAG, "New assets version: " + deviceInfo.version + " found. Saved version: " + string);
                this.saveDeviceInfo(deviceInfo);
                b2 = true;
            }
        }
        return b2;
    }
    
    public static byte[] readAllBytesFromFile(final File file) throws IOException {
        final byte[] b = new byte[(int)file.length()];
        final DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        dataInputStream.readFully(b);
        dataInputStream.close();
        return b;
    }
    
    private void saveDeviceInfo(final DeviceInfo deviceInfo) {
        final String string = "assets_of_colour_" + deviceInfo.hexCode;
        this.mContext.getSharedPreferences(ContentManager.TAG, 0).edit().putString(string, deviceInfo.version).apply();
        Log.d(ContentManager.TAG, string + " version " + deviceInfo.version + " saved");
    }
    
    String buildBitmapURL(final String s, final String s2) {
        final StringWriter stringWriter = new StringWriter();
        stringWriter.append(ContentManager.mBaseUrl);
        if (ContentManager.mBaseUrl.charAt(ContentManager.mBaseUrl.length() - 1) != '/') {
            stringWriter.append('/');
        }
        switch (this.mContext.getResources().getDisplayMetrics().densityDpi) {
            case 120: {
                stringWriter.append("mdpi");
                break;
            }
            case 160: {
                stringWriter.append("mdpi");
                break;
            }
            case 240: {
                stringWriter.append("hdpi");
                break;
            }
            case 320: {
                stringWriter.append("xhdpi");
                break;
            }
            case 480: {
                stringWriter.append("xxhdpi");
                break;
            }
        }
        stringWriter.append(String.format("/%s_%s.png", s, s2));
        return stringWriter.toString();
    }
    
    String buildSVGURL(final String s, final String s2) {
        final StringWriter stringWriter = new StringWriter();
        stringWriter.append(ContentManager.mBaseUrl);
        if (ContentManager.mBaseUrl.charAt(ContentManager.mBaseUrl.length() - 1) != '/') {
            stringWriter.append('/');
        }
        stringWriter.append(String.format("%s_%s.svg", s, s2));
        return stringWriter.toString();
    }
    
    String buildURL(final ContentType contentType, final String s, final String s2) {
        String s3;
        if (contentType == ContentType.Bitmap) {
            s3 = this.buildBitmapURL(s, s2);
        }
        else if (contentType == ContentType.SVG) {
            s3 = this.buildSVGURL(s, s2);
        }
        else if (contentType == ContentType.Binary) {
            s3 = this.buildSVGURL(s, s2);
        }
        else {
            s3 = null;
        }
        return s3;
    }
    
    public Object checkContentInCache(final ContentType contentType, final String s, final String s2) {
        Object o;
        if ((o = this.checkContentInContentCache(contentType, s, s2)) == null && (o = this.checkContentInMemoryCache(contentType, s, s2)) == null) {
            o = this.checkContentInFileCache(contentType, this.buildURL(contentType, s, s2));
        }
        return o;
    }
    
    public Object checkContentInContentCache(final ContentType contentType, final String s, final String s2) {
        final Object value = this.contentCache.get((Object)this.buildURL(contentType, s, s2));
        if (value == null) {
            return null;
        }
        Object value2;
        if (contentType == ContentType.Bitmap && value instanceof Bitmap) {
            value2 = value;
        }
        else {
            if (contentType == ContentType.SVG) {
                value2 = value;
                if (value instanceof SVG) {
                    return value2;
                }
            }
            if (contentType != ContentType.Binary) {
                return null;
            }
            value2 = this.memoryCache.get((Object)this.buildURL(contentType, s, s2));
        }
        return value2;
        value2 = null;
        return value2;
    }
    
    public Object checkContentInFileCache(final ContentType contentType, final String s) {
        final byte[] checkDataInFileCache = this.checkDataInFileCache(s, true);
        if (checkDataInFileCache == null) {
            return null;
        }
        Object o;
        if (contentType == ContentType.Bitmap) {
            o = decodeBitmap(checkDataInFileCache);
            if (o != null) {
                this.memoryCache.put((Object)s, (Object)checkDataInFileCache);
                this.contentCache.put((Object)s, o);
            }
            else {
                o = null;
            }
        }
        else if (contentType == ContentType.SVG) {
            o = decodeSVG(checkDataInFileCache);
            if (o != null) {
                this.memoryCache.put((Object)s, (Object)checkDataInFileCache);
                this.contentCache.put((Object)s, o);
            }
            else {
                o = null;
            }
        }
        else {
            if (contentType != ContentType.Binary) {
                return null;
            }
            o = checkDataInFileCache;
        }
        return o;
        o = null;
        return o;
    }
    
    public Object checkContentInMemoryCache(final ContentType contentType, final String s) {
        final byte[] array = (byte[])this.memoryCache.get((Object)s);
        if (array == null) {
            return null;
        }
        Object o;
        if (contentType == ContentType.Bitmap) {
            o = decodeBitmap(array);
            if (o != null) {
                this.contentCache.put((Object)s, o);
            }
            else {
                o = null;
            }
        }
        else if (contentType == ContentType.SVG) {
            o = decodeSVG(array);
            if (o != null) {
                this.contentCache.put((Object)s, o);
            }
            else {
                o = null;
            }
        }
        else {
            if (contentType != ContentType.Binary) {
                return null;
            }
            o = this.memoryCache.get((Object)s);
        }
        return o;
        o = null;
        return o;
    }
    
    public Object checkContentInMemoryCache(final ContentType contentType, final String s, final String s2) {
        return this.checkContentInMemoryCache(contentType, this.buildURL(contentType, s, s2));
    }
    
    public byte[] checkDataInFileCache(final String s, final boolean b) {
        Log.d(ContentManager.TAG, "Check file for url(" + s + ")");
        final File file = FileCacheManager.getInstance(this.mContext).getFile(s);
        if (!file.exists()) {
            return null;
        }
        try {
            byte[] allBytesFromFile;
            final byte[] array = allBytesFromFile = readAllBytesFromFile(file);
            if (b) {
                this.memoryCache.put((Object)s, (Object)array);
                allBytesFromFile = array;
            }
            return allBytesFromFile;
        }
        catch (IOException ex) {
            Log.e(ContentManager.TAG, "Failed to read data from cache file. Delete " + s);
            file.delete();
        }
        return null;
    }
    
    public Bitmap getBitmap(final String s, final String s2, final ContentLoadListener<Bitmap> contentLoadListener) {
        Log.d(ContentManager.TAG, "Get bitmap image for color code " + s);
        Bitmap bitmap;
        if (ManifestManager.getInstance().getManifest().getDeviceInfo(s) == null) {
            Log.w(ContentManager.TAG, "Device color code " + s + " not found in manifest.");
            bitmap = null;
        }
        else {
            Log.d(ContentManager.TAG, "Device color code " + s + " found in manifest");
            this.buildBitmapURL(s, s2);
            final Object checkContentInCache = this.checkContentInCache(ContentType.Bitmap, s, s2);
            if (checkContentInCache != null) {
                bitmap = (Bitmap)checkContentInCache;
            }
            else {
                Log.d(ContentManager.TAG, "Device color code " + s + " not found in file cache");
                this.loadBitmap(s, s2, contentLoadListener);
                bitmap = null;
            }
        }
        return bitmap;
    }
    
    public void getDeviceTypeAsync(final int n, final GetDeviceTypeHandler getDeviceTypeHandler) {
        new CheckDeviceTypeTask(n, getDeviceTypeHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
    }
    
    public SVG getSVG(final String str, final String s, final ContentLoadListener<SVG> contentLoadListener) {
        Log.d(ContentManager.TAG, "Get SVG image for color code " + str);
        final DeviceInfo deviceInfo = ManifestManager.getInstance().getManifest().getDeviceInfo(str);
        SVG svg;
        if (deviceInfo == null) {
            Log.w(ContentManager.TAG, "Device color code " + str + " not found in manifest");
            svg = null;
        }
        else {
            Log.d(ContentManager.TAG, "Device color code " + str + " found in manifest");
            final boolean differentVersionAvailable = this.isDifferentVersionAvailable(deviceInfo);
            Object checkContentInCache = null;
            if (!differentVersionAvailable) {
                checkContentInCache = this.checkContentInCache(ContentType.SVG, str, s);
            }
            if (checkContentInCache != null && checkContentInCache instanceof SVG) {
                Log.d(ContentManager.TAG, "Device color code " + str + " found in file cache");
                svg = (SVG)checkContentInCache;
            }
            else {
                Log.d(ContentManager.TAG, "Loading assets of color code " + str + " from server");
                this.loadSVG(str, s, contentLoadListener);
                svg = null;
            }
        }
        return svg;
    }
    
    @Override
    public String getServiceName() {
        return "devicemap";
    }
    
    @Override
    public void initService(final Context mContext, final Bundle bundle) {
        this.mContext = mContext;
        if (bundle != null) {
            ContentManager.mBaseUrl = bundle.getString("base_url", "");
        }
    }
    
    void loadBitmap(final String str, final String str2, final ContentLoadListener<Bitmap> contentLoadListener) {
        Log.d(ContentManager.TAG, "Queue color code " + str + ", type " + str2 + " loading bitmap task");
        this.executorService.submit(new LoadTextureRunnable(new LoadDeviceInfoTask<Bitmap>(str, str2, contentLoadListener)));
    }
    
    void loadData(final String str, final String str2, final ContentLoadListener<byte[]> contentLoadListener) {
        Log.d(ContentManager.TAG, "Queue color code " + str + " type " + str2 + " loading data task");
        this.executorService.submit(new LoadDataRunnable(new LoadDeviceInfoTask<byte[]>(str, str2, contentLoadListener)));
    }
    
    void loadSVG(final String str, final String str2, final ContentLoadListener<SVG> contentLoadListener) {
        Log.d(ContentManager.TAG, "Queue color code " + str + ", type " + str2 + " loading SVG task");
        this.executorService.submit(new LoadSVGRunnable(new LoadDeviceInfoTask<SVG>(str, str2, contentLoadListener)));
    }
    
    void saveToFileCache(final byte[] array, final String str) {
        if (array != null) {
            Log.d(ContentManager.TAG, "Save file data for url(" + str + ") to file cache");
            try {
                FileCacheManager.getInstance(this.mContext).cacheDataToFile(str, array);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public void syncService(final ServiceInfo serviceInfo) {
        this.mServiceLocation = serviceInfo.location;
    }
    
    public class CheckDeviceTypeTask extends AsyncTask<Void, Void, Object>
    {
        private int mColorCode;
        private GetDeviceTypeHandler mGetDeviceTypeHandler;
        
        public CheckDeviceTypeTask(final int mColorCode, final GetDeviceTypeHandler mGetDeviceTypeHandler) {
            this.mColorCode = mColorCode;
            this.mGetDeviceTypeHandler = mGetDeviceTypeHandler;
        }
        
        protected Object doInBackground(Void... array) {
            Log.i(ContentManager.TAG, "Begin fetch notification. Server URL: " + ContentManager.this.mServiceLocation);
            Label_0288: {
                StringBuilder sb = null;
                Label_0240: {
                    try {
                        array = (Void[])(Object)new URL(ContentManager.this.mServiceLocation);
                        array = (Void[])(Object)((URL)(Object)array).openConnection();
                        ((URLConnection)(Object)array).setRequestProperty("Accept", "application/json");
                        ((URLConnection)(Object)array).setRequestProperty("Content-type", "application/json");
                        ((URLConnection)(Object)array).setDoInput(true);
                        ((URLConnection)(Object)array).setDoOutput(true);
                        final HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("hexCode", Integer.toHexString(this.mColorCode));
                        final String string = new JSONObject((Map)hashMap).toString();
                        final DataOutputStream dataOutputStream = new DataOutputStream(((URLConnection)(Object)array).getOutputStream());
                        Log.i(ContentManager.TAG, "Sending params: " + string);
                        dataOutputStream.writeUTF(string);
                        ((URLConnection)(Object)array).connect();
                        final InputStream inputStream = ((URLConnection)(Object)array).getInputStream();
                        if (inputStream == null || ((HttpURLConnection)(Object)array).getResponseCode() != 200) {
                            break Label_0288;
                        }
                        array = (Void[])(Object)new BufferedReader(new InputStreamReader(inputStream));
                        sb = new StringBuilder();
                        while (true) {
                            final String line = ((BufferedReader)(Object)array).readLine();
                            if (line == null) {
                                break Label_0240;
                            }
                            sb.append(line);
                        }
                    }
                    catch (Exception ex) {}
                    return array;
                }
                Log.i(ContentManager.TAG, "Received fetch result. Result: " + sb.toString());
                array = (Void[])(Object)new JSONObject(sb.toString());
                return array;
            }
            Log.i(ContentManager.TAG, "Fetch invalid");
            array = null;
            return array;
        }
        
        protected void onPostExecute(final Object o) {
            if (this.mGetDeviceTypeHandler != null) {
                if (o instanceof JSONObject) {
                    final JSONObject jsonObject = (JSONObject)o;
                    this.mGetDeviceTypeHandler.onSuccess(jsonObject.optString("deviceInfo"), jsonObject.optString("type"));
                }
                else if (o == null) {
                    this.mGetDeviceTypeHandler.onSuccess(null, null);
                }
                else {
                    this.mGetDeviceTypeHandler.onFail((Exception)o);
                }
            }
            super.onPostExecute(o);
        }
    }
    
    public interface GetDeviceTypeHandler
    {
        void onFail(final Exception p0);
        
        void onSuccess(final String p0, final String p1);
    }
    
    class LoadDataRunnable implements Runnable, LoadDataTaskListener
    {
        final LoadDeviceInfoTask<byte[]> task;
        String url;
        
        public LoadDataRunnable(final LoadDeviceInfoTask<byte[]> task) {
            this.url = null;
            this.task = task;
            this.url = ContentManager.this.buildURL(ContentType.SVG, task.hexCode, task.imageType);
        }
        
        public void endLoadTask(final Object o) {
            final LoadDeviceInfoTask<byte[]> task = this.task;
            if (o instanceof byte[]) {
                Log.d(ContentManager.TAG, "Texture loaded for color code " + task.hexCode + " image type " + task.imageType);
                final byte[] array = (byte[])o;
                ContentManager.this.memoryCache.put((Object)this.url, (Object)array);
                ContentManager.this.saveToFileCache(array, this.url);
                if (this.task.listener != null) {
                    this.task.listener.onLoadSuccess(array, this.task.hexCode, this.task.imageType);
                }
            }
            else if (o instanceof Exception) {
                Log.e(ContentManager.TAG, "Texture loading failed for color code " + task.hexCode + " image type " + task.imageType);
                final Exception ex = (Exception)o;
                if (!(ex instanceof MalformedURLException) && !(ex instanceof FileNotFoundException) && !(ex instanceof UnknownHostException)) {
                    if (ContentManager.mRetry < 10) {
                        ContentManager.this.loadData(this.task.hexCode, this.task.imageType, this.task.listener);
                        Log.d(ContentManager.TAG, "Retry texture load for color code " + this.task.hexCode + " image type " + this.task.imageType);
                        ContentManager.mRetry = ContentManager.mRetry;
                    }
                    else if (ContentManager.mRetry == 10) {
                        ContentManager.mRetry = 0;
                    }
                }
            }
        }
        
        @Override
        public void onBeginTask(final LoadDataTask loadDataTask) {
        }
        
        @Override
        public void onEndTask(final LoadDataTask loadDataTask) {
            this.endLoadTask(loadDataTask.data);
        }
        
        @Override
        public void onError(final LoadDataTask loadDataTask, final Throwable t) {
            this.endLoadTask(t);
        }
        
        @Override
        public void run() {
            Log.d(ContentManager.TAG, "Begin loading task for color code " + this.task.hexCode);
            final Object checkContentInCache = ContentManager.this.checkContentInCache(ContentType.Binary, this.task.hexCode, this.task.imageType);
            if (checkContentInCache != null) {
                this.endLoadTask(checkContentInCache);
            }
            else {
                Log.d(ContentManager.TAG, "Queue texture loading task for color code " + this.task.hexCode + " image type " + this.task.imageType);
                DownloadManager.getInstance(ContentManager.this.mContext).loadFile(this.url, "no idea", this);
            }
        }
    }
    
    class LoadSVGRunnable implements Runnable
    {
        final LoadDeviceInfoTask<SVG> task;
        
        public LoadSVGRunnable(final LoadDeviceInfoTask<SVG> task) {
            this.task = task;
        }
        
        @Override
        public void run() {
            Log.d(ContentManager.TAG, "Begin loading task for color code " + this.task.hexCode);
            final String buildSVGURL = ContentManager.this.buildSVGURL(this.task.hexCode, this.task.imageType);
            final Object checkContentInCache = ContentManager.this.checkContentInCache(ContentType.SVG, this.task.hexCode, this.task.imageType);
            if (checkContentInCache != null) {
                final SVG svg = (SVG)checkContentInCache;
                if (this.task.listener != null) {
                    this.task.listener.onLoadSuccess(svg, this.task.hexCode, this.task.imageType);
                }
            }
            else {
                Log.d(ContentManager.TAG, "Queue texture loading task for color code " + this.task.hexCode + " image type " + this.task.imageType);
                DownloadManager.getInstance(ContentManager.this.mContext).loadFile(buildSVGURL, "no idea", new LoadDataTaskListener() {
                    @Override
                    public void onBeginTask(final LoadDataTask loadDataTask) {
                    }
                    
                    @Override
                    public void onEndTask(final LoadDataTask loadDataTask) {
                        final LoadDeviceInfoTask<SVG> task = LoadSVGRunnable.this.task;
                        Log.d(ContentManager.TAG, "Texture loaded for color code " + task.hexCode + " image type " + task.imageType);
                        final SVG decodeSVG = ContentManager.decodeSVG(loadDataTask.data);
                        ContentManager.this.contentCache.put((Object)loadDataTask.url, (Object)decodeSVG);
                        ContentManager.this.memoryCache.put((Object)loadDataTask.url, (Object)loadDataTask.data);
                        ContentManager.this.saveToFileCache(loadDataTask.data, buildSVGURL);
                        if (task.listener != null) {
                            task.listener.onLoadSuccess(decodeSVG, task.hexCode, task.imageType);
                        }
                    }
                    
                    @Override
                    public void onError(final LoadDataTask loadDataTask, final Throwable t) {
                        final LoadDeviceInfoTask<SVG> task = LoadSVGRunnable.this.task;
                        Log.e(ContentManager.TAG, "Texture loading failed for color code " + task.hexCode + " image type " + task.imageType);
                        if (!(t instanceof MalformedURLException) && !(t instanceof FileNotFoundException) && !(t instanceof UnknownHostException)) {
                            if (ContentManager.mRetry < 10) {
                                ContentManager.this.loadSVG(task.hexCode, task.imageType, task.listener);
                                Log.d(ContentManager.TAG, "Retry texture load for color code " + task.hexCode + " image type " + task.imageType);
                                ContentManager.mRetry = ContentManager.mRetry;
                            }
                            else if (ContentManager.mRetry == 10) {
                                ContentManager.mRetry = 0;
                            }
                        }
                    }
                });
            }
        }
    }
    
    class LoadTextureRunnable implements Runnable
    {
        final LoadDeviceInfoTask<Bitmap> task;
        
        public LoadTextureRunnable(final LoadDeviceInfoTask<Bitmap> task) {
            this.task = task;
        }
        
        @Override
        public void run() {
            Log.d(ContentManager.TAG, "Begin loading task for color code " + this.task.hexCode);
            final String buildBitmapURL = ContentManager.this.buildBitmapURL(this.task.hexCode, this.task.imageType);
            final Object checkContentInCache = ContentManager.this.checkContentInCache(ContentType.Bitmap, this.task.hexCode, this.task.imageType);
            if (checkContentInCache != null && checkContentInCache instanceof Bitmap) {
                if (this.task.listener != null) {
                    this.task.listener.onLoadSuccess((Bitmap)checkContentInCache, this.task.hexCode, this.task.imageType);
                }
            }
            else {
                Log.d(ContentManager.TAG, "Queue texture loading task for color code " + this.task.hexCode + " image type " + this.task.imageType);
                DownloadManager.getInstance(ContentManager.this.mContext).loadFile(buildBitmapURL, "no idea", new LoadDataTaskListener() {
                    @Override
                    public void onBeginTask(final LoadDataTask loadDataTask) {
                    }
                    
                    @Override
                    public void onEndTask(final LoadDataTask loadDataTask) {
                        final LoadDeviceInfoTask<Bitmap> task = LoadTextureRunnable.this.task;
                        Log.d(ContentManager.TAG, "Texture loaded for color code " + task.hexCode + " image type " + task.imageType);
                        final Bitmap decodeBitmap = ContentManager.decodeBitmap(loadDataTask.data);
                        ContentManager.this.contentCache.put((Object)loadDataTask.url, (Object)decodeBitmap);
                        ContentManager.this.memoryCache.put((Object)loadDataTask.url, (Object)loadDataTask.data);
                        ContentManager.this.saveToFileCache(loadDataTask.data, buildBitmapURL);
                        if (task.listener != null) {
                            task.listener.onLoadSuccess(decodeBitmap, task.hexCode, task.imageType);
                        }
                    }
                    
                    @Override
                    public void onError(final LoadDataTask loadDataTask, final Throwable t) {
                        final LoadDeviceInfoTask<Bitmap> task = LoadTextureRunnable.this.task;
                        Log.e(ContentManager.TAG, "Texture loading failed for color code " + task.hexCode + " image type " + task.imageType);
                        if (!(t instanceof MalformedURLException) && !(t instanceof FileNotFoundException) && !(t instanceof UnknownHostException)) {
                            if (ContentManager.mRetry < 10) {
                                ContentManager.this.loadBitmap(task.hexCode, task.imageType, task.listener);
                                Log.d(ContentManager.TAG, "Retry texture load for color code " + task.hexCode + " image type " + task.imageType);
                                ContentManager.mRetry = ContentManager.mRetry;
                            }
                            else if (ContentManager.mRetry == 10) {
                                ContentManager.mRetry = 0;
                            }
                        }
                    }
                });
            }
        }
    }
}
