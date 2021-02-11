package fr.noel.project.dto;

import fr.noel.project.entities.Competence;
import fr.noel.project.entities.Jeux;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class JeuxDto {

    private Long id;
    private String name;
    private Integer duree;
    private boolean isArchived;
    private String categorieName;
    private Long categorieId;
    List<CompetenceDto> competences;

    public static JeuxDto toJeuxDto(Jeux entity) {
        JeuxDto jeuxDto = new JeuxDto();
        jeuxDto.setId(entity.getId());
        jeuxDto.setName(entity.getName());
        jeuxDto.setArchived(entity.isArchived());
        jeuxDto.setDuree(entity.getDuree());
        jeuxDto.setCategorieName(entity.getCategorie().getName());
        jeuxDto.setCategorieId(entity.getCategorie().getId());
        final List<Competence> competences = entity.getCompetences();
        if (competences != null && competences.size() > 0) {
            final List<CompetenceDto> comptList = competences.stream().map(CompetenceDto::toCompetenceDto).collect(Collectors.toList());
            jeuxDto.setCompetences(comptList);
        }
        return jeuxDto;
    }

}
