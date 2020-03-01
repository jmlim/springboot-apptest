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
 * - 구현체가 준비가 안되어있거나 준비가 되어 있더라도 난 StudyService만 테스트 하고 싶다 할 때 사용.
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

        when(memberService.findById(any()))
                .thenReturn(Optional.of(member)) // 첫번째엔 정상리턴
                .thenThrow(new RuntimeException()) // 두번째엔 런타임예외
                .thenReturn(Optional.empty()); //세번째 호출땐 비어있는게 나오도록 stubbing

        // 위 stubbing 에 대해 전부 대응되도록 테스트
        Optional<Member> byId = memberService.findById(1L);
        assertEquals("hackerljm@gmail.com", byId.get().getEmail());

        assertThrows(RuntimeException.class, () -> {
            memberService.findById(99L);
        });

        assertEquals(Optional.empty(), memberService.findById(1L));
    }
}