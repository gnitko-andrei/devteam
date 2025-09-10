package by.teachmeskills.devteam.entity;

import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_seq")
    @SequenceGenerator(name = "hibernate_seq", sequenceName = "hibernate_sequence")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    private String specification;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private User customer;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToMany
    @JoinTable(
            name = "developer_project",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "developer_id")}
    )
    @Builder.Default
    private List<User> developers = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();

    public String getCustomerName() {
        return customer != null ? customer.getFullName(): "none";
    }

    public String getManagerName() {
        return manager != null ? manager.getFullName() : "none";
    }

    public Integer getProjectPrice() {
        return tasks.stream().map(Task::getPrice).mapToInt(Integer::valueOf).sum();
    }
}
