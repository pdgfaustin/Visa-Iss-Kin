package com.visa_iss_kin.security;

import com.visa_iss_kin.model.UserRole;

/**
 *
 * @author Faustin PADINGANYI
 */
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class LoginResponse {
    private String userName;
    private String nomComplet;
    private String email;
    private UserRole role;
    private Boolean actif;
    private Boolean compteVerrouille;
    private Boolean premiereConnexion;
    private String message;

    public LoginResponse() {
    }
}
