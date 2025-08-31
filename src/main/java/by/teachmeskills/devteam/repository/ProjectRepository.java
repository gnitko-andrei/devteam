package by.teachmeskills.devteam.repository;

import by.teachmeskills.devteam.dto.project.ProjectFiltersDto;
import by.teachmeskills.devteam.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByName(String name);

    @Query("SELECT p FROM Project p " +
            "WHERE p.manager.id = :#{#searchCriteria.userId} " +
            "AND (:#{#searchCriteria.statusFilter} IS NULL OR p.status = :#{#searchCriteria.statusFilter}) " +
            "AND (:#{#searchCriteria.nameFilter} IS NULL OR LOWER(p.name) LIKE CONCAT('%', :#{#searchCriteria.nameFilter}, '%'))")
    List<Project> findManagerProjects(@Param("searchCriteria") ProjectFiltersDto searchCriteria);

    @Query("SELECT p FROM Project p " +
            "WHERE p.customer.id = :#{#searchCriteria.userId} " +
            "AND (:#{#searchCriteria.statusFilter} IS NULL OR p.status = :#{#searchCriteria.statusFilter}) " +
            "AND (:#{#searchCriteria.nameFilter} IS NULL OR LOWER(p.name) LIKE CONCAT('%', :#{#searchCriteria.nameFilter}, '%'))")
    List<Project> findCustomerProjects(@Param("searchCriteria") ProjectFiltersDto searchCriteria);

    @Query("SELECT p FROM Project p " +
            "JOIN p.developers d " +
            "WHERE d.id = :#{#searchCriteria.userId} " +
            "AND (:#{#searchCriteria.statusFilter} IS NULL OR p.status = :#{#searchCriteria.statusFilter}) " +
            "AND (:#{#searchCriteria.nameFilter} IS NULL OR LOWER(p.name) LIKE CONCAT('%', :#{#searchCriteria.nameFilter}, '%'))")
    List<Project> findDeveloperProjects(@Param("searchCriteria") ProjectFiltersDto searchCriteria);
}

