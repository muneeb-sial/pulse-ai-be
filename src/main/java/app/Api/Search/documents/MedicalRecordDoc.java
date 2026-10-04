package app.api.search.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "medical_records", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDoc {

    @Id
    private Long id;

    private Long patientId;

    private Long doctorId;

    private Long appointmentId;

    private Long medicalHistoryId;

    @Field(type = FieldType.Text)
    private String diagnosis;
}
