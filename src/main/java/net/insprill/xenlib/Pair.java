package net.insprill.xenlib;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
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

}
