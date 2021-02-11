package fr.noel.project.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Jeux")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Jeux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int duree;
    private boolean isArchived;
    private String statut;


    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "Jeux_Competence",joinColumns = @JoinColumn(name = "jeux_id"),inverseJoinColumns = @JoinColumn(name = "comp_id"))
    private List<Competence> competences = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Categorie categorie;


    @OneToMany(mappedBy = "jeux",cascade = CascadeType.ALL,orphanRemoval = true)
    private  List<Tache> taches = new ArrayList<>();


}
