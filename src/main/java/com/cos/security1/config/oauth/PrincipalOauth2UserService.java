package com.cos.security1.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.oauth.provider.FacebookUserInfo;
import com.cos.security1.config.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.oauth.provider.NaverUserInfo;
import com.cos.security1.config.oauth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;	

	@Autowired
	private UserRepository  userRepository;
	
	//구글로 부터 받은 userRequest 데이터 후처리되는 함수
	//함수종료시 @AuthenticationPrincipal  어노테이션이 만들어진다.
	@Override 
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
		//registrationId로 어떤 OAuth로 로그인
		//registrationId='google'
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 
		// -> code를 리턴(OAuth2-Client라이브러리) -> AccessToken요청
		//userRequest  정보 -> 회원프로필 받아야 함(loaduser  함수) -> 구글로부터 회원프로필 정보를 받아준다.
		System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());
		OAuth2User oauth2User = super.loadUser(userRequest); 
		System.out.println("oauth2user getAttributes : " + oauth2User.getAttributes());

		//회원가입을 강제로 진행해볼 예정
		OAuth2UserInfo oAuth2UserInfo = null; 
		if ( userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}else if ( userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		}else if ( userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		}else {
			System.out.println("우리는 네이버, 구글과 페이스북만 지원해요");
		}

		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "-" + providerId;
		String password =bCryptPasswordEncoder.encode("겟인데어"); 
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		/*	
		String provider = userRequest.getClientRegistration().getRegistrationId(); // google
		String providerId = oauth2User.getAttribute("sub"); // ProviderId
		String username = provider + "-" + providerId;
		String password =bCryptPasswordEncoder.encode("겟인데어"); 
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		*/
		
		User userEntity = userRepository.findByUsername(username);
		if (userEntity ==null) {
			System.out.println("로그인이 최초입니다.");
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}else {
			System.out.println("로그인을 이미 한적이 있습니다.");
		}
		return new PrincipalDetails(userEntity,  oauth2User.getAttributes()); 
		//return super.loadUser(userRequest);
	}
}

/*
getClientRegistration : ClientRegistration{registrationId='google', clientId='864802243323-j9fdvgra3e5qkdb1d1pms95emvh5gkpo.apps.googleusercontent.com', clientSecret='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', clientAuthenticationMethod=org.springframework.security.oauth2.core.ClientAuthenticationMethod@4fcef9d3, authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3, redirectUri='{baseUrl}/{action}/oauth2/code/{registrationId}', scopes=[email, profile], providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@7f757bda, clientName='Google'}
getAccessToken : org.springframework.security.oauth2.core.OAuth2AccessToken@8efbd7a0
getAttributes : {
sub=117986258618481711156, 
name=김세웅, 
given_name=세웅, 
family_name=김, 
picture=https://lh3.googleusercontent.com/a/AEdFTp5mnIx8Z6Aiw8paS4ckFD-yGo5Kg9vKFZqPPQYV=s96-c, 
email=sewkim00@gmail.com, 
email_verified=true, 
locale=ko}

 username = "google_117986258618481711156"
 password = "암호화(겟인데어)"
  email = sewkim00@gmail.com
  role = "ROLE_USER"
  provider = "google"
  providerId = "117986258618481711156" 
    
  OAuth2-Client
  Provider
  	google
  	facebook
  	twitter
  Provider
    네이버
    카카오 
*/
