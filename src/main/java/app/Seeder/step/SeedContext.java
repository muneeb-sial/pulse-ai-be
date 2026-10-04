package app.seeder.step;

import app.seeder.SeederProperties;
import app.seeder.gen.TextGenerator;
import app.api.appointment.AppointmentEntity;
import app.api.auth.UserEntity;
import app.api.department.DepartmentEntity;
import app.api.doctor.DoctorEntity;
import app.api.medicalhistory.MedicalHistoryEntity;
import app.api.medicine.MedicineEntity;
import app.api.patient.PatientEntity;
import lombok.Getter;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/** State shared across seed steps: generators plus the parents each later step needs for FK wiring. */
@Getter
public class SeedContext {

    private final SeederProperties props;
    private final Random random;
    private final Faker faker;
    private final TextGenerator text;
    private final String passwordHash;

    private final List<UserEntity> allUsers = new ArrayList<>();
    private final List<UserEntity> doctorUsers = new ArrayList<>();
    private final List<UserEntity> patientUsers = new ArrayList<>();
    private final List<DepartmentEntity> departments = new ArrayList<>();
    private final List<MedicineEntity> medicines = new ArrayList<>();
    private final List<DoctorEntity> doctors = new ArrayList<>();
    private final List<PatientEntity> patients = new ArrayList<>();
    private final Map<Long, List<MedicalHistoryEntity>> historiesByPatientId = new HashMap<>();
    private final List<AppointmentEntity> appointments = new ArrayList<>();

    public SeedContext(SeederProperties props, String passwordHash) {
        this.props = props;
        this.passwordHash = passwordHash;
        this.random = new Random(props.getRandomSeed());
        this.faker = new Faker(Locale.ENGLISH, random);
        this.text = new TextGenerator(random);
    }

    /** 1000+ word clinical note. */
    public String note() {
        return text.note(props.getNotes().getMinWords(), props.getNotes().getMaxWords());
    }

    /** Shorter multi-paragraph clinical text for diagnosis / examination / instruction columns. */
    public String detail() {
        return text.detail(props.getDetail().getMinWords(), props.getDetail().getMaxWords());
    }

    public <T> T pick(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
