package fr.noel.project.entities;



import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "Tache")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant dateAffectation;
    private Instant dateFin;

    @ManyToOne(fetch = FetchType.LAZY)
    private Jeux jeux;

    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser lutin;

    @ManyToOne(fetch = FetchType.LAZY)
    private Commande commande;



}
