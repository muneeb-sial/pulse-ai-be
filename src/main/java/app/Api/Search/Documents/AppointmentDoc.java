package app.Api.Search.Documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Document(indexName = "appointments", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDoc {

    @Id
    private Long id;

    private Long patientId;

    private Long doctorId;

    @Field(type = FieldType.Text)
    private String patientName;

    @Field(type = FieldType.Text)
    private String doctorName;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime startTime;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime endTime;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Keyword)
    private String type;

    private Boolean paid;
}
