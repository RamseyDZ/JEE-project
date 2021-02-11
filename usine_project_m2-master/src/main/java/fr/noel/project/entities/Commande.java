package fr.noel.project.entities;


import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Commande")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private Instant dateCreation;


    @OneToMany(mappedBy = "commande",cascade = CascadeType.ALL,orphanRemoval = true)
    private  List<Tache> taches = new ArrayList<>();


}
