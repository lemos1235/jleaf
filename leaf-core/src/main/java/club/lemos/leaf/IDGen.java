package club.lemos.leaf;

import club.lemos.leaf.common.Result;

public interface IDGen {
    Result get(String key);
    boolean init();
}
