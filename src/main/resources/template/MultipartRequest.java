package com.jtool.codegen.api.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

public class MultipartRequest<T> extends Request<T> {

    private String BOUNDARY = UUID.randomUUID().toString();
    private String PREFIX = "--";
    private String LINE_END = "\r\n";
    private String MULTIPART_FROM_DATA = "multipart/form-data";
    private String CHARSET = "UTF-8";

    private final Response.Listener<T> mListener;
    private final Response.ErrorListener mErrorListener;
    private Map<String, Object> params;
    private Class<T> mClass;

    public MultipartRequest(String url, Map<String, Object> params, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.params = params;
        this.mClass = clazz;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new Gson().fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {

            byteArrayOutputStream = new ByteArrayOutputStream();

            for(String key : params.keySet()) {
                if(params.get(key) != null) {
                    if(params.get(key) instanceof File) {
                        File file = (File)params.get(key);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(PREFIX);
                        stringBuilder.append(BOUNDARY);
                        stringBuilder.append(LINE_END);
                        stringBuilder.append("Content-Disposition: form-data;name=\"" + key + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
                        stringBuilder.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                        stringBuilder.append(LINE_END);

                        byteArrayOutputStream.write(stringBuilder.toString().getBytes("UTF-8"));
                        FileInputStream fileInputStream = null;

                        try {
                            fileInputStream = new FileInputStream(file);
                            byte[] buffer = new byte[1024];
                            int len = 0;
                            while ((len = fileInputStream.read(buffer)) != -1) {
                                byteArrayOutputStream.write(buffer, 0, len);
                            }
                        } finally {
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e) {
                                    fileInputStream = null;
                                }
                            }
                        }
                        byteArrayOutputStream.write(LINE_END.getBytes("UTF-8"));
                    } else {
                        String paramStr = params.get(key).toString();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(PREFIX);
                        stringBuilder.append(BOUNDARY);
                        stringBuilder.append(LINE_END);
                        stringBuilder.append("Content-Disposition: form-data;name=\"" + key + "\"" + LINE_END);
                        stringBuilder.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);
                        stringBuilder.append(LINE_END);
                        stringBuilder.append(paramStr);
                        stringBuilder.append(LINE_END);

                        byteArrayOutputStream.write(stringBuilder.toString().getBytes("UTF-8"));
                    }
                }
            }

            byteArrayOutputStream.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());

            byteArrayOutputStream.flush();

            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
            return new ByteArrayOutputStream().toByteArray();
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    byteArrayOutputStream = null;
                }
            }
        }
    }
}