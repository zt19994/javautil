package com.util.httpclientutil;

import com.util.traceutil.StackTraceUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient 工具类
 *
 * @author zt1994 2019/8/16 11:49
 */
public class HttpClientUtil {

  private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

  /**
   * 请求设置
   */
  private CookieStore cookieStore = new BasicCookieStore();

  /**
   * 请求配置
   */
  private RequestConfig requestConfig = RequestConfig.custom()
      .setSocketTimeout(15000)
      .setConnectTimeout(15000)
      .setConnectionRequestTimeout(15000)
      .setCookieSpec(CookieSpecs.STANDARD)
      .build();

  private static HttpClientUtil instance = null;

  private HttpClientUtil() {
  }

  /**
   * 获取httpClient实例
   */
  public static HttpClientUtil getInstance() {
    if (instance == null) {
      instance = new HttpClientUtil();
    }
    return instance;
  }

  /**
   * https代码块
   */
  private static final String HTTP = "http";

  private static final String HTTPS = "https";

  private static SSLConnectionSocketFactory SSL_SOCKET_FACTORY = null;

  private static PoolingHttpClientConnectionManager CM = null;

  static {
    try {
      SSLContextBuilder builder = new SSLContextBuilder();
      // 全部信任 不做身份鉴定
      builder.loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true);
      SSL_SOCKET_FACTORY = new SSLConnectionSocketFactory(builder.build(),
          /* new String[]{"TLSv1", "TLSv1.1", "TLSv1.2", "SSLv3", "SSLv2Hello"},*/
          new String[]{"TLSv1.2"},
          null,
          NoopHostnameVerifier.INSTANCE);
      Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
          .register(HTTP, new PlainConnectionSocketFactory())
          .register(HTTPS, SSL_SOCKET_FACTORY)
          .build();
      CM = new PoolingHttpClientConnectionManager(registry);
      // max connection
      CM.setMaxTotal(200);
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    }
  }


  /**
   * 发送 get请求
   */
  public String sendHttpGet(String httpUrl) {
    // 创建get请求
    HttpGet httpGet = new HttpGet(httpUrl);
    return sendHttpGet(httpGet);
  }


  /**
   * 发送 post请求
   *
   * @param httpUrl 地址
   */
  public String sendHttpPost(String httpUrl) {
    // 创建httpPost
    HttpPost httpPost = new HttpPost(httpUrl);
    return sendHttpPost(httpPost);
  }


  /**
   * 发送 post请求
   *
   * @param httpUrl 地址
   * @param params 参数(格式:key1=value1&key2=value2)
   */
  public String sendHttpPost(String httpUrl, String params) {
    // 创建httpPost
    HttpPost httpPost = new HttpPost(httpUrl);
    try {
      //设置参数
      StringEntity stringEntity = new StringEntity(params, "UTF-8");
      stringEntity.setContentType("application/x-www-form-urlencoded");
      httpPost.setEntity(stringEntity);
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    }
    return sendHttpPost(httpPost);
  }


  /**
   * 发送 post请求
   *
   * @param httpUrl 地址
   * @param maps 参数
   */
  public String sendHttpPost(String httpUrl, Map<String, String> maps) {
    // 创建httpPost
    HttpPost httpPost = new HttpPost(httpUrl);
    // 创建参数队列
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    for (String key : maps.keySet()) {
      nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
    }
    try {
      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    }
    return sendHttpPost(httpPost);
  }


  /**
   * 发送Post请求(创建post请求客户端)
   */
  private String sendHttpPost(HttpPost httpPost) {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    String responseContent = null;
    try {
      // 创建默认的httpClient实例.
      httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
      httpPost.setConfig(requestConfig);
      // 执行请求
      response = httpClient.execute(httpPost);

      entity = response.getEntity();
      responseContent = EntityUtils.toString(entity, "UTF-8");
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    } finally {
      try {
        // 关闭连接,释放资源
        if (response != null) {
          response.close();
        }
        if (httpClient != null) {
          httpClient.close();
        }
      } catch (IOException e) {
        logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
      }
    }
    return responseContent;
  }


  /**
   * 发送Get请求(创建get请求客户端)
   */
  private String sendHttpGet(HttpGet httpGet) {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    String responseContent = null;
    try {
      // 创建默认的httpClient实例.
      httpClient = HttpClients.custom()
          .setDefaultCookieStore(cookieStore)
          .setRedirectStrategy(new LaxRedirectStrategy())
          .build();
      httpGet.setConfig(requestConfig);
      // 执行请求
      response = httpClient.execute(httpGet);
      entity = response.getEntity();
      responseContent = EntityUtils.toString(entity, "UTF-8");
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    } finally {
      try {
        // 关闭连接,释放资源
        if (response != null) {
          response.close();
        }
        if (httpClient != null) {
          httpClient.close();
        }
      } catch (IOException e) {
        logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
      }
    }
    return responseContent;
  }


  /**
   * 发送https的get请求，带用户认证
   *
   * @param httpUrl 地址
   * @param username 用户名
   * @param password 密码
   */
  public String sendHttpsGet(String httpUrl, String username, String password) {
    // 创建get请求
    HttpGet httpGet = new HttpGet(httpUrl);
    return sendHttpsGet(httpGet, username, password);
  }


  /**
   * 创建https的 (get请求客户端https)
   *
   * @param httpGet get请求
   * @param username 用户名
   * @param password 密码
   */
  private String sendHttpsGet(HttpGet httpGet, String username, String password) {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    String responseContent = null;

    try {
      // 设置认证
      CredentialsProvider provider = new BasicCredentialsProvider();
      if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
      }
      // 创建默认的httpClient实例.
      httpClient = HttpClients.custom()
          .setDefaultCookieStore(cookieStore)
          .setRedirectStrategy(new LaxRedirectStrategy())
          .setSSLSocketFactory(SSL_SOCKET_FACTORY)
          .setConnectionManager(CM)
          .setConnectionManagerShared(true)
          .setDefaultCredentialsProvider(provider)
          .build();
      httpGet.setConfig(requestConfig);
      // 执行请求
      response = httpClient.execute(httpGet);
      entity = response.getEntity();
      responseContent = EntityUtils.toString(entity, "UTF-8");
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    } finally {
      try {
        // 关闭连接,释放资源
        if (response != null) {
          response.close();
        }
        if (httpClient != null) {
          httpClient.close();
        }
      } catch (IOException e) {
        logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
      }
    }
    return responseContent;
  }


  /**
   * 发送 post请求
   *
   * @param httpUrl 地址
   * @param username 用户名
   * @param password 密码
   */
  public String sendHttpsPost(String httpUrl, String username, String password) {
    // 创建httpPost
    HttpPost httpPost = new HttpPost(httpUrl);
    return sendHttpsPost(httpPost, username, password);
  }


  /**
   * 发送 post请求
   *
   * @param httpUrl 地址
   * @param params 参数(格式:key1=value1&key2=value2)
   * @param username 用户名
   * @param password 密码
   */
  public String sendHttpsPost(String httpUrl, String params, String username, String password) {
    // 创建httpPost
    HttpPost httpPost = new HttpPost(httpUrl);
    try {
      //设置参数
      StringEntity stringEntity = new StringEntity(params, "UTF-8");
      stringEntity.setContentType("application/x-www-form-urlencoded");
      httpPost.setEntity(stringEntity);
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    }
    return sendHttpsPost(httpPost, username, password);
  }


  /**
   * 创建https的 (post请求客户端https)
   *
   * @param httpPost post请求
   * @param username 用户名
   * @param password 密码
   */
  private String sendHttpsPost(HttpPost httpPost, String username, String password) {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    String responseContent = null;

    try {
      // 设置认证
      CredentialsProvider provider = new BasicCredentialsProvider();
      if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
      }
      // 创建默认的httpClient实例.
      httpClient = HttpClients.custom()
          .setDefaultCookieStore(cookieStore)
          .setRedirectStrategy(new LaxRedirectStrategy())
          .setSSLSocketFactory(SSL_SOCKET_FACTORY)
          .setConnectionManager(CM)
          .setConnectionManagerShared(true)
          .setDefaultCredentialsProvider(provider)
          .build();
      httpPost.setConfig(requestConfig);
      // 执行请求
      response = httpClient.execute(httpPost);

      Header[] headers = response.getAllHeaders();
      String location = null;
      String authToken = null;
      for (Header header : headers) {
        if (StringUtils.equals(header.getName(), "Location")) {
          location = header.getValue();
        } else if (StringUtils.equals(header.getName(), "X-Cisco-CMS-Auth-Token")) {
          authToken = header.getValue();
        }
      }
      entity = response.getEntity();
      if (location != null) {
        responseContent = location;
      } else if (authToken != null) {
        responseContent = authToken;
      } else {
        responseContent = EntityUtils.toString(entity, "UTF-8");
      }
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    } finally {
      try {
        // 关闭连接,释放资源
        if (response != null) {
          response.close();
        }
        if (httpClient != null) {
          httpClient.close();
        }
      } catch (IOException e) {
        logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
      }
    }
    return responseContent;
  }


  /**
   * 发送 put 请求
   *
   * @param httpUrl 地址
   * @param username 用户名
   * @param password 密码
   */
  public String sendHttpsPut(String httpUrl, String username, String password) {
    // 创建httpPut
    HttpPut httpPut = new HttpPut(httpUrl);
    return sendHttpsPut(httpPut, username, password);
  }


  /**
   * 发送 put 请求
   *
   * @param httpUrl 地址
   * @param params 参数(格式:key1=value1&key2=value2)
   * @param username 用户名
   * @param password 密码
   */
  public String sendHttpsPut(String httpUrl, String params, String username, String password) {
    // 创建httpPut
    HttpPut httpPut = new HttpPut(httpUrl);
    try {
      //设置参数
      StringEntity stringEntity = new StringEntity(params, "UTF-8");
      stringEntity.setContentType("application/x-www-form-urlencoded");
      httpPut.setEntity(stringEntity);
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    }
    return sendHttpsPut(httpPut, username, password);
  }


  /**
   * 创建https的 (put请求客户端https)
   *
   * @param httpPut put 请求
   * @param username 用户名
   * @param password 密码
   */
  private String sendHttpsPut(HttpPut httpPut, String username, String password) {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    String responseContent = null;

    try {
      // 设置认证
      CredentialsProvider provider = new BasicCredentialsProvider();
      if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
      }
      // 创建默认的httpClient实例.
      httpClient = HttpClients.custom()
          .setDefaultCookieStore(cookieStore)
          .setRedirectStrategy(new LaxRedirectStrategy())
          .setSSLSocketFactory(SSL_SOCKET_FACTORY)
          .setConnectionManager(CM)
          .setConnectionManagerShared(true)
          .setDefaultCredentialsProvider(provider)
          .build();
      httpPut.setConfig(requestConfig);
      // 执行请求
      response = httpClient.execute(httpPut);

      entity = response.getEntity();
      responseContent = EntityUtils.toString(entity, "UTF-8");
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    } finally {
      try {
        // 关闭连接,释放资源
        if (response != null) {
          response.close();
        }
        if (httpClient != null) {
          httpClient.close();
        }
      } catch (IOException e) {
        logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
      }
    }
    return responseContent;
  }


  /**
   * 发送 delete 请求
   *
   * @param httpUrl 地址链接
   * @param username 用户名
   * @param password 密码
   */
  public String sendHttpsDelete(String httpUrl, String username, String password) {
    // 创建get请求
    HttpDelete httpDelete = new HttpDelete(httpUrl);
    return sendHttpsDelete(httpDelete, username, password);
  }


  /**
   * 创建 https 的 delete 请求
   */
  private String sendHttpsDelete(HttpDelete httpDelete, String username, String password) {
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    String responseContent = null;

    try {
      // 设置认证
      CredentialsProvider provider = new BasicCredentialsProvider();
      if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
      }
      // 创建默认的httpClient实例.
      httpClient = HttpClients.custom()
          .setDefaultCookieStore(cookieStore)
          .setRedirectStrategy(new LaxRedirectStrategy())
          .setSSLSocketFactory(SSL_SOCKET_FACTORY)
          .setConnectionManager(CM)
          .setConnectionManagerShared(true)
          .setDefaultCredentialsProvider(provider)
          .build();
      httpDelete.setConfig(requestConfig);
      // 执行请求
      response = httpClient.execute(httpDelete);
      entity = response.getEntity();
      responseContent = EntityUtils.toString(entity, "UTF-8");
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    } finally {
      try {
        // 关闭连接,释放资源
        if (response != null) {
          response.close();
        }
        if (httpClient != null) {
          httpClient.close();
        }
      } catch (IOException e) {
        logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
      }
    }
    return responseContent;
  }


  /**
   * 创建https的 (get请求客户端https)
   * 若成功则返回base64图片，否则返回""
   * @param httpUrl 请求url
   * @param username 用户名
   * @param password 密码
   */
  public String sendHttpsSnapshotGet(String httpUrl, String username, String password) {
    HttpGet httpGet = new HttpGet(httpUrl);
    BASE64Encoder base64Encoder = new BASE64Encoder();
    CloseableHttpClient httpClient = null;
    CloseableHttpResponse response = null;
    HttpEntity entity = null;
    String responseContent = null;
    String result="";
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    try {
      // 设置认证
      CredentialsProvider provider = new BasicCredentialsProvider();
      if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
      }
      // 创建默认的httpClient实例.
      httpClient = HttpClients.custom()
          .setDefaultCookieStore(cookieStore)
          .setRedirectStrategy(new LaxRedirectStrategy())
          .setSSLSocketFactory(SSL_SOCKET_FACTORY)
          .setConnectionManager(CM)
          .setConnectionManagerShared(true)
          .setDefaultCredentialsProvider(provider)
          .build();
      httpGet.setConfig(requestConfig);
      // 执行请求
      response = httpClient.execute(httpGet);
      entity = response.getEntity();
      //返回类型，在有快照的时候返回图片，否则是返回一个xml  Content-Type: text/xml  Content-Type: image/jpeg
      Header contentType = entity.getContentType();
      if(null!=contentType&&"Content-Type: text/xml".equals(contentType.toString())){
        return result;
      }
      entity.writeTo(outStream);
      //得到图片的二进制数据，以二进制封装得到数据，具有通用性
      byte[] data = outStream.toByteArray();
      String snapshot = base64Encoder.encode(data);
     // base64StringToImage(snapshot);
      result=snapshot;
    } catch (Exception e) {
      logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
    } finally {
      try {
        // 关闭连接,释放资源
        if (response != null) {
          response.close();
        }
        if (httpClient != null) {
          httpClient.close();
        }
      } catch (IOException e) {
        logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
      }
    }
    return result;
  }

  /**
   * base64图片写到本地
   * @param base64String
   */
  private void base64StringToImage(String base64String){
    try {
      BASE64Decoder decoder = new BASE64Decoder();
      byte[] bytes1 = decoder.decodeBuffer(base64String);
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
      BufferedImage bi1 = ImageIO.read(bais);
      File w2 = new File("d://2.image");//可以是jpg,png,gif格式
      ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动
    } catch (IOException e) {
      logger.error("base64图片写到本地失败");

    }
  }
}
