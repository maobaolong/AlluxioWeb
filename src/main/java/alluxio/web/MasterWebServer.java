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

package alluxio.web;

import alluxio.master.AlluxioMasterService;

import javax.annotation.concurrent.NotThreadSafe;
import java.net.InetSocketAddress;

/**
 * The Alluxio master web server.
 */
@NotThreadSafe
public final class MasterWebServer extends WebServer {

  public static final String ALLUXIO_MASTER_SERVLET_RESOURCE_KEY = "Alluxio Master";

  /**
   * Creates a new instance of {@link MasterWebServer}.
   *
   * @param serviceName the service name
   * @param address the service address
   * @param master the Alluxio master
   */
  public MasterWebServer(String serviceName, InetSocketAddress address,
                         final AlluxioMasterService master) {
    super(serviceName, address);
  }
}
