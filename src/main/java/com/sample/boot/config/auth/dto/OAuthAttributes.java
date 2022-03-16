package com.sample.boot.config.auth.dto;

import com.sample.boot.domain.user.Role;
import com.sample.boot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
//        if("kakao".equals(registrationId)){
//            return ofKakao("id", attributes);
//        }
        if("naver".equals(registrationId)){
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> naverAccount = (Map<String, Object>) attributes.get("naverAccount");

        return OAuthAttributes.builder()
                .name((String) naverAccount.get("name"))
                .email((String) naverAccount.get("email"))
                .picture((String) naverAccount.get("profile_image"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

//    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
//        // 카카오는 kakao_account에 유저정보가 있음
//        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//        // kakao_account 안에 또 profile이라는 JSON 객체가 이어서 존재함 (nickname, profile_image)
//        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
//
//        return OAuthAttributes.builder()
//                .name((String) kakaoProfile.get("nickname"))
//                .email((String) kakaoAccount.get("email"))
//                .picture((String) kakaoProfile.get("profile_image_url"))
//                .attributes(attributes)
//                .nameAttributeKey(userNameAttributeName)
//                .build();
//    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
