package fr.noel.project.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ResponseDto {
    protected Boolean success;
    protected String message;
}
