package by.teachmeskills.devteam.repository;

import by.teachmeskills.devteam.entity.attributes.task.Task;
import by.teachmeskills.devteam.entity.TaskStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    @Query("SELECT t FROM Task t " +
            "WHERE t.project.id = :projectId " +
            "AND (:status IS NULL OR t.status = :status)")
    List<Task> findByProjectIdAndStatus(@Param("projectId") Long projectId, @Param("status") TaskStatus status);
}

