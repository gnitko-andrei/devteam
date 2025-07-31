package by.teachmeskills.devteam.entity;

import jakarta.persistence.*;

import java.util.*;

import static by.teachmeskills.devteam.util.TextUtils.replaceHyphenationOnBr;

@Entity
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
    private String status;

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
    private Set<User> developers = new HashSet<>();


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private List<Task> tasks = new ArrayList<>();

    public Project() {
    }

    public Project(String name, String specification, String status, User customer, User manager) {
        this.name = name;
        this.specification = specification;
        this.status = status;
        this.customer = customer;
        this.manager = manager;
    }

    public String getCustomerName() {
        return customer != null ? customer.getFirstName() + " " + customer.getLastName() : "none";
    }

    public String getCustomerInfo() {
        return customer != null ? replaceHyphenationOnBr(buildCustomerInfo(customer)) : "none";
    }

    private String buildCustomerInfo(User customer) {
        return customer.getFirstName() + " " + customer.getLastName()
                + "\n" + customer.getEmail()
                + "\n" + customer.getContacts();
    }

    public String getManagerName() {
        return manager != null ? manager.getFirstName() + " " + manager.getLastName() : "none";
    }

    public String getManagerInfo() {
        if (manager != null) {
            String info = manager.getFirstName() + " " + manager.getLastName()
                    + "\n" + manager.getEmail()
                    + "\n" + manager.getContacts();
            return replaceHyphenationOnBr(info);
        }
        return "none";
    }

    public Integer getProjectPrice() {
        Integer projectPrice = 0;
        for (Task task : tasks) {
            projectPrice += task.getPrice();
        }
        return projectPrice;
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

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Set<User> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<User> developers) {
        this.developers = developers;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
