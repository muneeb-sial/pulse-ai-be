package app.Api.Search.Documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Document(indexName = "prescriptions", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDoc {

    @Id
    private Long id;

    private Long patientId;

    private Long doctorId;

    private Long appointmentId;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate prescribedDate;

    @Field(type = FieldType.Text)
    private String diagnosis;

    @Field(type = FieldType.Text)
    private List<String> medicineNames;
}
