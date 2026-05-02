package com.studentportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EditProfileDTO {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Size(max = 50)
    private String major;

    @Size(max = 300)
    private String bio;

    public String getName() { return name; }
    public void setName(String name) { this.name = name.trim(); }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major != null ? major.trim() : ""; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio != null ? bio.trim() : ""; }
}
