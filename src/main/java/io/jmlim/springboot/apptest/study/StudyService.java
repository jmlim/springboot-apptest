package io.jmlim.springboot.apptest.study;

import io.jmlim.springboot.apptest.domain.Member;
import io.jmlim.springboot.apptest.domain.Study;
import io.jmlim.springboot.apptest.member.MemberService;

public class StudyService {
    private final MemberService memberService;
    private final StudyRepository repository;

    public StudyService(MemberService memberService, StudyRepository repository) {
        assert memberService != null;
        assert repository != null;

        this.memberService = memberService;
        this.repository = repository;
    }

    public Study createNewStudy(Long memberId, Study study) {
        Member member = memberService.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("Member doesn't exist for id: '" + memberId + "'"));
        study.setOwner(member);
        Study newStudy = repository.save(study);
        // member service 한테 새로운 스터다가 나왔다고 알려주는것을 가정함.
        memberService.notify(newStudy);
        memberService.notify(member);
        return newStudy;
    }
}
