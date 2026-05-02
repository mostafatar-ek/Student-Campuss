package com.studentportal.dto;

import jakarta.validation.constraints.Size;

public class SendMessageDTO {

    @Size(max = 1000)
    private String content;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content != null ? content.trim() : ""; }
}
