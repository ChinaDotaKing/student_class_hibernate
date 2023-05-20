package org.example.domain.hibernate;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.Semester;
import org.example.domain.WebRegClass;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="Semester" )
public class SemesterHibernate extends Semester {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private int id;

    @Column
    private String name;

    @Column(name ="start_date")
    private Date start_date;

    @Column(name ="end_date")
    private Date end_date;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "semesterHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<WebRegClassHibernate> webRegClassHibernates = new HashSet<>();
}
