package fr.noel.project.dto;


import fr.noel.project.entities.Tache;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TacheDto {

    private Long id;
    private Instant dateAffectation;
    private Instant dateFin;

    private JeuxDto jeux;
    private UserDto lutin;
    //private CommandeDto commande;


    public static TacheDto toTacheDto(Tache entity) {
        final TacheDto tacheDto = new TacheDto();
        tacheDto.setId(entity.getId());
        tacheDto.setDateAffectation(entity.getDateAffectation());
        tacheDto.setDateFin(entity.getDateFin());
        tacheDto.setJeux(JeuxDto.toJeuxDto(entity.getJeux()));
        tacheDto.setLutin(UserDto.toUserDto(entity.getLutin()));
        //tacheDto.setCommande(CommandeDto.toCommandeDto(entity.getCommande()));
        return tacheDto;
    }

}
