package com.irsan.templateproject.model.request;

import com.irsan.templateproject.model.enumeration.Role;
import lombok.*;

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
public class UserRequest {

    private String username;
    private String password;
    private Role authorities;
    private String fullName;
    private String email;
    private String phone;
    private String address;

}
