package io.jmlim.springboot.apptest.study;

import io.jmlim.springboot.apptest.domain.Member;
import io.jmlim.springboot.apptest.domain.Study;
import io.jmlim.springboot.apptest.member.MemberService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
class StudyServiceTest {

    private static Logger LOGGER = LoggerFactory.getLogger(StudyServiceTest.class);

    @Mock
    MemberService memberService;
    @Autowired
    StudyRepository studyRepository;

    @Container
    static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
            .withExposedPorts(5432) // 내부포트
            .withEnv("POSTGRES_DB", "studytest")// 환경변수 설정
            .waitingFor(Wait.forListeningPort());
    //.waitingFor(Wait.forHttp("/hello"))
    //.waitingFor(Wait.forLogMessage())

    @BeforeAll
    static void beforeAll() {
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOGGER);
        postgreSQLContainer.followOutput(logConsumer);
    }

    /*    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName("studytest");*/

    // 테스트 컨테이너 안의 데이터가 쌓이는 것을 방지
    @BeforeEach
    void beforeEach() {
        System.out.println("=================================");
        System.out.println(postgreSQLContainer.getMappedPort(5432));
        // System.out.println(postgreSQLContainer.getLogs()); //컨테이너 모든 로그 보기.

        studyRepository.deleteAll();
    }


    // 모든 테스트마다 컨테이너를 새로 만든다.
    //  PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer();
    // 스태틱으로 할 경우 모든 테스트마다 컨테이너를 새로 만들지 않고 공유해서 사용 가능하다.
/*    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName("studytest");*/
/*    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }*/


    @Test
    void createNewStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("hackerljm@gmail.com");
        Study study = new Study(10, "테스트");

        given(memberService.findById(1L)).willReturn(Optional.of(member));

        // When
        studyService.createNewStudy(1L, study);

        // Then
        assertEquals(1L, study.getOwnerId());
        then(memberService).should(times(1)).notify(study);
        then(memberService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바, 테스트");
        // When
        studyService.openStudy(study);

        // Then
        assertAll(
                () -> assertEquals(study.getStatus(), StudyStatus.OPENED),
                () -> assertNotNull(study.getOpenedDateTime())
        );
        then(memberService).should().notify(study);
    }
}