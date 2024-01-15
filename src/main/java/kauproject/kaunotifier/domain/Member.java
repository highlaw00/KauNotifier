package kauproject.kaunotifier.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String email;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;
    @UpdateTimestamp
    private LocalDateTime updateTimestamp;

    @OneToMany(mappedBy = "member")
    private List<Subscription> subscriptions = new ArrayList<>();

    // == 생성 메서드 == //
    protected Member() {}

    public static Member createMember(String name, String email) {
        Member member = new Member();
        member.name = name;
        member.email = email;

        return member;
    }

    // == 연관관계 메서드 == //

    public void addSubscription(Subscription subscription) {
        this.subscriptions.add(subscription);
    }
}
