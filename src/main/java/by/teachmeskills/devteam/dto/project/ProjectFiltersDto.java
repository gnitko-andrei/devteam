package by.teachmeskills.devteam.dto.project;

import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.project.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ProjectFiltersDto {
    private Long userId;
    private Set<Role> userRoles;
    private String nameFilter;
    private ProjectStatus statusFilter;

    public void setNameFilter(String nameFilter) {
        this.nameFilter = (nameFilter == null || nameFilter.isBlank())
                ? null
                : nameFilter.trim().toLowerCase();
    }
}