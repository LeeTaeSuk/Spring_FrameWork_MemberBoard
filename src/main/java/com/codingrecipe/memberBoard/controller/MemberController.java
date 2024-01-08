package com.codingrecipe.memberBoard.controller;


import com.codingrecipe.memberBoard.dto.MemberDTO;
import com.codingrecipe.memberBoard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/memberSave")
    public String saveForm() {
        return "memberSave";
    }
    @PostMapping("/memberSave")
    public String save(@ModelAttribute MemberDTO memberDTO){
        int saveResult = memberService.save(memberDTO);
        if(saveResult > 0){
            return "memberLogin";
        }
        else{
            return "mamberSave";
        }
    }
    @GetMapping("/memberLogin")
    public String loginForm(){
        return "memberLogin";
    }
    @PostMapping("/memberLogin")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        boolean loginResult = memberService.login(memberDTO);
        if(loginResult){
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            return "main";
        }
        else{
            return "memberLogin";
        }
    }

    @GetMapping("/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "memberList";
    }
    //@PostMapping

    @GetMapping
    public String findById(@RequestParam("id") Long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member",memberDTO);
        return "memberDetail";
    }
    @GetMapping("/memberDelete")
    public String delete(@RequestParam("id") Long id){
        memberService.delete(id);
        return "redirect:/member/";
    }
    @GetMapping("/memberUpdate")
    public String updateForm(HttpSession session, Model model){
        //세션에 저장된 나의 이메일 가져오기
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
        model.addAttribute("member", memberDTO);
        return "memberUpdate";
    }
    @PostMapping("/memberUpdate")
    public String update(@ModelAttribute MemberDTO memberDTO){
        boolean result = memberService.update(memberDTO);
        if(result){
            return "redirect:/member?id=" + memberDTO.getId();
        }
        else{
            return "index";
        }
    }
    @RequestMapping("/memberLogout")
    public ModelAndView logout (HttpSession session){
        session.invalidate();
        ModelAndView mv = new ModelAndView("redirect:/");
        return mv;
    }
}