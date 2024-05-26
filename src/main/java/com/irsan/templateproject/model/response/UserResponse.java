package com.irsan.templateproject.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.irsan.templateproject.model.enumeration.Role;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 23/05/2024
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {

    private static final long serialVersionUID = 5913875213210629033L;

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private Role authorities;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    @JsonIgnore
    private List<String> tokenActives;

}
