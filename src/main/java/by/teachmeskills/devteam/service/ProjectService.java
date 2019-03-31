package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

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

    public void addNewProject(String name, String specification, User user) {
        specification = replaceHyphenationOnBr(specification);
        Project project = new Project(name, specification, "ожидание обработки менеджером", user);
        save(project);
    }

    public String replaceHyphenationOnBr(String text) {
        return text.replaceAll("\n", "<br>");
    }

    public String replaceBrOnHyphenation(String text) {
        return text.replaceAll("<br>", "\n");
    }

    public void updateProject(Long id, Map<String, String> formData) {
        Project project = projectRepository.findById(id).get();

        project.setName(formData.get("name"));
        project.setSpecification(replaceHyphenationOnBr(formData.get("specification")));
        projectRepository.save(project);
    }


}
