package fr.noel.project.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class JwtResponse extends ResponseDto {

    private String token;
    private Object content;

    @Builder
    public JwtResponse(boolean success, String message, Object content,String token) {
        super(success,message);
        this.content=content;
        this.token=token;
    }
}
