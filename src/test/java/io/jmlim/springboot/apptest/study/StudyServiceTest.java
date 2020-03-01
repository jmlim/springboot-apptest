package io.jmlim.springboot.apptest.study;

import io.jmlim.springboot.apptest.domain.Member;
import io.jmlim.springboot.apptest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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

        Member member = new Member();
        member.setId(1L);
        member.setEmail("hackerljm@gmail.com");

        // any()로 할경우 어떤 값을 던지더라도 위에서 만든 member 객체가 리턴
        when(memberService.findById(any())).thenReturn(Optional.of(member));

        assertEquals("hackerljm@gmail.com", memberService.findById(1L).get().getEmail());
        assertEquals("hackerljm@gmail.com", memberService.findById(2L).get().getEmail());
        assertEquals("hackerljm@gmail.com", memberService.findById(3L).get().getEmail());

        // 1이라는 값을 던질경우 Throw 리턴
        // when(memberService.findById(1L)).thenThrow(new RuntimeException());

        // member메소드 중 validate 메소드가 1L 이라는 값을 던질경우 IllegalArgumentException 예외를 던짐.
        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1L);
        });

        memberService.validate(2L);

    }
}