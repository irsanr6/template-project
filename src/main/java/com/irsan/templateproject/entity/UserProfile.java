package com.irsan.templateproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 23/05/2024
 */
@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile extends Auditable {

    @Id
    @Column(name = "user_id")
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    private String email;
    private String address;
    private String phone;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

}
