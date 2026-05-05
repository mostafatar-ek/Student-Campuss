package com.studentportal.dto;

import com.studentportal.models.Message;

public class MessageView {

    private final Message message;
    private final boolean mine;

    public MessageView(Message message, boolean mine) {
        this.message = message;
        this.mine = mine;
    }

    public Message getMessage() { return message; }
    public boolean isMine() { return mine; }

    // delegate the most-used fields so the template stays clean
    public Long getId() { return message.getId(); }
    public com.studentportal.models.Student getSender() { return message.getSender(); }
    public String getContent() { return message.getContent(); }
    public String getAttachment() { return message.getAttachment(); }
    public java.time.LocalDateTime getSentAt() { return message.getSentAt(); }
}
