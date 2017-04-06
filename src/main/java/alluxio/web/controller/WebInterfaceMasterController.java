package alluxio.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebInterfaceMasterController {
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
