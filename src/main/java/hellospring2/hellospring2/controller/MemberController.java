package hellospring2.hellospring2.controller;

import hellospring2.hellospring2.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller //스프링에서 스프링빈이 관리됨, 스프링 컨테이너에 등록하고 하나의 등록된 객체를 사용할거임
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
