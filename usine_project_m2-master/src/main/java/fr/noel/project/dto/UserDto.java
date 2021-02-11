package fr.noel.project.dto;


import fr.noel.project.entities.AppUser;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Long createdBy;

    List<CompetenceDto> competences;


    public static UserDto toUserDto(AppUser entity){
        final UserDto userDto = new UserDto();
        userDto.setId(entity.getId());
        userDto.setName(entity.getName());
        userDto.setPassword("");
        userDto.setEmail(entity.getEmail());
        userDto.setRole(entity.getRole().getName());
        final List<CompetenceDto> comptList = entity.getCompetences().stream().map(CompetenceDto::toCompetenceDto).collect(Collectors.toList());
        userDto.setCompetences(comptList);
        return userDto;
    }
}
