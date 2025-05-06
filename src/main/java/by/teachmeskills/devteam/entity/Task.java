package by.teachmeskills.devteam.entity;

import jakarta.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_seq")
    @SequenceGenerator(name = "hibernate_seq", sequenceName = "hibernate_sequence")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    private String description;

    @Column(nullable = false)
    private Integer time;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public Task() {
    }

    public Task(String name, String description, Integer time, Integer price, String status, Project project) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.price = price;
        this.status = status;
        this.project = project;
    }

    public Task(String name, String description, Project project) {
        this.name = name;
        this.description = description;
        this.time = 0;
        this.price = 0;
        this.status = "Ожидание выполнения";
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
