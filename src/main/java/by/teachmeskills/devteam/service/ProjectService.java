package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static by.teachmeskills.devteam.util.TextUtils.replaceHyphenationOnBr;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserService userService;

    public Project findById(Long id) {
        return projectRepository.findById(id).get();
    }

    public Iterable<Project> findAll() {
        return projectRepository.findAll();
    }

    public Iterable<Project> findByName(String name) {
        return projectRepository.findByName(name);
    }

    public void save(Project project) {
        projectRepository.save(project);
    }


    public void delete(Project project) {
        projectRepository.delete(project);
    }

    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    public void addNewProject(String name, String specification, User user, Long managerId) {
        specification = replaceHyphenationOnBr(specification);
        User manager = userService.findById(managerId);
        Project project = new Project(name, specification, "ожидание обработки менеджером", user, manager);
        save(project);
    }

    public void updateProject(Long id, Map<String, String> formData) {
        Project project = projectRepository.findById(id).get();
        User manager = userService.findById(Long.parseLong(formData.get("managerId")));

        project.setName(formData.get("name"));
        project.setSpecification(replaceHyphenationOnBr(formData.get("specification")));
        project.setManager(manager);
        projectRepository.save(project);
    }


    public List<User> getAllManagers() {
        List<User> allUsers = userService.findAll();
        List<User> managers = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getRoles().contains(Role.MANAGER)) {
                managers.add(user);
            }
        }
        return managers;
    }
}
