package by.teachmeskills.devteam.entity;

import by.teachmeskills.devteam.entity.attributes.task.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import static com.google.common.base.Preconditions.checkArgument;

@Entity
@Table(name = "task")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
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

    public Task() {
        this.time = 0;
        this.price = 0;
        this.status = TaskStatus.NEW;
    }

    public Task(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public void submitAdditionalTime(int hours) {
        checkArgument(hours >= 0, "Additional task hours must be non-negative");
        this.time = Math.addExact(this.time, hours);
    }
}
