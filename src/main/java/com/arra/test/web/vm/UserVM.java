package com.arra.test.web.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserVM {
    @NotNull
    @Size(min=2, max = 63)
    public String username;

    @NotNull
    @Size(min = 4, max = 512)
    public String password;

    public boolean rememberMe;


}
