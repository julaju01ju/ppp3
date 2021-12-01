package com.javamentor.qa.platform.models.entity.user;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@ToString
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_entity")
public class User implements UserDetails {

    private static final long serialVersionUID = 8086496705293852501L;

    @Id
    @GeneratedValue(generator = "User_seq")
    private Long id;

    @Column
    @NonNull
    private String email;

    @Column
    @NonNull
    private String password;

    @Column
    private String fullName;

    @Column(name = "persist_date", updatable = false)
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @CreationTimestamp
    private LocalDateTime persistDateTime;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column
    private String city;

    @Column(name = "link_site")
    private String linkSite;

    @Column(name = "link_github")
    private String linkGitHub;

    @Column(name = "link_vk")
    private String linkVk;

    @Column
    private String about;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "reputation_count")
    private Long reputationCount;

    @Column(name = "last_redaction_date", nullable = false)
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDateTime;

    @Column
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Role.class, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "role_id", nullable = false)
    @NonNull
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(fullName, user.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, fullName);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDateTime getPersistDateTime() {
        return persistDateTime;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public String getCity() {
        return city;
    }

    public String getLinkSite() {
        return linkSite;
    }

    public String getLinkGitHub() {
        return linkGitHub;
    }

    public String getLinkVk() {
        return linkVk;
    }

    public String getAbout() {
        return about;
    }

    public String getImageLink() {
        return imageLink;
    }

    public Long getReputationCount() {
        return reputationCount;
    }

    public LocalDateTime getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public String getNickname() {
        return nickname;
    }

    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPersistDateTime(LocalDateTime persistDateTime) {
        this.persistDateTime = persistDateTime;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLinkSite(String linkSite) {
        this.linkSite = linkSite;
    }

    public void setLinkGitHub(String linkGitHub) {
        this.linkGitHub = linkGitHub;
    }

    public void setLinkVk(String linkVk) {
        this.linkVk = linkVk;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setReputationCount(Long reputationCount) {
        this.reputationCount = reputationCount;
    }

    public void setLastUpdateDateTime(LocalDateTime lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
