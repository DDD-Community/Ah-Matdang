package be.ddd.infra.exception;

import be.ddd.domain.exception.*;
import java.util.Set;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 생겼습니다. 관리자에게 문의하세요.", Set.of()),
    INVALID_INPUT_VALUE(
            HttpStatus.BAD_REQUEST,
            "입력 값이 올바르지 않습니다.",
            Set.of(
                    MethodArgumentNotValidException.class,
                    ConstraintViolationException.class,
                    InvalidInputException.class)),
    FUTURE_DATE_NOT_ALLOWED(
            HttpStatus.BAD_REQUEST,
            "미래 날짜는 허용되지 않습니다.",
            Set.of(FutureDateNotAllowedException.class)),
    METHOD_NOT_ALLOWED(
            HttpStatus.METHOD_NOT_ALLOWED,
            "지원하지 않는 HTTP 메서드입니다.",
            Set.of(HttpRequestMethodNotSupportedException.class)),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.", Set.of()),

    // beverage
    CAFE_BEVERAGE_NOT_FOUND(
            HttpStatus.NOT_FOUND, "카페 음료를 찾을 수 없습니다.", Set.of(CafeBeverageNotFoundException.class)),
    ALREADY_LIKE_BEVERAGE_(
            HttpStatus.BAD_REQUEST, "이미 찜 버튼을 눌렀습니다.", Set.of(AlreadyLikeBeverageException.class)),

    // member
    MEMBER_NOT_FOUND(
            HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다.", Set.of(MemberNotFoundException.class)),
    MEMBER_BEVERAGE_LIKE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "회원이 누른 찜 정보 없음",
            Set.of(MemberBeverageLikeNotFoundException.class)),

    // like
    LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "찜 정보를 찾을 수 없습니다.", Set.of(LikeNotFoundException.class));

    private final HttpStatusCode status;
    private final String code;
    private final String message;
    private final Set<Class<? extends Exception>> exceptions;

    ErrorCode(
            HttpStatusCode status,
            String code,
            String message,
            Set<Class<? extends Exception>> exceptions) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.exceptions = exceptions;
    }

    ErrorCode(HttpStatusCode status, String message, Set<Class<? extends Exception>> exceptions) {
        this.status = status;
        this.code = String.valueOf(status.value());
        this.message = message;
        this.exceptions = exceptions;
    }
}
