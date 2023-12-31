package co.kr.lotteon.security;

import co.kr.lotteon.entity.member.MemberEntity;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j2
@Getter
@Setter
@Builder
@ToString
public class MyUserDetails implements UserDetails {
    public MyUserDetails (MemberEntity member){
        this.member = member;
    }

    private MemberEntity member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 계정이 갖는 권한 목록
        List<GrantedAuthority> authorities = new ArrayList<>();
        MemberType memberType = MemberType.values()[member.getType() - 1];

        authorities.add(new SimpleGrantedAuthority(memberType.getRole()));
        log.info("Role : "+memberType.getRole());
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPass();
    }

    @Override
    public String getUsername() {
        return member.getUid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
