package by.teachmeskills.devteam.repository;

import by.teachmeskills.devteam.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

}

