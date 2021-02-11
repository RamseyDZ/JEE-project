package fr.noel.project.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Role")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL)
    private List<AppUser> users = new ArrayList<>();


}
