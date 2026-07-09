package com.visa_iss_kin.security;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface AuthService {
    LoginResponse login(
            LoginRequest loginRequest
    );
}
