package org.tron.common.runtime.vm.program;

import static java.lang.System.arraycopy;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.tron.common.crypto.Hash;
import org.tron.common.runtime.vm.DataWord;
import org.tron.core.capsule.StorageRowCapsule;
import org.tron.core.db.StorageRowStore;

@Slf4j
public class Storage {

  private byte[] addrHash;  // contract address
  private StorageRowStore store;
  private final Map<DataWord, StorageRowCapsule> rowCache = new HashMap<>();

  private static final int PREFIX_BYTES = 16;

  public Storage(byte[] address, StorageRowStore store) {
    addrHash = addrHash(address);
    this.store = store;
  }

  public DataWord getValue(DataWord key) {
    if (rowCache.containsKey(key)) {
      // logger.error("getValue: in rowCache, hit key");
      return rowCache.get(key).getValue();
    } else {
      // logger.error("getValue: in rowCache, not hit key");
      StorageRowCapsule row = store.get(compose(key.getData(), addrHash));
      if (row == null || row.getInstance() == null) {
        return null;
      }
      rowCache.put(key, row);
      return row.getValue();
    }
  }

  public void put(DataWord key, DataWord value) {
    if (rowCache.containsKey(key)) {
      // logger.error("put: in rowCache, hit key");
      rowCache.get(key).setValue(value);
    } else {
      // logger.error("put: in rowCache, not hit key");
      byte[] rowKey = compose(key.getData(), addrHash);
      StorageRowCapsule row = new StorageRowCapsule(rowKey, value.getData());
      rowCache.put(key, row);
    }
  }

  private static byte[] compose(byte[] key, byte[] addrHash) {
    byte[] result = new byte[key.length];
    arraycopy(addrHash, 0, result, 0, PREFIX_BYTES);
    arraycopy(key, PREFIX_BYTES, result, PREFIX_BYTES, PREFIX_BYTES);
    return result;
  }

  // 32 bytes
  private static byte[] addrHash(byte[] address) {
    return Hash.sha3(address);
  }

  public void commit() {
    rowCache.forEach((key, value) -> {
      if (value.isDirty()) {
        if (value.getValue().isZero()) {
          this.store.delete(value.getRowKey());
        } else {
          this.store.put(value.getRowKey(), value);
        }
      }
    });
  }
}
