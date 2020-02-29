package io.jmlim.springboot.apptest;

import org.junit.jupiter.api.*;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

// 테스트 이름 전략 짜기
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @Test
    @DisplayName("스터디 만들기 \uD83D\uDE31")
    void create_new_study() {
        Study study = new Study(-10);
        assertNotNull(study);
        //  assertEquals(StudyStatus.DRAFT, study.getStatus(), "스터디를 처음 만들면 상태값이 DRAFT여야 한다.");
        //  assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값이 DRAFT여야 한다.");
        // 풀어보기
        assertEquals(StudyStatus.DRAFT, study.getStatus(), new Supplier<String>() {
            /**
             * Gets a result.
             *
             * @return a result
             */
            @Override
            public String get() {
                return "스터디를 처음 만들면 DRAFT 상태여야 한다.";
            }
        });
//        assertTrue(1 < 2);
        assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야한다.");
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