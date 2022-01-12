package net.insprill.xenlib;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class Pair<K, V> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private K first;
    @Getter
    @Setter
    private V second;

}
