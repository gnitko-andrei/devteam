package by.teachmeskills.devteam.repository;

import by.teachmeskills.devteam.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByStatus(String status);

}

