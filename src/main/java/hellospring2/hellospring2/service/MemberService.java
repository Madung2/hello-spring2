package hellospring2.hellospring2.service;

import hellospring2.hellospring2.domain.Member;
import hellospring2.hellospring2.repository.MemberRepository;
import hellospring2.hellospring2.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    public Long join(Member member) {
        //회원가입: 같은 이름 있는 중복 회원 X
//        Optional<Member> result = memberRepository.findByName(member.getName());
//        result.ifPresent(m -> {//값이 있다면
//            throw new IllegalStateException("이미 존재하는 회원입니다");
//        });
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m-> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
    //전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}

