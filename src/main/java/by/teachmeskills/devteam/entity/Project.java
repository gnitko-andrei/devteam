package by.teachmeskills.devteam.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

import static by.teachmeskills.devteam.util.TextUtils.replaceHyphenationOnBr;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Type(type = "text")
    private String specification;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private User customer;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User manager;

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
        String info = customer.getFirstName() + " " + customer.getLastName()
                + "\n" + customer.getEmail()
                + "\n" + customer.getContacts();
        return customer != null ? replaceHyphenationOnBr(info) : "none";
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
