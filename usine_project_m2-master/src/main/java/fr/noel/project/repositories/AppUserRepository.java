package fr.noel.project.repositories;

import fr.noel.project.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    AppUser findByEmail(String email);

    @Query("select id from AppUser u where u.email=:email")
    Long findIdByEmail(@Param("email") String email);

    List<AppUser> findByCreatedBy(Long createdBy);
}
