package fr.noel.project.repositories;

import fr.noel.project.entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categorie,Long> {
}
