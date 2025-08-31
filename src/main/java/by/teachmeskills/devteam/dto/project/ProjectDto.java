package by.teachmeskills.devteam.dto.project;

import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.entity.project.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static by.teachmeskills.devteam.util.TextUtils.replaceHyphenationOnBr;

@Data
@Builder
public class ProjectDto {
    private Long id;
    private String name;
    private String specification;
    private ProjectStatus status;
    private UserDto customer;
    private UserDto manager;
    private List<UserDto> developers;
    private Integer projectPrice;

    public String getCustomerInfo() {
        return customer != null ? replaceHyphenationOnBr(customer.getFormattedUserInfo()) : "none";
    }

    public String getManagerInfo() {
        return manager != null ? replaceHyphenationOnBr(manager.getFormattedUserInfo()) : "none";
    }
}