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
public class Trio<K, V, T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private K first;
    @Getter
    @Setter
    private V second;
    @Getter
    @Setter
    private T third;

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

    /**
     * @return True if third isn't null, false otherwise.
     */
    public boolean hasThird() {
        return third != null;
    }

}
