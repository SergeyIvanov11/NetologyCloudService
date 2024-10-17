package com.netology.netologycloudservice.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Files")
public class CloudFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename", nullable = false)
    String filename;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner")
    CloudUser owner; // владелец файла

    @Column(name = "filepath", nullable = false)
    String filePath; // путь к файлу

    @Column(name = "content", nullable = false)
    byte[] content; // содержимое файла

    @CreationTimestamp
    @Column(name = "date_of_upload", nullable = false)
    LocalDateTime dateOfUpload; // время и дата загрузки файла
}
