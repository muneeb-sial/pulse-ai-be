package app.api.search.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "medical_histories", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalHistoryDoc {

    @Id
    private Long id;

    private Long patientId;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Search_As_You_Type)
    private String title;

    private Boolean active;
}
