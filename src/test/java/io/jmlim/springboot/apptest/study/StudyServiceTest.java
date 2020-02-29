package io.jmlim.springboot.apptest.study;

import io.jmlim.springboot.apptest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 의존하고 있는 클래스에 대한 구현체는 없고 인터페이스만 있음.
 * Mocking 을 할수밖에 없는 구조.
 *  - 구현체가 준비가 안되어있거나 준비가 되어 있더라도 난 StudyService만 테스트 하고 싶다 할 때 사용.
 */
@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Test
    void createStudyService(@Mock MemberService memberService,
                            @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);
    }
}