package highlaw00.kaunotifier.entity;

import java.io.Serializable;
import java.util.Objects;

public class SubscriptionId implements Serializable {
    private Long user;
    private Long source;

    public SubscriptionId() {
    }

    public SubscriptionId(Long user, Long source) {
        this.user = user;
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionId that = (SubscriptionId) o;
        return Objects.equals(user, that.user) && Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, source);
    }
}
