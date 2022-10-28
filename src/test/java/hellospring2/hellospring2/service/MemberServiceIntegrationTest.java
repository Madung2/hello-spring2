package hellospring2.hellospring2.service;

import hellospring2.hellospring2.domain.Member;
import hellospring2.hellospring2.repository.MemberRepository;
import hellospring2.hellospring2.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


//스프링컨테이너에게 메모리멤버 리포지토리를 내놔~라고 해야함
@SpringBootTest
@Transactional//테스트를 실행할때 트랜젝션을 실행하고 다 끝나면 롤백 db 데이터 다 지워짐
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;


    @Test
    void 회원가입() {
        //given -> when -> then
        //given
        Member member = new Member();
        member.setName("name1");//테스트 전용 db를 따로 구축

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }
//    @Test//중복회원로직
//    public void 중복_회원_예외() {
//        //given
//        Member member1 = new Member();
//        member1.setName("name2");
//
//        Member member2 = new Member();
//        member2.setName("name2");
//
//        //when
//        memberService.join(member1);
//
//        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
//        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        //        try{
////            memberService.join(member2);
////            fail("예외발생필요");
////        } catch (IllegalStateException e) {
////            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
////        }
//
//        //then
//    }
//
//
////    @Test
////    void findMembers() {
////    }
////
////    @Test
////    void findOne() {
////    }
}