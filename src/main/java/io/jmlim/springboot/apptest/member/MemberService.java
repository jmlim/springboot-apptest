package io.jmlim.springboot.apptest.member;

import io.jmlim.springboot.apptest.domain.Member;
import io.jmlim.springboot.apptest.domain.Study;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);

    void validate(Long memberId);

    void notify(Study newStudy);

    void notify(Member member);
}
