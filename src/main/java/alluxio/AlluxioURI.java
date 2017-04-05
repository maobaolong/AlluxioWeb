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

package alluxio;


import javax.annotation.concurrent.ThreadSafe;
import java.io.Serializable;

@ThreadSafe
public final class AlluxioURI implements Comparable<AlluxioURI>, Serializable {
  private static final long serialVersionUID = -1207227692436086387L;
  public static final String SEPARATOR = "/";
  public static final String CUR_DIR = ".";
  public static final String WILDCARD = "*";

  public static final AlluxioURI EMPTY_URI = new AlluxioURI("");


  public AlluxioURI(String s) {
  }

  @Override
  public int compareTo(AlluxioURI o) {
    return 0;
  }
}
