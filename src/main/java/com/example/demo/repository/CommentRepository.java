package com.example.demo.repository;

import com.example.demo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторій для доступу до коментарів, пов'язаних з обладнанням.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Знаходить всі коментарі, пов'язані з конкретним обладнанням.
     * @param equipmentId ID обладнання
     * @return список коментарів
     */
    List<Comment> findByEquipmentId(Long equipmentId);
}
