package fr.noel.project.dto;



import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ContentResponseDto extends ResponseDto {
    private Object content;

    @Builder
    public ContentResponseDto(boolean success, String message, Object content) {
        super(success,message);
        this.content=content;
    }
}
