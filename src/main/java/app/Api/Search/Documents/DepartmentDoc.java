package app.Api.Search.Documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "departments", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;
}
