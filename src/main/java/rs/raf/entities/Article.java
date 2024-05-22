package rs.raf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

public class Article {

    private Long id;

    @NotNull(message = "Title field is required")
    @NotEmpty(message = "Title field is required")
    private String title;

    @NotNull(message = "Text field is required")
    @NotEmpty(message = "Text field is required")
    private String text;

    @NotNull(message = "Author field is required")
    @NotEmpty(message = "Author field is required")
    private String author;
    private String date;
    private Long destinationId;
    private int numberOfVisits = 0;
    private List<Long> activities = new ArrayList<>();

}
