/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.util.network;

import alluxio.Configuration;
import alluxio.Constants;
import alluxio.PropertyKey;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

/**
 * Utility methods for working with http.
 */
public final class HttpUtils {

  private static final boolean IS_PROXY = Configuration.getBoolean(PropertyKey.MASTER_PROXY_ENABLE);
  private static final String PROXY_HOST = Configuration.get(PropertyKey.MASTER_PROXY_HOST);
  private static final int PROXY_PORT = Configuration.getInt(PropertyKey.MASTER_PROXY_PORT);
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  private HttpUtils() {
  }

  /**
   * use put method to send params by http.
   * @param url the http url
   * @param param the param
   * @param timeout socket timeout and connection timeout
   * @return the response body
   */
  public static String sendPut(String url, Map<String, String> param, Integer timeout) {
    StringBuilder contentBuffer = new StringBuilder();
    PutMethod putMethod = null;
    try {
      HttpClient httpClient = new HttpClient();
      if (IS_PROXY) {
        httpClient.getHostConfiguration().setProxy(PROXY_HOST, PROXY_PORT);
      }
      // 设置超时时间
      if (null != timeout) {
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
      }

      putMethod = new PutMethod(url);
      putMethod.addRequestHeader("Content-Type", "application/json");
      putMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
      Set<String> keys = param.keySet();
      NameValuePair[] data = new NameValuePair[keys.size()];
      int idx = 0;
      for (Map.Entry<String, String> entry : param.entrySet()) {
        String value = entry.getValue();
        NameValuePair item = new NameValuePair(entry.getKey(), value);
        data[idx] = item;
        idx++;
      }
      if (data.length > 0) {
        putMethod.setQueryString(data);
      }
      int statusCode = httpClient.executeMethod(putMethod);
      if (statusCode == HttpStatus.SC_OK) {
        BufferedReader br = new BufferedReader(
            new InputStreamReader(putMethod.getResponseBodyAsStream(), "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
          contentBuffer.append(line);
        }
        br.close();
      } else if (statusCode == HttpStatus.SC_CREATED) {
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(putMethod.getResponseBodyAsStream(), "UTF-8"))) {
          String line;
          while ((line = br.readLine()) != null) {
            contentBuffer.append(line);
          }
        }
      } else {
        LOG.error("HTTP PUT request error! statusCode:" + statusCode);
      }
    } catch (Exception e) {
      LOG.error("HTTP PUT request error!", e);
    } finally {
      if (putMethod != null) {
        putMethod.releaseConnection();
      }
    }
    return contentBuffer.toString();
  }

  /**
   * use POST method to send params by http.
   *
   * @param url     full path with scheme
   * @param param   the param
   * @param timeout socket timeout and connection timeout
   * @return return contents
   */
  public static String sendPost(String url, Map<String, String> param, Integer timeout) {
    Set<String> keys = param.keySet();
    NameValuePair[] data = new NameValuePair[keys.size()];
    int idx = 0;
    for (String key : keys) {
      String value = param.get(key);
      NameValuePair item = new NameValuePair(key, value);
      data[idx] = item;
      idx++;
    }

    StringBuilder contentBuffer = new StringBuilder();
    PostMethod postMethod = null;
    try {
      HttpClient httpClient = new HttpClient();
      if (IS_PROXY) {
        httpClient.getHostConfiguration().setProxy(PROXY_HOST, PROXY_PORT);
      }
      if (null != timeout) {
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
      }

      postMethod = new PostMethod(url);
      postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

      postMethod.setRequestBody(data);

      int statusCode = httpClient.executeMethod(postMethod);
      if (statusCode == HttpStatus.SC_OK) {
        BufferedReader br = new BufferedReader(
            new InputStreamReader(postMethod.getResponseBodyAsStream(), "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
          contentBuffer.append(line);
        }
        br.close();
      } else {
        LOG.error("HTTP GET request error! statusCode:" + statusCode);
      }
    } catch (Exception e) {
      LOG.error("HTTP POST request error!", e);
    } finally {
      if (postMethod != null) {
        postMethod.releaseConnection();
      }
    }
    return contentBuffer.toString();
  }

  /**
   * use POST method to send params by http.
   *
   * @param url   full path with scheme
   * @param param the param
   * @return the contents
   */
  public static String sendPost(String url, Map<String, String> param) {
    return sendPost(url, param, 5000);
  }

  /**
   * use GET method to send params by http.
   *
   * @param url     full path with scheme
   * @param timeout wait for until timeout
   * @return return the content
   */
  public static String sendGet(String url, Integer timeout) {
    StringBuilder contentBuffer = new StringBuilder();
    GetMethod getMethod = null;
    try {
      HttpClient httpClient = new HttpClient();
      if (IS_PROXY) {
        httpClient.getHostConfiguration().setProxy(PROXY_HOST, PROXY_PORT);
      }
      // 设置超时时间
      if (null != timeout) {
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);
      }
      getMethod = new GetMethod(url);
      getMethod.setRequestHeader("Content-Type", "application/json");
      
      getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
      HttpMethodParams params = new HttpMethodParams();

      int statusCode = httpClient.executeMethod(getMethod);
      if (statusCode == HttpStatus.SC_OK) {
        BufferedReader br = new BufferedReader(
            new InputStreamReader(getMethod.getResponseBodyAsStream(), "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
          contentBuffer.append(line);
        }
        br.close();
      } else {
        LOG.error("HTTP GET request error! status code :" + statusCode);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("HTTP GET request error!", e);
    } finally {
      if (getMethod != null) {
        getMethod.releaseConnection();
      }
    }
    return contentBuffer.toString();
  }

  /**
   * use GET method to send params by http.
   *
   * @param url     full path with scheme
   * @return return the content
   */
  public static String sendGet(String url) {
    return sendGet(url, 1000);
  }
}
