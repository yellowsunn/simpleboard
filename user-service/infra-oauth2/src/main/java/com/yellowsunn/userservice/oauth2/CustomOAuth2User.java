package com.yellowsunn.userservice.oauth2;

import com.yellowsunn.userservice.domain.user.User;
import com.yellowsunn.userservice.domain.user.UserRole;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private final boolean exist;
    private String uuid;
    private String email;
    private String tempUserToken;

    @Builder(builderMethodName = "existUserBuilder", builderClassName = "ExistUserBuilder")
    private CustomOAuth2User(OAuth2Attribute oAuth2Attribute,
                             User user) {
        super(
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())),
                oAuth2Attribute.getAttributes(),
                oAuth2Attribute.getNameAttributeKey()
        );
        this.exist = true;
        this.uuid = user.getUuid();
        this.email = user.getEmail();
    }

    @Builder(builderMethodName = "tempUserBuilder", builderClassName = "TempUserBuilder")
    private CustomOAuth2User(OAuth2Attribute oAuth2Attribute, String tempUserToken) {
        super(
                Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())),
                oAuth2Attribute.getAttributes(),
                oAuth2Attribute.getNameAttributeKey()
        );
        this.exist = false;
        this.tempUserToken = tempUserToken;
    }
}
