package by.teachmeskills.devteam.entity;

import by.teachmeskills.devteam.entity.attributes.task.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;

@Entity
@Table(name = "task")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("java:S1948")
public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_seq")
    @SequenceGenerator(name = "hibernate_seq", sequenceName = "hibernate_sequence")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Integer time = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer price = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskStatus status = TaskStatus.NEW;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @EqualsAndHashCode.Exclude
    private Project project;

    public void submitAdditionalTime(int hours) {
        checkArgument(hours >= 0, "Additional task hours must be non-negative");
        this.time = Math.addExact(this.time, hours);
    }
}
