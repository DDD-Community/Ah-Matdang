package be.ddd.domain.entity.member;

import be.ddd.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification_settings")
@Log4j2
public class NotificationSettings extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_setting_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Column(name = "is_enabled", nullable = false)
    @ColumnDefault("true")
    private boolean isEnabled = true;

    @Column(name = "reminders_enabled", nullable = false)
    @ColumnDefault("true")
    private boolean remindersEnabled = true;

    @Column(name = "reminder_time", nullable = false)
    private LocalTime reminderTime = LocalTime.of(15, 0);

    @Column(name = "risk_warnings_enabled", nullable = false)
    @ColumnDefault("true")
    private boolean riskWarningsEnabled = true;

    @Column(name = "news_updates_enabled", nullable = false)
    @ColumnDefault("true")
    private boolean newsUpdatesEnabled = true;

    public NotificationSettings(Member member) {
        this.member = member;
    }

    public void updateSettings(
            Boolean isEnabled,
            Boolean remindersEnabled,
            LocalTime reminderTime,
            Boolean riskWarningsEnabled,
            Boolean newsUpdatesEnabled) {
        log.info("진짜중요! isEnabled:{}", isEnabled);
        if (isEnabled != null) {
            this.isEnabled = isEnabled;
            if (!this.isEnabled) {
                log.info("도ㅒㅆ냐??????");
                this.remindersEnabled = false;
                this.riskWarningsEnabled = false;
                this.newsUpdatesEnabled = false;
            }
        }
        if (remindersEnabled != null) {
            this.remindersEnabled = remindersEnabled;
        }
        if (reminderTime != null) {
            this.reminderTime = reminderTime;
        }
        if (riskWarningsEnabled != null) {
            this.riskWarningsEnabled = riskWarningsEnabled;
        }
        if (newsUpdatesEnabled != null) {
            this.newsUpdatesEnabled = newsUpdatesEnabled;
        }
    }
}
