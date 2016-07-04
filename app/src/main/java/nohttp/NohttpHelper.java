package nohttp;

import com.example.jiawei.nohttpapi.BaseActivity;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by zhang on 2016/7/4.
 */
public class NohttpHelper {

    private static final String TAG = "NohttpHelper";
    private static NohttpHelper callServer;
    private RequestQueue requestQueue;

    private NohttpHelper() {
        requestQueue = NoHttp.newRequestQueue();
    }

    public synchronized static NohttpHelper getRequestInstance() {
        if (callServer == null)
            callServer = new NohttpHelper();
        return callServer;
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param context   context用来实例化dialog.
     * @param what      用来标志请求, 当多个请求使用同一个{@link HttpListener}时, 在回调方法中会返回这个what.
     * @param url   请求地址.
     * @param callback  结果回调对象.
     * @param canCancel 是否允许用户取消请求.
     * @param isLoading 是否显示dialog.
     */
    public void get(BaseActivity context,int what,String url, HttpListener<String> callback, boolean canCancel,boolean isLoading){
        Request<String>  request = NoHttp.createStringRequest(url, RequestMethod.GET);
        if (request != null) {
            request.add("userName", "yolanda");// String类型
            requestQueue.add(what, request, new HttpResponseListener<String>(context, request, callback, canCancel, isLoading));
        }

    }
    public void post(BaseActivity context, int what, String url, HttpListener<String> callback, boolean canCancel, boolean isLoading){
        Request<String>  request = NoHttp.createStringRequest(url, RequestMethod.POST);
        if (request != null) {
            request.add("userName", "yolanda");// String类型
            request.add("userPass", "yolanda.pass");
            request.add("userAge", 20);// int类型
            request.add("userSex", '1');// char类型，还支持其它类型
            requestQueue.add(what, request, new HttpResponseListener<String>(context, request, callback, canCancel, isLoading));
        }
    }
    public static void upload(){}
    public static void download(){

    }

    /**
     * 取消这个sign标记的所有请求.
     */
    public void cancelBySign(Object sign) {
        requestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求.
     */
    public void cancelAll() {
        requestQueue.cancelAll();
    }

    /**
     * 退出app时停止所有请求.
     */
    public void stopAll() {
        requestQueue.stop();
    }

}
