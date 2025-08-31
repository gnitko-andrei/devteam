package by.teachmeskills.devteam.entity;

import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
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
    private List<User> developers = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private List<Task> tasks = new ArrayList<>();

    public Project(String name, String specification, ProjectStatus status, User customer, User manager) {
        this.name = name;
        this.specification = specification;
        this.status = status;
        this.customer = customer;
        this.manager = manager;
    }

    public boolean isVisibleForUser(Long userId) {
        var users = new ArrayList<User>();
        Optional.ofNullable(developers).ifPresent(users::addAll);
        Optional.ofNullable(manager).ifPresent(users::add);
        Optional.ofNullable(customer).ifPresent(users::add);
        return users.stream()
                .anyMatch(user -> Objects.equals(user.getId(), userId));
    }

    public String getCustomerName() {
        return customer != null ? customer.getFirstName() + " " + customer.getLastName() : "none";
    }

    public String getManagerName() {
        return manager != null ? manager.getFirstName() + " " + manager.getLastName() : "none";
    }

    public Integer getProjectPrice() {
        return tasks.stream().map(Task::getPrice).mapToInt(Integer::valueOf).sum();
    }
}
