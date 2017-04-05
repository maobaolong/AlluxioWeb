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

package alluxio.util.io;

import alluxio.AlluxioURI;
import alluxio.exception.ExceptionMessage;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities related to both Alluxio paths like {@link AlluxioURI} and local file paths.
 */
@ThreadSafe
public final class PathUtils {
  private static final String TEMPORARY_SUFFIX_FORMAT = ".alluxio.0x%016X.tmp";
  private static final int TEMPORARY_SUFFIX_LENGTH =
      String.format(TEMPORARY_SUFFIX_FORMAT, 0).length();

  /**
   * Joins each element in paths in order, separated by {@link AlluxioURI#SEPARATOR}.
   * <p>
   * For example,
   *
   * <pre>
   * {@code
   * concatPath("/myroot/", "dir", 1L, "filename").equals("/myroot/dir/1/filename");
   * concatPath("alluxio://myroot", "dir", "filename").equals("alluxio://myroot/dir/filename");
   * concatPath("myroot/", "/dir/", "filename").equals("myroot/dir/filename");
   * concatPath("/", "dir", "filename").equals("/dir/filename");
   * }
   * </pre>
   *
   * Note that empty element in base or paths is ignored.
   *
   * @param base base path
   * @param paths paths to concatenate
   * @return joined path
   * @throws IllegalArgumentException if base or paths is null
   */
  public static String concatPath(Object base, Object... paths) throws IllegalArgumentException {
    Preconditions.checkArgument(base != null, "Failed to concatPath: base is null");
    Preconditions.checkArgument(paths != null, "Failed to concatPath: a null set of paths");
    List<String> trimmedPathList = new ArrayList<>();
    String trimmedBase =
        CharMatcher.is(AlluxioURI.SEPARATOR.charAt(0)).trimTrailingFrom(base.toString().trim());
    trimmedPathList.add(trimmedBase);
    for (Object path : paths) {
      if (path == null) {
        continue;
      }
      String trimmedPath =
          CharMatcher.is(AlluxioURI.SEPARATOR.charAt(0)).trimFrom(path.toString().trim());
      if (!trimmedPath.isEmpty()) {
        trimmedPathList.add(trimmedPath);
      }
    }
    if (trimmedPathList.size() == 1 && trimmedBase.isEmpty()) {
      // base must be "[/]+"
      return AlluxioURI.SEPARATOR;
    }
    return Joiner.on(AlluxioURI.SEPARATOR).join(trimmedPathList);

  }

  private PathUtils() {} // prevent instantiation
}
