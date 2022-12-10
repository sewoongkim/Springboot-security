package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.model.User;

@Controller // View를 리턴하겠다.
public class IndexController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// localhost:8080/
	// localhost:8080
	@GetMapping({ "", "/" })
	public String index() {
		// 머스테치 기본폴더: src/main/resources/
		// 뷰리졸버 설정: templates (perfix), mustache (suffix)
		return "index"; // src/main/resources/templates/index.mustache
	}

	@GetMapping("/user")
	public String user() {
		return "user";
	}

	// 스프링시큐리티 해당주소를 낚아 채버리네요!!
	// SecurityConfig  파일 생성 후 작동안함.
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}	

	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}	

	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");

		// 회원가입 잘됨. 비밀번호: 1234 ==> 시큐리티로 로그인 할수 없음. 이유는 패스워드가 암호화가 안되었음.
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword) ;
		userRepository.save(user);
		return "redirect:/loginForm";
	}	

}
