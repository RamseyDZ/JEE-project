package fr.noel.project.dto;

import fr.noel.project.entities.Commande;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommandeDto {

    private Long id;
    private String comment;
    private Instant dateCreation;
    private List<TacheDto> taches;
    private String statut;


    public static  CommandeDto toCommandeDto(Commande entity){
        CommandeDto commandeDto = new CommandeDto();
        commandeDto.setId(entity.getId());
        commandeDto.setComment(entity.getComment());
        commandeDto.setDateCreation(entity.getDateCreation());
        final List<TacheDto> tacheDtoList = entity.getTaches().stream().map(TacheDto::toTacheDto).collect(Collectors.toList());
        commandeDto.setTaches(tacheDtoList);
        return commandeDto;
    }

}

