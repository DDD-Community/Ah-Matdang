package be.ddd.domain.entity.member;

import be.ddd.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID fakeId;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(unique = true)
    private String providerId;

    private String nickname;

    private String profileUrl;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    private MemberHealthMetric memberHealthMetric;

    public void ofProfile(
            String nickname, LocalDate birthDay, MemberHealthMetric memberHealthMetric) {
        this.nickname = nickname;
        this.birthDay = birthDay;
        this.memberHealthMetric = memberHealthMetric;
    }
}
