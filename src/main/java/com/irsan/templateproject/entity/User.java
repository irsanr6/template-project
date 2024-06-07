package com.irsan.templateproject.entity;

import com.irsan.templateproject.model.enumeration.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 17/05/2024
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Auditable {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Role authorities;
    @Column(columnDefinition = "bool default true", insertable = false, updatable = false)
    private boolean accountNonExpired;
    @Column(columnDefinition = "bool default true", insertable = false, updatable = false)
    private boolean accountNonLocked;
    @Column(columnDefinition = "bool default true", insertable = false, updatable = false)
    private boolean credentialsNonExpired;
    @Column(columnDefinition = "bool default true", insertable = false, updatable = false)
    private boolean enabled;
    @Column(columnDefinition = "int4 default 0", insertable = false, updatable = false)
    private int failedAttempt;
    private LocalDateTime lockTime;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserProfile userProfile;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTokenActive> userTokenActives = new ArrayList<>();

}
