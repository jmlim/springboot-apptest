package io.jmlim.springboot.apptest;

import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

// 테스트 이름 전략 짜기
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @Test
    @DisplayName("스터디 만들기 \uD83D\uDE31")
    void create_new_study() {
        // TEST_ENV 환경변수가 local 인 경우에만 통과
        // - 주의 : 인텔리제이에서는 처음 실행 시 환경변수를 로드한걸 사용하므로 만약 환경변수가 변경되었다면 재시작을 해야 정상적으로 읽어온다.
        String testEnv = System.getenv("TEST_ENV");
        System.out.println(testEnv);
        // assumeTrue("LOCAL".equalsIgnoreCase(testEnv));

        assumingThat("LOCAL".equalsIgnoreCase(testEnv), () -> {
            System.out.println("local");
            Study actual = new Study(10);
            assertThat(actual.getLimit()).isGreaterThan(0);
        });

        assumingThat("jmlim".equalsIgnoreCase(testEnv), () -> {
            System.out.println("jmlim");
            Study actual = new Study(10);
            assertThat(actual.getLimit()).isGreaterThan(0);
        });
    }

    @Test
    void create1_new_study_again() {
        System.out.println("create1");
    }

    //반드시 스태틱 void 로 작성.
    // 모든 테스트가 실행된 이전에 딱 한번 실행됨.
    @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
    }

    //반드시 스태틱 void 로 작성.
    // 모든 테스트가 실행된 이후에 딱 한번 실행됨.
    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }

    // 각각 테스트 실행하기 이전에 실행
    @BeforeEach
    void beforeEach() {
        System.out.println("beforeEach");
    }

    // 각각 테스트 실행 후 실행
    @AfterEach
    void afterEach() {
        System.out.println("afterEach");
    }
}