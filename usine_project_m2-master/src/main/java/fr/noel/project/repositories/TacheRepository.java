package fr.noel.project.repositories;

import fr.noel.project.entities.Jeux;
import fr.noel.project.entities.Tache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TacheRepository extends JpaRepository<Tache,Long> {

    Page<Tache> findByCommande_Id(Long commandeId, Pageable p);

    @Query("select max(t.jeux.duree) from Tache t  where t.commande.id = :idCommande ")
    int findMaxDuree(@Param("idCommande") Long commandeId);

    List<Tache> findByLutin_Id(long idUser);

}
