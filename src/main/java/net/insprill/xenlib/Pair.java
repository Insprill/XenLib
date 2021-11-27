package net.insprill.xenlib;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Pair<K, V> {

    @Getter
    @Setter
    private K first;
    @Getter
    @Setter
    private V second;

}
