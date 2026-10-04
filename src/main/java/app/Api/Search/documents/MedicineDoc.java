package app.api.search.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "medicines", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Search_As_You_Type)
    private String name;

    @Field(type = FieldType.Text)
    private String genericName;

    @Field(type = FieldType.Text)
    private String manufacturer;

    @Field(type = FieldType.Keyword)
    private String strength;

    @Field(type = FieldType.Keyword)
    private String form;
}
