package alluxio.web.controller;

import alluxio.Configuration;
import alluxio.PropertyKey;
import alluxio.PropertyKeyFormat;
import alluxio.util.FormatUtils;
import alluxio.util.network.HttpUtils;
import alluxio.web.WebUtils;
import alluxio.web.entity.StorageTierInfo;
import alluxio.wire.AlluxioMasterInfo;
import alluxio.wire.Capacity;
import alluxio.wire.StartupConsistencyCheck;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebInterfaceMasterGeneralController {
  private final static String REST_PREFIX = "http://localhost:19999/api/v1/master/";

  @RequestMapping("/*")
  public String root(Model model, HttpServletRequest request) {
    try {
      populateValues(request);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "general/general";
  }

  @RequestMapping("/home")
  public String home(Model model, HttpServletRequest request) {
    return root(model, request);
  }

  /**
   * Populates key, value pairs for UI display.
   *
   * @param request The {@link HttpServletRequest} object
   * @throws IOException if an I/O error occurs
   */
  private void populateValues(HttpServletRequest request) throws IOException {

    String infoJson = HttpUtils.sendGet(REST_PREFIX + "info");
    ObjectMapper mapper = new ObjectMapper();
    AlluxioMasterInfo masterInfo = mapper.readValue(infoJson, AlluxioMasterInfo.class);
    StartupConsistencyCheck check = masterInfo.getStartupConsistencyCheck();
    Capacity capacity = masterInfo.getCapacity();
    Capacity ufsCapacity = masterInfo.getUfsCapacity();
    String version = masterInfo.getVersion();
    Map<String, String>  configMap = masterInfo.getConfiguration();
    Map<String, Capacity> tierCapacityMap = masterInfo.getTierCapacity();

    long startTimeMs = masterInfo.getStartTimeMs();
    String masterNodeAddress = masterInfo.getRpcAddress();
    int workerCount = masterInfo.getWorkers().size();
    long capacityBytes = capacity.getTotal();
    long usedCapacityBytes = capacity.getUsed();
    long freeCapacityBytes = capacityBytes - usedCapacityBytes;
    long ufsCapacityBytes = ufsCapacity.getTotal();
    long ufsUsedCapacityBytes = ufsCapacity.getUsed();
    long ufsFreeCapacityBytes = ufsCapacityBytes - ufsUsedCapacityBytes;

    request.setAttribute("debug", Configuration.getBoolean(PropertyKey.DEBUG));
    request.setAttribute("masterNodeAddress", masterNodeAddress);

    request.setAttribute("uptime",
        WebUtils.convertMsToClockTime(System.currentTimeMillis() - startTimeMs));

    request.setAttribute("startTime", WebUtils.convertMsToDate(startTimeMs));

    request.setAttribute("version", version);

    request.setAttribute("liveWorkerNodes", workerCount);

    request.setAttribute("capacity",
        FormatUtils.getSizeFromBytes(capacityBytes));

    request.setAttribute("usedCapacity",
        FormatUtils.getSizeFromBytes(usedCapacityBytes));

    request.setAttribute("freeCapacity", FormatUtils.getSizeFromBytes(freeCapacityBytes));
    
    request.setAttribute("consistencyCheckStatus", check.getStatus());
    if ("complete".equalsIgnoreCase(check.getStatus())) {
      request.setAttribute("inconsistentPaths", check.getInconsistentUris().size());
      request.setAttribute("inconsistentPathItems", check.getInconsistentUris());
    } else {
      request.setAttribute("inconsistentPaths", 0);
    }

    long sizeBytes = ufsCapacityBytes;
    if (sizeBytes >= 0) {
      request.setAttribute("diskCapacity", FormatUtils.getSizeFromBytes(sizeBytes));
    } else {
      request.setAttribute("diskCapacity", "UNKNOWN");
    }

    sizeBytes = ufsUsedCapacityBytes;
    if (sizeBytes >= 0) {
      request.setAttribute("diskUsedCapacity", FormatUtils.getSizeFromBytes(sizeBytes));
    } else {
      request.setAttribute("diskUsedCapacity", "UNKNOWN");
    }

    sizeBytes = ufsFreeCapacityBytes;
    if (sizeBytes >= 0) {
      request.setAttribute("diskFreeCapacity", FormatUtils.getSizeFromBytes(sizeBytes));
    } else {
      request.setAttribute("diskFreeCapacity", "UNKNOWN");
    }

    StorageTierInfo[] infos = generateOrderedStorageTierInfo(configMap, tierCapacityMap);
    request.setAttribute("storageTierInfos", infos);
  }

  private StorageTierInfo[] generateOrderedStorageTierInfo(Map<String, String> configMap,
                                                           Map<String, Capacity> tierCapacityMap) {
    int levels = Integer.parseInt(configMap.get(PropertyKey.WORKER_TIERED_STORE_LEVELS.toString()));
    List<StorageTierInfo> infos = new ArrayList<>();
    for (int i = 0; i < levels; i++){
      String tierAlias =
          configMap.get(PropertyKeyFormat.WORKER_TIERED_STORE_LEVEL_ALIAS_FORMAT.format(i).toString());
      Capacity tierCapacity = tierCapacityMap.get(tierAlias);
      StorageTierInfo info =
          new StorageTierInfo(tierAlias, tierCapacity.getTotal(), tierCapacity.getUsed());
      infos.add(info);
    }
    return infos.toArray(new StorageTierInfo[infos.size()]);
  }
}
