package com.example.administrator.widgetdemo.utils;

/**
 * Created by zy on 17/3/9.
 */

import android.app.Activity;

import com.example.administrator.widgetdemo.Constant;
import com.qiniu.android.http.Client;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.utils.StringMap;
import com.qiniu.android.utils.StringUtils;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author:huangxiaoming
 * Date:2018/4/19
 * Desc:
 * Version:
 */
public final class QiniuAuth {

    /**
     * 上传策略
     * 参考文档：<a href="https://developer.qiniu.com/kodo/manual/put-policy">上传策略</a>
     */
    private static final String[] policyFields = new String[]{
            "callbackUrl",
            "callbackBody",
            "callbackHost",
            "callbackBodyType",
            "callbackFetchKey",

            "returnUrl",
            "returnBody",

            "endUser",
            "saveKey",
            "insertOnly",

            "detectMime",
            "mimeLimit",
            "fsizeLimit",
            "fsizeMin",

            "persistentOps",
            "persistentNotifyUrl",
            "persistentPipeline",

            "deleteAfterDays",
    };
    private static final String[] deprecatedPolicyFields = new String[]{
            "asyncOps",
    };
    private String accessKey;
    private SecretKeySpec secretKey;
    private String uploadToken;

    private QiniuAuth(String accessKey, SecretKeySpec secretKeySpec) {
        this.accessKey = accessKey;
        this.secretKey = secretKeySpec;
    }

    public static QiniuAuth create(String accessKey, String secretKey) {
        if (StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey)) {
            throw new IllegalArgumentException("empty key");
        }
        byte[] sk = StringUtils.utf8Bytes(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(sk, "HmacSHA1");
        return new QiniuAuth(accessKey, secretKeySpec);
    }

    private static void copyPolicy(final StringMap policy, StringMap originPolicy, final boolean strict) {
        if (originPolicy == null) {
            return;
        }
        originPolicy.forEach(new StringMap.Consumer() {
            @Override
            public void accept(String key, Object value) {
                if (inStringArray(key, deprecatedPolicyFields)) {
                    throw new IllegalArgumentException(key + " is deprecated!");
                }
                if (!strict || inStringArray(key, policyFields)) {
                    policy.put(key, value);
                }
            }
        });
    }

    private Mac createMac() {
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return mac;
    }

    public String sign(byte[] data) {
        Mac mac = createMac();
        String encodedSign = UrlSafeBase64.encodeToString(mac.doFinal(data));
        return this.accessKey + ":" + encodedSign;
    }

    public String sign(String data) {
        return sign(StringUtils.utf8Bytes(data));
    }

    public String signWithData(byte[] data) {
        String s = UrlSafeBase64.encodeToString(data);
        return sign(StringUtils.utf8Bytes(s)) + ":" + s;
    }

    public String signWithData(String data) {
        return signWithData(StringUtils.utf8Bytes(data));
    }

    /**
     * 生成HTTP请求签名字符串
     *
     * @param urlString
     * @param body
     * @param contentType
     * @return
     */
    public String signRequest(String urlString, byte[] body, String contentType) {
        URI uri = URI.create(urlString);
        String path = uri.getRawPath();
        String query = uri.getRawQuery();

        Mac mac = createMac();

        mac.update(StringUtils.utf8Bytes(path));

        if (query != null && query.length() != 0) {
            mac.update((byte) ('?'));
            mac.update(StringUtils.utf8Bytes(query));
        }
        mac.update((byte) '\n');
        if (body != null && Client.FormMime.equalsIgnoreCase(contentType)) {
            mac.update(body);
        }

        String digest = UrlSafeBase64.encodeToString(mac.doFinal());

        return this.accessKey + ":" + digest;
    }

    /**
     * 验证回调签名是否正确
     *
     * @param originAuthorization 待验证签名字符串，以 "QBox "作为起始字符
     * @param url                 回调地址
     * @param body                回调请求体。原始请求体，不要解析后再封装成新的请求体--可能导致签名不一致。
     * @param contentType         回调ContentType
     * @return
     */
    public boolean isValidCallback(String originAuthorization, String url, byte[] body, String contentType) {
        String authorization = "QBox " + signRequest(url, body, contentType);
        return authorization.equals(originAuthorization);
    }

    /**
     * 下载签名
     *
     * @param baseUrl 待签名文件url，如 http://img.domain.com/u/3.jpg 、
     *                http://img.domain.com/u/3.jpg?imageView2/1/w/120
     * @return
     */
    public String privateDownloadUrl(String baseUrl) {
        return privateDownloadUrl(baseUrl, 3600);
    }

    /**
     * 下载签名
     *
     * @param baseUrl 待签名文件url，如 http://img.domain.com/u/3.jpg 、
     *                http://img.domain.com/u/3.jpg?imageView2/1/w/120
     * @param expires 有效时长，单位秒。默认3600s
     * @return
     */
    public String privateDownloadUrl(String baseUrl, long expires) {
        long deadline = System.currentTimeMillis() / 1000 + expires;
        return privateDownloadUrlWithDeadline(baseUrl, deadline);
    }

    String privateDownloadUrlWithDeadline(String baseUrl, long deadline) {
        StringBuilder b = new StringBuilder();
        b.append(baseUrl);
        int pos = baseUrl.indexOf("?");
        if (pos > 0) {
            b.append("&e=");
        } else {
            b.append("?e=");
        }
        b.append(deadline);
        String token = sign(StringUtils.utf8Bytes(b.toString()));
        b.append("&token=");
        b.append(token);
        return b.toString();
    }

