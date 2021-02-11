package fr.noel.project.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Categorie")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "categorie",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Jeux> jeux = new ArrayList<>();
}
