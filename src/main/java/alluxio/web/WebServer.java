package alluxio.web;

import alluxio.AlluxioURI;
import alluxio.Configuration;
import alluxio.Constants;
import alluxio.PropertyKey;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * alluxio.web <br>
 * <p>
 * Copyright: Copyright (c) 17-4-4 下午8:45
 * <p>
 * Company: 京东
 * <p>
 *
 * @author maobaolong@jd.com
 * @version 1.0.0
 */
public class WebServer {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  protected final WebAppContext mWebAppContext;
  private final Server mServer;
  private final String mServiceName;
  private InetSocketAddress mAddress;
  private final ServerConnector mServerConnector;

  public WebServer(String serviceName, InetSocketAddress address) {
    Preconditions.checkNotNull(serviceName, "Service name cannot be null");
    Preconditions.checkNotNull(address, "Server address cannot be null");

    mAddress = address;
    mServiceName = serviceName;

    QueuedThreadPool threadPool = new QueuedThreadPool();
    int webThreadCount = Configuration.getInt(PropertyKey.WEB_THREADS);

    // Jetty needs at least (1 + selectors + acceptors) threads.
    threadPool.setMinThreads(webThreadCount * 2 + 1);
    threadPool.setMaxThreads(webThreadCount * 2 + 100);

    mServer = new Server(threadPool);

    mServerConnector = new ServerConnector(mServer);
    mServerConnector.setPort(mAddress.getPort());
    mServerConnector.setHost(mAddress.getAddress().getHostAddress());

    mServer.addConnector(mServerConnector);

    // Open the connector here so we can resolve the port if we are selecting a free port.
    try {
      mServerConnector.open();
    } catch (IOException e) {
      Throwables.propagate(e);
    }

    System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");

    mWebAppContext = new WebAppContext();
    mWebAppContext.setContextPath(AlluxioURI.SEPARATOR);
    mWebAppContext.setDescriptor(WebServer.class.getResource("/WEB-INF/web.xml").toString());
    mWebAppContext.setResourceBase("./src/main/webapp");
    mWebAppContext.setContextPath("/");
    mWebAppContext.setParentLoaderPriority(true);
    mServer.setHandler(mWebAppContext);
  }


  /**
   * @param handler to use
   */
  public void setHandler(AbstractHandler handler) {
    mServer.setHandler(handler);
  }

  /**
   * @return the underlying Jetty server
   */
  public Server getServer() {
    return mServer;
  }

  /**
   * Gets the actual bind hostname.
   *
   * @return the bind host
   */
  public String getBindHost() {
    String bindHost = mServerConnector.getHost();
    return bindHost == null ? "0.0.0.0" : bindHost;
  }

  /**
   * Gets the actual port that the web server is listening on.
   *
   * @return the local port
   */
  public int getLocalPort() {
    return mServerConnector.getLocalPort();
  }

  /**
   * Shuts down the web server.
   *
   * @throws Exception if the underlying jetty server throws an exception
   */
  public void stop() throws Exception {
    // close all connectors and release all binding ports
    for (Connector connector : mServer.getConnectors()) {
      connector.stop();
    }

    mServer.stop();
  }

  /**
   * Starts the web server.
   */
  public void start() {
    try {
      mServer.start();
      LOG.info("{} started @ {}", mServiceName, mAddress);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }
}
