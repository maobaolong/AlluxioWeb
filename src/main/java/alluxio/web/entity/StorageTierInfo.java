package alluxio.web.entity;

import alluxio.util.FormatUtils;

public final class StorageTierInfo {
  private final String mStorageTierAlias;
  private final long mCapacityBytes;
  private final long mUsedBytes;
  private final int mUsedPercent;
  private final long mFreeBytes;
  private final int mFreePercent;

  public StorageTierInfo(String storageTierAlias, long capacityBytes, long usedBytes) {
    mStorageTierAlias = storageTierAlias;
    mCapacityBytes = capacityBytes;
    mUsedBytes = usedBytes;
    mFreeBytes = mCapacityBytes - mUsedBytes;
    mUsedPercent = (int) (100L * mUsedBytes / mCapacityBytes);
    mFreePercent = 100 - mUsedPercent;
  }

  /**
   * @return the storage alias
   */
  public String getStorageTierAlias() {
    return mStorageTierAlias;
  }

  /**
   * @return the capacity
   */
  public String getCapacity() {
    return FormatUtils.getSizeFromBytes(mCapacityBytes);
  }

  /**
   * @return the free capacity
   */
  public String getFreeCapacity() {
    return FormatUtils.getSizeFromBytes(mFreeBytes);
  }

  /**
   * @return the free space as a percentage
   */
  public int getFreeSpacePercent() {
    return mFreePercent;
  }

  /**
   * @return the used capacity
   */
  public String getUsedCapacity() {
    return FormatUtils.getSizeFromBytes(mUsedBytes);
  }

  /**
   * @return the used space as a percentage
   */
  public int getUsedSpacePercent() {
    return mUsedPercent;
  }
}