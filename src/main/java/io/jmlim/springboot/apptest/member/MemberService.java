package io.jmlim.springboot.apptest.member;

import io.jmlim.springboot.apptest.domain.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);

    void validate(Long memberId);
}
