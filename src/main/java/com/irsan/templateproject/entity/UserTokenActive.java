package com.irsan.templateproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 23/05/2024
 */
@Entity
@Table(name = "user_token_actives")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenActive extends Auditable {

    @Id
    @Column(name = "token_id")
    private String id;
    @Column(name = "access_token", length = 600)
    private String accessToken;
    @Column(name = "expired_date")
    private LocalDateTime expiredDate;
    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void onPrePersist() {
        audit("INSERT");
    }

    @PreUpdate
    public void onPreUpdate() {
        audit("UPDATE");
    }

    private void audit(String operation) {
        if (operation.equals("INSERT")) {
            setCreatedDate(LocalDateTime.now());
            setCreatedBy(user.getUsername());
        }
        setModifiedBy(user.getUsername());
        setModifiedDate(LocalDateTime.now());
    }

}
