package fr.noel.project.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PageableResponse extends ResponseDto {

    private Object content;
    private long totalElts;
    private int totalPages;


    @Builder
    public PageableResponse(boolean success, String message, Object content, long totalElts,int totalPages) {
        super(success,message);
        this.content=content;
        this.totalElts=totalElts;
        this.totalPages=totalPages;
    }
}