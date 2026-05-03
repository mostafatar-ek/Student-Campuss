package com.studentportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    @Size(max = 50)
    private String major;

    public String getName() { return name; }
    public void setName(String name) { this.name = name.trim(); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email.replaceAll(" ", ""); }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password.replaceAll(" ", ""); }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major != null ? major.trim() : ""; }
}
