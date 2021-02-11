package fr.noel.project.dto;


import fr.noel.project.entities.Categorie;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CategorieDto {
    private Long id;
    private String name;


    static public CategorieDto toCategorieDto(Categorie entity){
        return new CategorieDto(entity.getId(),entity.getName());
    }
}
