package by.teachmeskills.devteam.entity.task;

import by.teachmeskills.devteam.entity.project.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
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
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.time = 0;
        this.price = 0;
        this.status = TaskStatus.NEW;
    }

    public void submitAdditionalTime(int hours) {
        this.time = this.time == null ? hours : this.time + hours;
    }
}
