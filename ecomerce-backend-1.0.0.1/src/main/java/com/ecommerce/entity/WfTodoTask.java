package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "wf_todo_task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WfTodoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "biz_type", nullable = false, length = 30)
    private String bizType;

    @Column(name = "biz_id", nullable = false)
    private Long bizId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "priority", nullable = false, length = 10)
    private String priority;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "is_done", nullable = false)
    private Integer isDone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Transient
    private String userName;
}
