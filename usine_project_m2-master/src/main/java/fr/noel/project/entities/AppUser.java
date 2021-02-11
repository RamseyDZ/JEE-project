package fr.noel.project.entities;


import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private Instant creationDate;
    private Long createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "User_Competence",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "compt_id"))
    private List<Competence> competences = new ArrayList<>();

    @OneToMany(mappedBy = "lutin",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Tache> assignedGames = new ArrayList<>();

}
