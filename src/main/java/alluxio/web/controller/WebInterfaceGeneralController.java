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
public class WebInterfaceGeneralController {
  @RequestMapping("/*")
  public String root() {
    return "home/home";
  }

  @RequestMapping("/home")
  public String home() {
    return root();
  }
}
