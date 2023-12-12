package org.university.deanery.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classrooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Integer classroomNo;

    @OneToOne(mappedBy = "classroom", cascade = CascadeType.ALL)
    private Timetable timetable;

    public Classroom(Integer classroomNo) {
        this.classroomNo = classroomNo;
    }
}
