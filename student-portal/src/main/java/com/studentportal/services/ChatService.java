package com.studentportal.services;

import com.studentportal.dto.SendMessageDTO;
import com.studentportal.models.Message;
import com.studentportal.models.Student;
import com.studentportal.repos.MessageRepo;
import com.studentportal.repos.StudentRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final MessageRepo messageRepo;
    private final StudentRepo studentRepo;
    private final NotifService notifService;
    private final StudentService studentService;

    ChatService(MessageRepo messageRepo, StudentRepo studentRepo,
                NotifService notifService, StudentService studentService) {
        this.messageRepo = messageRepo;
        this.studentRepo = studentRepo;
        this.notifService = notifService;
        this.studentService = studentService;
    }

    public Message sendMessage(Long senderId, Long receiverId, SendMessageDTO data, MultipartFile file) throws IOException {
        Student sender = studentRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Student receiver = studentRepo.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        boolean hasContent = data.getContent() != null && !data.getContent().isBlank();
        boolean hasFile = file != null && !file.isEmpty();
        if (!hasContent && !hasFile) throw new RuntimeException("Message cannot be empty");

        String attachment = hasFile ? studentService.saveFile(file) : null;
        Message message = messageRepo.save(new Message(sender, receiver, hasContent ? data.getContent() : null, attachment));

        notifService.create(receiverId, "MESSAGE", sender.getName() + " sent you a message", senderId);
        return message;
    }

    public List<Message> getConversation(Long studentIdA, Long studentIdB) {
        Student a = studentRepo.findById(studentIdA).orElseThrow(() -> new RuntimeException("Student not found"));
        Student b = studentRepo.findById(studentIdB).orElseThrow(() -> new RuntimeException("Student not found"));
        return messageRepo.findBySenderAndReceiverOrSenderAndReceiverOrderBySentAtDesc(a, b, b, a);
    }

    public List<Student> getConversationPartners(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Student> partners = new ArrayList<>(messageRepo.findBySender(student).stream()
                .map(Message::getReceiver).distinct().toList());
        messageRepo.findByReceiver(student).stream()
                .map(Message::getSender)
                .filter(s -> partners.stream().noneMatch(p -> p.getId().equals(s.getId())))
                .forEach(partners::add);
        return partners;
    }
}
