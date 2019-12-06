package cn.boommanpro.unifygateway.cas;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户信息
 */
public class UserInfo   implements UserDetails {
    private static final long serialVersionUID = -1041327031937199938L;

    private String id;
    private String username;
    private String email;
    private String countryCode;
    private String mobile;
    private String nickname;
    private String role;
    private String realName;

    private String password;

    private boolean isAccountNonExpired = true; //是否过期

    private boolean isAccountNonLocked = true;//账户未锁定为true

    private boolean isCredentialsNonExpired = true;//证书不过期为true

    private boolean isEnabled = true;//是否可用

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.getRole() == null || this.getRole().length() <1){
            return AuthorityUtils.commaSeparatedStringToAuthorityList("");
        }
        else{
            return AuthorityUtils.commaSeparatedStringToAuthorityList(this.getRole());
        }
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getCountryCode() {
        return countryCode;
    }


    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getNickname() {
        return nickname;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }


    public String getRealName() {
        return realName;
    }


    public void setRealName(String realName) {
        this.realName = realName;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }


    public void setAccountNonExpired(boolean isAccountNonExpired) {
        this.isAccountNonExpired = isAccountNonExpired;
    }


    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }


    public void setAccountNonLocked(boolean isAccountNonLocked) {
        this.isAccountNonLocked = isAccountNonLocked;
    }


    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }


    public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }


    public boolean isEnabled() {
        return isEnabled;
    }


    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }


    @Override
    public String toString() {
        return "UserInfo [id=" + id + ", username=" + username + ", email=" + email + ", countryCode=" + countryCode
                + ", mobile=" + mobile + ", nickname=" + nickname + ", role=" + role + ", realName=" + realName
                + ", password=" + password + ", isAccountNonExpired=" + isAccountNonExpired + ", isAccountNonLocked="
                + isAccountNonLocked + ", isCredentialsNonExpired=" + isCredentialsNonExpired + ", isEnabled="
                + isEnabled + "]";
    }

}