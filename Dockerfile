# --- Build Stage ---
# 1. Gradle 빌드 환경 설정
FROM gradle:8.5.0-jdk21 AS build
WORKDIR /app

# 2. Gradle Wrapper 파일 복사
COPY gradlew ./
COPY gradle ./gradle

# 3. 빌드 설정 파일만 먼저 복사 (의존성 캐싱을 위함)
COPY build.gradle settings.gradle /app/

# 4. 의존성 다운로드 (이 단계가 캐시됨)
# 소스코드 변경 없이 build.gradle이 그대로라면, 이 레이어를 재사용하여 시간 단축
RUN ./gradlew dependencies

# 5. 소스 코드 복사
COPY src /app/src

# 6. 애플리케이션 빌드 (실행 권한 부여 포함)
RUN chmod +x ./gradlew && ./gradlew bootJar

# --- Package Stage ---
# 7. 최종 이미지를 위한 JRE 환경 설정
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 8. Build Stage에서 빌드된 JAR 파일만 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 9. 애플리케이션 포트 노출
EXPOSE 8080

# 10. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]