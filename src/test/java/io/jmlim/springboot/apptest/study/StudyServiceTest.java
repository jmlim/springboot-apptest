package io.jmlim.springboot.apptest.study;

import io.jmlim.springboot.apptest.domain.Member;
import io.jmlim.springboot.apptest.domain.Study;
import io.jmlim.springboot.apptest.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * 의존하고 있는 클래스에 대한 구현체는 없고 인터페이스만 있음.
 * Mocking 을 할수밖에 없는 구조.
 * - 구현체가 준비가 안되어있거나 준비가 되어 있더라도 난 StudyService만 테스트 하고 싶다 할 때 사용.
 */
@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    MemberService memberService;
    @Mock
    StudyRepository studyRepository;

    @Test
    void createStudyService() {

        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("hackerljm@gmail.com");

        Study study = new Study(10, "테스트");
        // memberService 객체에 findById 메소드를 1L 값으로 호출하면 Optional.of(member) 객체를 리턴하도록 Stubbing
        // when(memberService.findById(1L)).thenReturn(Optional.of(member));
        // studyRepository 객체에 save 메소드를 study 객체로 호출하면 study 객체 그대로 리턴하도록 Stubbing
        // when(studyRepository.save(study)).thenReturn(study);

        // 위 코드를 bdd 스타일로 작성.
        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        // Whrn
        studyService.createNewStudy(1L, study);

        // Then
        assertEquals(member, study.getOwner());
        // verify(memberService, times(1)).notify(study);
        // 위 코드를 bdd 스타일로 작성.
        then(memberService).should(times(1)).notify(study);
        // memberService.notify(스터디) 실행 이후에 memberService의 notify(멤버); 를 한번 더 실행하므로 에러.
        // 아래 코드는 스터디 이후 더이항 실행되지 않기를 원하고 짠 테스트
        // verifyNoMoreInteractions(memberService);
        // 위코드를 bdd 스타일로 작성.
        then(memberService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바, 테스트");
        // TODO studyRepository Mock 객체의 save 메소드를 호출 시 study를 리턴하도록 만들기.
        given(studyRepository.save(study)).willReturn(study);

        // When
        studyService.openStudy(study);

        // Then
        // TODO study의 status가 OPENED로 변경됐는지 확인
        // TODO study의 openedDataTime이 null이 아닌지 확인
        assertAll(
                () -> assertEquals(study.getStatus(), StudyStatus.OPENED),
                () -> assertNotNull(study.getOpenedDateTime())
        );
        // TODO memberService의 notify(study)가 호출 됐는지 확인.
        then(memberService).should(times(1)).notify(study);
    }
}