    /**
     * scope = bucket
     * 一般情况下可通过此方法获取token
     *
     * @param bucket 空间名
     * @return 生成的上传token
     */
    public String uploadToken(String bucket) {
        return uploadToken(bucket, null, 3600, null, true);
    }

    /**
     * scope = bucket:key
     * 同名文件覆盖操作、只能上传指定key的文件可以可通过此方法获取token
     *
     * @param bucket 空间名
     * @param key    key，可为 null
     * @return 生成的上传token
     */
    public String uploadToken(String bucket, String key) {
        return uploadToken(bucket, key, 3600, null, true);
    }

    /**
     * 生成上传token
     *
     * @param bucket  空间名
     * @param key     key，可为 null
     * @param expires 有效时长，单位秒
     * @param policy  上传策略的其它参数，如 new StringMap().put("endUser", "uid").putNotEmpty("returnBody", "")。
     *                scope通过 bucket、key间接设置，deadline 通过 expires 间接设置
     * @return 生成的上传token
     */
    public String uploadToken(String bucket, String key, long expires, StringMap policy) {
        return uploadToken(bucket, key, expires, policy, true);
    }

    /**
     * 生成上传token
     *
     * @param bucket  空间名
     * @param key     key，可为 null
     * @param expires 有效时长，单位秒。默认3600s
     * @param policy  上传策略的其它参数，如 new StringMap().put("endUser", "uid").putNotEmpty("returnBody", "")。
     *                scope通过 bucket、key间接设置，deadline 通过 expires 间接设置
     * @param strict  是否去除非限定的策略字段，默认true
     * @return 生成的上传token
     */
    public String uploadToken(String bucket, String key, long expires, StringMap policy, boolean strict) {
        long deadline = System.currentTimeMillis() / 1000 + expires;
        return uploadTokenWithDeadline(bucket, key, deadline, policy, strict);
    }

    public String uploadTokenWithDeadline(String bucket, String key, long deadline, StringMap policy, boolean strict) {
        // TODO   UpHosts Global
        String scope = bucket;
        if (key != null) {
            scope = bucket + ":" + key;
        }
        StringMap x = new StringMap();
        copyPolicy(x, policy, strict);
        x.put("scope", scope);
        x.put("deadline", deadline);

        String s = JsonUtil.encode(x);
        return signWithData(StringUtils.utf8Bytes(s));
    }

    public static boolean inStringArray(String s, String[] array) {
        for (String x : array) {
            if (x.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private static QiniuAuth qiniuAuth;

    public static QiniuAuth getInstence() {
        if (qiniuAuth == null) {
            synchronized (QiniuAuth.class) {
                if (qiniuAuth == null) {
                    if (StringUtils.isNullOrEmpty(Constant.QINIU_ACCESS_KEY) || StringUtils.isNullOrEmpty(Constant.QINIU_SECRET_KEY)) {
                        throw new IllegalArgumentException("empty key");
                    }
                    byte[] sk = StringUtils.utf8Bytes(Constant.QINIU_SECRET_KEY);
                    SecretKeySpec secretKeySpec = new SecretKeySpec(sk, "HmacSHA1");
                    qiniuAuth = new QiniuAuth(Constant.QINIU_ACCESS_KEY,secretKeySpec);
                }
            }
        }
        return qiniuAuth;
    }


    public interface IQiniuListener{
        void uploadImageSuccess(String url,int uploadSuccessCount);
        void uploadImageFailed(String error);
//        void uploadImageCanceled();
    }

    public IQiniuListener mIQiniuListener;

    public void init(final IQiniuListener qiniuListener, final List<String> filePathList, final Activity context){
        if(filePathList == null || filePathList.size() == 0){
            return;
        }
        this.mIQiniuListener = qiniuListener;
        uploadToken = qiniuAuth.uploadToken(Constant.QINIU_BUCKET_NAME);

        //文件名
        String fileName;
        //上传的文件
        File uploadFile;
        //上传管理器
        UploadManager uploadManager = new UploadManager();
        UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
            int uploadSuccessCount = 0;
            /**
             * 上传完成的回调
             * @param key
             * @param info
             * @param response
             */
            @Override
            public void complete(final String key, final ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    uploadSuccessCount++;
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            qiniuListener.uploadImageSuccess(Constant.QINIU_IMAGE_HOST+key,uploadSuccessCount);
                        }
                    });
                }else if(info.isServerError() || info.isNetworkBroken()){
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            qiniuListener.uploadImageFailed(info.error);
                        }
                    });
                }else if(info.isCancelled()){
//                    context.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            qiniuListener.uploadImageCanceled();
//                        }
//                    });
                }
            }
        };

        UploadOptions uploadOptions = new UploadOptions(null, null, false, new UpProgressHandler() {
            /**
             * 上传进度
             * @param key
             * @param percent
             */
            @Override
            public void progress(String key, double percent) {
                LogUtil.defLog("key=" + key + "&percent=" + Double.toString(percent));
            }

        }, new UpCancellationSignal() {
            /**
             * 是否取消上传
             * @return
             */
            @Override
            public boolean isCancelled() {
                return false;
            }
        });

        for (String filePath:filePathList) {

            fileName = System.currentTimeMillis() + "." + filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
            uploadFile = new File(filePath);

            uploadManager.put(uploadFile, fileName, uploadToken,upCompletionHandler,uploadOptions);
        }
    }
}
