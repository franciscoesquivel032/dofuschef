package com.franciscoesquivel.dofuschef.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private boolean success;
}
