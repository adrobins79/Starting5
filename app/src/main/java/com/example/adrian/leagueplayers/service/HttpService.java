package com.example.adrian.leagueplayers.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;

public class HttpService {


    private final static int GET = 1;
    private final static int POST = 2;

    public HttpService() {

    }

    public String get(String url) throws ServiceException {
        return request(url, GET, null);
    }
    public String get(String url, List<NameValuePair> params) throws ServiceException {
        return request(url, GET, params);
    }
    public String post(String url, List<NameValuePair> params) throws ServiceException {
        return request(url, POST, params);
    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    private String request(String url, int method, List<NameValuePair> params) throws ServiceException  {
        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }

            //TODO check for 200
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

            if(response == null){
                throw new ServiceException(url + " : no response available ");
            }

        } catch (Exception e) {
            throw new ServiceException(url + " failed ",e);
        }

        return response;

    }
}