package app.api.search.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "doctors", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Search_As_You_Type)
    private String name;

    @Field(type = FieldType.Text)
    private String specialization;

    @Field(type = FieldType.Text)
    private String qualification;

    @Field(type = FieldType.Keyword)
    private String city;

    @Field(type = FieldType.Text)
    private String biography;

    private Integer yearsOfExperience;

    private Long departmentId;

    @Field(type = FieldType.Text)
    private String departmentName;

    private Boolean active;
}
