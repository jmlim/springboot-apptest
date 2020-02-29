package io.jmlim.springboot.apptest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import static org.assertj.core.api.Assertions.assertThat;

// 테스트 이름 전략 짜기
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {
    @FastTest
    @DisplayName("스터디 만들기 \uD83D\uDE31 fast")
    void create_new_study() {
        Study actual = new Study(100);
        assertThat(actual.getLimit()).isGreaterThan(0);
        System.out.println("create_new_study");
    }

    @SlowTest
    @DisplayName("스터디 만들기 \uD83D\uDE31 slow")
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