package be.ddd.application.member;

import be.ddd.application.member.dto.res.RecommendedSugar;
import be.ddd.domain.entity.member.Gender;
import be.ddd.domain.entity.member.Member;
import be.ddd.domain.entity.member.MemberHealthMetric;
import org.springframework.stereotype.Service;

@Service
public class SugarRecommendationService {

    public RecommendedSugar calculate(Member member) {
        MemberHealthMetric metric = member.getMemberHealthMetric();
        if (metric == null
                || metric.getAge() == null
                || metric.getWeightKg() == null
                || metric.getGender() == null
                || metric.getActivityRange() == null) {
            return new RecommendedSugar(0, 0);
        }

        double bmr = calculateBMR(metric.getAge(), metric.getWeightKg(), metric.getGender());
        double tee = bmr * metric.getActivityRange().getPal();

        double dailySugarKcal = tee * 0.10;
        double dailySugarKcalIdeal = tee * 0.05;

        double sugarMaxGDouble = dailySugarKcal / 4.0;
        double sugarIdealGDouble = dailySugarKcalIdeal / 4.0;

        int sugarMaxG = (int) Math.round(sugarMaxGDouble);
        int sugarIdealG = (int) Math.round(sugarIdealGDouble);

        return new RecommendedSugar(sugarMaxG, sugarIdealG);
    }

    private double calculateBMR(int age, Integer weight, Gender gender) {
        double w = (double) weight;
        if (age >= 18 && age <= 30) {
            return gender == Gender.MALE ? (15.3 * w + 679) : (14.7 * w + 496);
        } else if (age > 30 && age <= 60) {
            return gender == Gender.MALE ? (11.6 * w + 879) : (8.7 * w + 829);
        } else if (age > 60) {
            return gender == Gender.MALE ? (13.5 * w + 487) : (10.5 * w + 596);
        }
        return 0;
    }
}
