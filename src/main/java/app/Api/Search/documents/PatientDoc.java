package app.api.search.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDate;

@Document(indexName = "patients", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Search_As_You_Type)
    private String name;

    @Field(type = FieldType.Keyword)
    private String phone;

    @Field(type = FieldType.Keyword)
    private String city;

    @Field(type = FieldType.Keyword)
    private String gender;

    @Field(type = FieldType.Keyword)
    private String bloodGroup;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate dateOfBirth;

    private Boolean active;
}
