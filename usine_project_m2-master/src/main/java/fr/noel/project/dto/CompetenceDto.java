package fr.noel.project.dto;


import fr.noel.project.entities.Competence;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CompetenceDto {

    private Long id;
    private String name;


    public static CompetenceDto toCompetenceDto(Competence entity){
        return new CompetenceDto(entity.getId(),entity.getName());
    }

}
