package be.ddd.domain.entity.member.intake;

import be.ddd.common.entity.BaseTimeEntity;
import be.ddd.domain.entity.crawling.CafeBeverage;
import be.ddd.domain.entity.member.Member;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "intake_history", indexes = @Index(columnList = "member_id,intake_time"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class IntakeHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intake_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "intake_time", nullable = false)
    private LocalDateTime intakeTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beverage_id", nullable = false)
    private CafeBeverage cafeBeverage;
}
