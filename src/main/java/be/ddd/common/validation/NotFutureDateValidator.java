package be.ddd.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NotFutureDateValidator implements ConstraintValidator<NotFutureDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext context) {
        if (dateTime == null) {
            return true; // null 값은 @NotNull 등으로 처리
        }
        return !dateTime.toLocalDate().isAfter(LocalDate.now());
    }
}
