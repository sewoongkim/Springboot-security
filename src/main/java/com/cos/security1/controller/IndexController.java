package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;

@Controller // View를 리턴하겠다.
public class IndexController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(
			Authentication authentication, 
	           //	@AuthenticationPrincipal UserDetails userDetails) { //DI(의존 주입)
		     @AuthenticationPrincipal PrincipalDetails userDetails) { //DI(의존 주입)
		System.out.println("/test/login ============");
		PrincipalDetails principalDetails = (PrincipalDetails)  authentication.getPrincipal();
		System.out.println("authentication : " + principalDetails.getUser());
		System.out.println("userDetails : " + userDetails.getUser());
		return "세션 정보 확인하기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(
			Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) { //DI(의존 주입)
		System.out.println("/test/login ============");
		OAuth2User  oAuth2User = (OAuth2User)  authentication.getPrincipal();
		System.out.println("authentication : " + oAuth2User.getAttributes());
		System.out.println("oAuth2User : " + oauth.getAttributes());
		return "OAuth 세션 정보 확인하기";
	}
	// 스프링 시큐리티
	// 시큐리티 세션을 가지고 있다. 
	//   Authentication (DI) 객체
	//     UserDetails  Type -> 입반적인 로그인 
	//     OAuth2User Type  -> OAuth  로그인
	
	//     X class 상속 (부모) PrincipalDetails
	//				UserDetails
	//				OAuth2User
	
	
	// localhost:8080/
	// localhost:8080
	@GetMapping({ "", "/" })
	public String index() {
		// 머스테치 기본폴더: src/main/resources/
		// 뷰리졸버 설정: templates (perfix), mustache (suffix)
		return "index"; // src/main/resources/templates/index.mustache
	}

	//OAuth 로그인을 해도 PrincipalDetails
	//일반 로그인을 해도 PrincipalDetails
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails ) {
		System.out.println("PrincipalDetails: " + principalDetails.getUser());
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

	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data()	{
		return "데이터정보";
	}
	
	/*
		 Google oauth API 
		 {"web":
		{"client_id":"864802243323-j9fdvgra3e5qkdb1d1pms95emvh5gkpo.apps.googleusercontent.com",
		"project_id":"esoteric-parsec-371211",
		"auth_uri":"https://accounts.google.com/o/oauth2/auth",
		"token_uri":"https://oauth2.googleapis.com/token",
		"auth_provider_x509_cert_url":"https://www.googleapis.com/oauth2/v1/certs",
		"client_secret":"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",
		"redirect_uris":["http://localhost:8080/login/oauth2/code/google"]}}
	 */

}
