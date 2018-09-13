package org.tron.common.runtime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TVMTestWithTimeResult {

  private Runtime runtime;
  private long duration;
  private byte[] contractAddress;

  public byte[] getContractAddress() {
    return contractAddress;
  }

  public TVMTestWithTimeResult setContractAddress(byte[] contractAddress) {
    this.contractAddress = contractAddress;
    return this;
  }

  public Runtime getRuntime() {
    return runtime;
  }

  public TVMTestWithTimeResult setRuntime(Runtime runtime) {
    this.runtime = runtime;
    return this;
  }

  public long getDuration() {
    return duration;
  }

  public TVMTestWithTimeResult setDuration(long duration) {
    this.duration = duration;
    return this;
  }

  public TVMTestWithTimeResult(Runtime runtime, long duration, byte[] contractAddress) {
    this.runtime = runtime;
    this.duration = duration;
    this.contractAddress = contractAddress;
  }

}
