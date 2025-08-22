package be.ddd.domain.entity.member;

import be.ddd.common.entity.BaseTimeEntity;
import be.ddd.domain.entity.member.intake.IntakeHistory;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    private String deviceToken; // FCM 디바이스 토큰

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @Embedded private MemberHealthMetric memberHealthMetric;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private NotificationSettings notificationSettings;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IntakeHistory> intakeHistories =
            new ArrayList<>(); // Initialize to avoid NullPointerException

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberBeverageLike> memberBeverageLikes = new ArrayList<>(); // Initialize

    @Column(name = "deleted_at")
    private LocalDate deletedAt;

    public void ofProfile(
            String nickname, LocalDate birthDay, MemberHealthMetric memberHealthMetric) {
        this.nickname = nickname;
        this.birthDay = birthDay;
        this.memberHealthMetric = memberHealthMetric;
    }

    public void updateDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void notificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }

    public Member(UUID fakeId, AuthProvider authProvider, String providerId) {
        this.fakeId = fakeId;
        this.authProvider = authProvider;
        this.providerId = providerId;
        this.memberHealthMetric = new MemberHealthMetric(null, 0, 0, null, null, null);
    }

    public void withdraw() {
        this.deletedAt = LocalDate.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
