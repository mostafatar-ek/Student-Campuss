package com.studentportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePostDTO {

    @NotBlank
    @Size(max = 1000)
    private String content;

    private String subjectTag;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content.trim(); }

    public String getSubjectTag() { return subjectTag; }
    public void setSubjectTag(String subjectTag) { this.subjectTag = subjectTag != null ? subjectTag.trim() : ""; }
}
