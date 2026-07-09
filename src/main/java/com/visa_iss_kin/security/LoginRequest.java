package com.visa_iss_kin.security;

/**
 *
 * @author Faustin PADINGANYI
 */
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class LoginRequest {
    private String userName;
    private String password;

    public LoginRequest() {
    }
}
