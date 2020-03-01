package io.jmlim.springboot.apptest.study;

import io.jmlim.springboot.apptest.domain.Member;
import io.jmlim.springboot.apptest.domain.Study;
import io.jmlim.springboot.apptest.member.MemberService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
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

    @Mock
    MemberService memberService;
    @Autowired
    StudyRepository studyRepository;

    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName("studytest");

    // 테스트 컨테이너 안의 데이터가 쌓이는 것을 방지
    @BeforeEach
    void beforeEach() {
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