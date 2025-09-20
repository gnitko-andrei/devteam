package by.teachmeskills.devteam.dto.project;

import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class ProjectFiltersDto {
    private Long userId;
    private Set<Role> userRoles;
    private String nameFilter;
    private ProjectStatus statusFilter;

    @Builder
    public ProjectFiltersDto(Long userId,
                             java.util.Set<Role> userRoles,
                             String nameFilter,
                             ProjectStatus statusFilter) {
        this.userId = userId;
        this.userRoles = userRoles;
        this.setNameFilter(nameFilter);
        this.statusFilter = statusFilter;
    }

    public void setNameFilter(String nameFilter) {
        if (nameFilter == null || nameFilter.isBlank()) {
            this.nameFilter = null;
            return;
        }
        var n = nameFilter.trim().toLowerCase();
        n = n.replace("!", "!!").replace("%", "!%").replace("_", "!_");
        this.nameFilter = n;
    }
}
