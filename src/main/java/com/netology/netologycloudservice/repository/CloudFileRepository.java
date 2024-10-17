package com.netology.netologycloudservice.repository;

import com.netology.netologycloudservice.dao.CloudFile;
import com.netology.netologycloudservice.dao.CloudUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CloudFileRepository extends JpaRepository<CloudFile, Long> {

    @Query("SELECT f FROM CloudFile f WHERE f.owner = :userId")
    List<CloudFile> findByOwner(@Param("userId") Long userId);

    @Query("SELECT f FROM CloudFile f WHERE f.id = :id AND f.owner = :userId")
    Optional<CloudFile> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    @Query("SELECT f FROM CloudFile f WHERE f.owner = :userId AND f.filename = :filename")
    Optional<CloudFile> findByOwnerAndFilename(@Param("userId") Long userId, @Param("filename") String filename);

}
