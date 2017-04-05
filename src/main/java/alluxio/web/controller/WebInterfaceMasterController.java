package alluxio.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * alluxio.web.controller <br>
 * <p>
 * Copyright: Copyright (c) 17-4-5 下午2:12
 * <p>
 * Company: 京东
 * <p>
 *
 * @author maobaolong@jd.com
 * @version 1.0.0
 */
@Controller
public class WebInterfaceMasterController {
/*  @RequestMapping("*//*")
  public String root() {
    return "general/general";
  }*/
  @RequestMapping("/home")
  public String home() {
    return "general/general";
  }

  @RequestMapping("/browse")
  public String browse() {
    return "browse/browse";
  }

  @RequestMapping("/configuration")
  public String configuration() {
    return "configuration/configuration";
  }

  @RequestMapping("/memory")
  public String memory() {
    return "memory/memory";
  }

  @RequestMapping("/workers")
  public String workers() {
    return "workers/workers";
  }

  @RequestMapping("/browseLogs")
  public String browseLogs() {
    return "browseLogs/browseLogs";
  }

  @RequestMapping("/metricsui")
  public String metricsui() {
    return "metricsui/metricsui";
  }
}
