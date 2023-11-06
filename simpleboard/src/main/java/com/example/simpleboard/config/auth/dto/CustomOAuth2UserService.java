package com.example.simpleboard.config.auth.dto;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.simpleboard.entity.Member;
import com.example.simpleboard.entity.Role;
import com.example.simpleboard.repository.MemberRepository;
import com.example.simpleboard.repository.RoleRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
    {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttribute attributes = OAuthAttribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUser(member));

        return new DefaultOAuth2User(member.getAuthorities(),
            attributes.getAttributes(),
            attributes.getNameAttributeKey())
        ;
    }

    public Member saveOrUpdate(OAuthAttribute attr)
    {
        var member = attr.toEntitywithoutRole();
        var target = memberRepository.findByEmail(member.getEmail());
        if(target.isPresent())
        {
            return target.get();
        }
        else
        {
            var basicUserRole = roleRepository.findByName("User").orElse(null);
            if(basicUserRole == null)
            {
                var newRole = Role.builder()
                    .name("User")
                    .descRole("this is simple role for user")
                    .build()
                ;
                basicUserRole = roleRepository.save(newRole);
            }
            member.getRoles().add(basicUserRole);
            member = memberRepository.save(member);
        }
        return member;
    }
}
