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

package alluxio.master;

import alluxio.Constants;
import alluxio.web.MasterWebServer;
import alluxio.web.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import java.net.InetSocketAddress;

/**
 * This class encapsulates the different master services that are configured to run.
 */
@NotThreadSafe
public class DefaultAlluxioMaster implements AlluxioMasterService {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  /** The web ui server. */
  private WebServer mWebServer = null;

  /** is true if the master is serving the RPC server. */
  private boolean mIsServing = false;

  /** The start time for when the master started serving the RPC server. */
  private long mStartTimeMs = -1;

  protected DefaultAlluxioMaster() {

  }

  
  @Override
  public long getStartTimeMs() {
    return mStartTimeMs;
  }

  @Override
  public long getUptimeMs() {
    return System.currentTimeMillis() - mStartTimeMs;
  }

  @Override
  public InetSocketAddress getWebAddress() {
    if (mWebServer != null) {
      return new InetSocketAddress(mWebServer.getBindHost(), mWebServer.getLocalPort());
    }
    return null;
  }

  @Override
  public boolean isServing() {
    return mIsServing;
  }

  @Override
  public void waitForReady() {
    
  }

  @Override
  public void start() throws Exception {
    startServing();
  }

  @Override
  public void stop() throws Exception {
    if (mIsServing) {
      LOG.info("Stopping RPC server on Alluxio master ");
      stopServing();
      mIsServing = false;
    } else {
      LOG.info("Stopping Alluxio master ");
    }
  }

  private void startServing() {
    startServing("", "");
  }

  protected void startServing(String startMessage, String stopMessage) {
    startServingWebServer();
  }

  protected void startServingWebServer() {
    mWebServer = new MasterWebServer("Alluxio Master Web service",
        new InetSocketAddress("0.0.0.0", 49999), this);
    // start web ui
    mWebServer.start();
  }

  protected void stopServing() throws Exception {
    if (mWebServer != null) {
      mWebServer.stop();
      mWebServer = null;
    }
    mIsServing = false;
  }
}
