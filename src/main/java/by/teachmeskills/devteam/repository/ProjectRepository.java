package by.teachmeskills.devteam.repository;

import by.teachmeskills.devteam.entity.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Long> {

    List<Project> findByName(String name);

    List<Project> findByStatus(String status);

}

