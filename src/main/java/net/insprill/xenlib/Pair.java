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

    /**
     * @return True if first isn't null, false otherwise.
     */
    public boolean hasFirst() {
        return first != null;
    }

    /**
     * @return True if second isn't null, false otherwise.
     */
    public boolean hasSecond() {
        return second != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair))
            return false;
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        return getFirst().equals(pair.getFirst()) && getSecond().equals(pair.getSecond());
    }

}
