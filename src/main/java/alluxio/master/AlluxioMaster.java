package alluxio.master;

import alluxio.Constants;
import alluxio.ServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * alluxio.master <br>
 * <p>
 * Copyright: Copyright (c) 17-4-4 下午8:31
 * <p>
 * Company: 京东
 * <p>
 *
 * @author maobaolong@jd.com
 * @version 1.0.0
 */
public class AlluxioMaster {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  /**
   * Starts the Alluxio master.
   *
   * @param args command line arguments, should be empty
   */
  public static void main(String[] args) {
    if (args.length != 0) {
      LOG.info("java -cp {} {}", "target/alluxio-" + "1.5.0-release" + "-jar-with-dependencies.jar",
          AlluxioMaster.class.getCanonicalName());
      System.exit(-1);
    }

    AlluxioMasterService master = AlluxioMasterService.Factory.create();
    ServerUtils.run(master, "Alluxio master");
  }

  private AlluxioMaster() {} // prevent instantiation
}
