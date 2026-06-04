package com.franciscoesquivel.dofuschef.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginDto {
    private String username;
    private String password;
}
