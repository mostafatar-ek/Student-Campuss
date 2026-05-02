package com.studentportal.services;

import com.studentportal.dto.EditProfileDTO;
import com.studentportal.models.Student;
import com.studentportal.repos.StudentRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepo studentRepo;

    @Value("${upload.dir}")
    private String uploadDir;

    StudentService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Student getProfile(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Student updateProfile(Long id, EditProfileDTO data) {
        Student student = getProfile(id);
        student.loadFromDTO(data);
        return studentRepo.save(student);
    }

    public Student uploadAvatar(Long id, MultipartFile file) throws IOException {
        Student student = getProfile(id);
        student.setAvatar(saveFile(file));
        return studentRepo.save(student);
    }

    public List<Student> searchStudents(String query) {
        if (query == null || query.isBlank()) return studentRepo.findAll();
        List<Student> byName = studentRepo.findByNameContainingIgnoreCase(query);
        List<Student> byMajor = studentRepo.findByMajorContainingIgnoreCase(query);
        byName.addAll(byMajor.stream()
                .filter(s -> byName.stream().noneMatch(n -> n.getId().equals(s.getId())))
                .toList());
        return byName;
    }

    public String saveFile(MultipartFile file) throws IOException {
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        String original = file.getOriginalFilename();
        String ext = (original != null && original.contains("."))
                ? original.substring(original.lastIndexOf('.')) : "";
        String filename = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
