package by.teachmeskills.devteam.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "user")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("java:S1948")
public class User implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_seq")
    @SequenceGenerator(name = "hibernate_seq", sequenceName = "hibernate_sequence")
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private boolean active;
    private String firstName;
    private String lastName;
    private String email;
    private String contacts;
    private Integer price; //only for developers
    private String skills; //only for developers

    @ManyToMany
    @JoinTable(
            name = "developer_project",
            joinColumns = {@JoinColumn(name = "developer_id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id")}
    )
    @Builder.Default
    private Set<Project> projects = new HashSet<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getFullName() {
        var fullName = Stream.of(this.firstName, this.lastName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        return fullName.isBlank() ? username : fullName;
    }

    public String getFormattedUserInfo() {
        return Stream.of(this.getFullName(), email, contacts)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
    }

    public String getRolesDescription() {
        var stringJoiner = new StringJoiner(", ");
        this.roles.stream().filter(role -> role != Role.USER).map(Role::getRoleName).sorted().forEach(stringJoiner::add);
        return stringJoiner.toString();
    }
}

