package io.jmlim.springboot.apptest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class StudyTest {

    @Test
    void create() {
        Study study = new Study();
        assertNotNull(study);
        System.out.println("create");
    }

    // 테스트를 실행하지 않기 위함.
    @Test
    @Disabled
    void create1() {
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