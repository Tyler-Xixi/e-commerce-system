package com.ecommerce.repository;

import com.ecommerce.entity.WfTodoTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WfTodoTaskRepository extends JpaRepository<WfTodoTask, Long> {
    List<WfTodoTask> findByUserIdAndIsDoneOrderByCreatedAtDesc(Long userId, Integer isDone);
    Page<WfTodoTask> findByUserIdAndIsDone(Long userId, Integer isDone, Pageable pageable);
    long countByUserIdAndIsDone(Long userId, Integer isDone);
}
