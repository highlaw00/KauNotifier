package highlaw00.kaunotifier.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


@Entity
@IdClass(SubscriptionId.class)
public class Subscription {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "source_id")
    public Source source;
}
