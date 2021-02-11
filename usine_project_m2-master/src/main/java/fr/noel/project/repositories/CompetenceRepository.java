package fr.noel.project.repositories;


import fr.noel.project.entities.Competence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetenceRepository extends JpaRepository<Competence,Long> {

    List<Competence> findByIdIn(List<Long> ids);
}
