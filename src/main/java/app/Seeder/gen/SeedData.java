package app.seeder.gen;

import app.shared.enums.MedicalHistoryType;

import java.util.List;
import java.util.Map;

/** Static lookup banks used by the seeders. */
public final class SeedData {

    private SeedData() {
    }

    public record MedicineTemplate(String generic, List<String> strengths, List<String> forms) {
    }

    public static final List<String> DEPARTMENTS = List.of(
            "Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Dermatology", "Oncology", "Gastroenterology",
            "Pulmonology", "Endocrinology", "Nephrology", "Urology", "Psychiatry", "Ophthalmology", "ENT",
            "Obstetrics and Gynecology", "General Medicine", "Emergency Medicine", "Radiology", "Rheumatology", "Hematology");

    /** Specializations a doctor in the given department can hold. */
    public static final Map<String, List<String>> SPECIALIZATIONS = Map.ofEntries(
            Map.entry("Cardiology", List.of("Interventional Cardiologist", "Cardiac Electrophysiologist", "Heart Failure Specialist")),
            Map.entry("Neurology", List.of("Neurologist", "Epileptologist", "Stroke Specialist")),
            Map.entry("Orthopedics", List.of("Orthopedic Surgeon", "Spine Surgeon", "Sports Medicine Physician")),
            Map.entry("Pediatrics", List.of("Pediatrician", "Neonatologist", "Pediatric Pulmonologist")),
            Map.entry("Dermatology", List.of("Dermatologist", "Cosmetic Dermatologist")),
            Map.entry("Oncology", List.of("Medical Oncologist", "Radiation Oncologist")),
            Map.entry("Gastroenterology", List.of("Gastroenterologist", "Hepatologist")),
            Map.entry("Pulmonology", List.of("Pulmonologist", "Sleep Medicine Specialist")),
            Map.entry("Endocrinology", List.of("Endocrinologist", "Diabetologist")),
            Map.entry("Nephrology", List.of("Nephrologist", "Transplant Nephrologist")),
            Map.entry("Urology", List.of("Urologist", "Andrologist")),
            Map.entry("Psychiatry", List.of("Psychiatrist", "Child Psychiatrist")),
            Map.entry("Ophthalmology", List.of("Ophthalmologist", "Retina Specialist")),
            Map.entry("ENT", List.of("ENT Surgeon", "Audiologist")),
            Map.entry("Obstetrics and Gynecology", List.of("Obstetrician", "Gynecologist", "Fertility Specialist")),
            Map.entry("General Medicine", List.of("General Physician", "Internal Medicine Specialist")),
            Map.entry("Emergency Medicine", List.of("Emergency Physician", "Trauma Specialist")),
            Map.entry("Radiology", List.of("Radiologist", "Interventional Radiologist")),
            Map.entry("Rheumatology", List.of("Rheumatologist")),
            Map.entry("Hematology", List.of("Hematologist")));

    public static final List<String> QUALIFICATIONS = List.of("MBBS", "MBBS, FCPS", "MBBS, MD", "MBBS, MRCP", "MBBS, MS", "MD, PhD", "MBBS, DM", "MBBS, MCh");

    public static final List<MedicineTemplate> MEDICINES = List.of(
            new MedicineTemplate("Paracetamol", List.of("250 mg", "500 mg", "650 mg", "1 g"), List.of("Tablet", "Syrup", "Suppository")),
            new MedicineTemplate("Ibuprofen", List.of("200 mg", "400 mg", "600 mg"), List.of("Tablet", "Suspension")),
            new MedicineTemplate("Amoxicillin", List.of("250 mg", "500 mg", "875 mg"), List.of("Capsule", "Suspension")),
            new MedicineTemplate("Azithromycin", List.of("250 mg", "500 mg"), List.of("Tablet", "Suspension")),
            new MedicineTemplate("Ciprofloxacin", List.of("250 mg", "500 mg", "750 mg"), List.of("Tablet")),
            new MedicineTemplate("Cefixime", List.of("100 mg", "200 mg", "400 mg"), List.of("Tablet", "Suspension")),
            new MedicineTemplate("Omeprazole", List.of("20 mg", "40 mg"), List.of("Capsule")),
            new MedicineTemplate("Pantoprazole", List.of("20 mg", "40 mg"), List.of("Tablet", "Injection")),
            new MedicineTemplate("Metformin", List.of("500 mg", "850 mg", "1000 mg"), List.of("Tablet")),
            new MedicineTemplate("Glimepiride", List.of("1 mg", "2 mg", "4 mg"), List.of("Tablet")),
            new MedicineTemplate("Insulin Glargine", List.of("100 IU/ml"), List.of("Injection")),
            new MedicineTemplate("Amlodipine", List.of("2.5 mg", "5 mg", "10 mg"), List.of("Tablet")),
            new MedicineTemplate("Losartan", List.of("25 mg", "50 mg", "100 mg"), List.of("Tablet")),
            new MedicineTemplate("Atenolol", List.of("25 mg", "50 mg", "100 mg"), List.of("Tablet")),
            new MedicineTemplate("Atorvastatin", List.of("10 mg", "20 mg", "40 mg", "80 mg"), List.of("Tablet")),
            new MedicineTemplate("Rosuvastatin", List.of("5 mg", "10 mg", "20 mg"), List.of("Tablet")),
            new MedicineTemplate("Aspirin", List.of("75 mg", "100 mg", "300 mg"), List.of("Tablet")),
            new MedicineTemplate("Clopidogrel", List.of("75 mg"), List.of("Tablet")),
            new MedicineTemplate("Warfarin", List.of("1 mg", "3 mg", "5 mg"), List.of("Tablet")),
            new MedicineTemplate("Salbutamol", List.of("100 mcg", "2 mg", "4 mg"), List.of("Inhaler", "Tablet", "Syrup")),
            new MedicineTemplate("Montelukast", List.of("4 mg", "5 mg", "10 mg"), List.of("Tablet", "Chewable Tablet")),
            new MedicineTemplate("Cetirizine", List.of("5 mg", "10 mg"), List.of("Tablet", "Syrup")),
            new MedicineTemplate("Loratadine", List.of("10 mg"), List.of("Tablet", "Syrup")),
            new MedicineTemplate("Prednisolone", List.of("5 mg", "10 mg", "20 mg"), List.of("Tablet")),
            new MedicineTemplate("Levothyroxine", List.of("25 mcg", "50 mcg", "100 mcg"), List.of("Tablet")),
            new MedicineTemplate("Furosemide", List.of("20 mg", "40 mg"), List.of("Tablet", "Injection")),
            new MedicineTemplate("Spironolactone", List.of("25 mg", "50 mg", "100 mg"), List.of("Tablet")),
            new MedicineTemplate("Diclofenac", List.of("50 mg", "75 mg", "100 mg"), List.of("Tablet", "Injection", "Gel")),
            new MedicineTemplate("Naproxen", List.of("250 mg", "500 mg"), List.of("Tablet")),
            new MedicineTemplate("Tramadol", List.of("50 mg", "100 mg"), List.of("Capsule", "Injection")),
            new MedicineTemplate("Ondansetron", List.of("4 mg", "8 mg"), List.of("Tablet", "Injection")),
            new MedicineTemplate("Domperidone", List.of("10 mg"), List.of("Tablet", "Suspension")),
            new MedicineTemplate("Loperamide", List.of("2 mg"), List.of("Capsule")),
            new MedicineTemplate("Sertraline", List.of("25 mg", "50 mg", "100 mg"), List.of("Tablet")),
            new MedicineTemplate("Escitalopram", List.of("5 mg", "10 mg", "20 mg"), List.of("Tablet")),
            new MedicineTemplate("Alprazolam", List.of("0.25 mg", "0.5 mg", "1 mg"), List.of("Tablet")),
            new MedicineTemplate("Levetiracetam", List.of("250 mg", "500 mg", "1000 mg"), List.of("Tablet", "Syrup")),
            new MedicineTemplate("Gabapentin", List.of("100 mg", "300 mg", "400 mg"), List.of("Capsule")),
            new MedicineTemplate("Ferrous Sulfate", List.of("200 mg", "325 mg"), List.of("Tablet", "Syrup")),
            new MedicineTemplate("Folic Acid", List.of("1 mg", "5 mg"), List.of("Tablet")),
            new MedicineTemplate("Vitamin D3", List.of("1000 IU", "5000 IU", "60000 IU"), List.of("Capsule", "Drops")),
            new MedicineTemplate("Calcium Carbonate", List.of("500 mg", "1000 mg"), List.of("Tablet")),
            new MedicineTemplate("Doxycycline", List.of("100 mg"), List.of("Capsule", "Tablet")),
            new MedicineTemplate("Metronidazole", List.of("200 mg", "400 mg", "500 mg"), List.of("Tablet", "Suspension")),
            new MedicineTemplate("Fluconazole", List.of("50 mg", "150 mg", "200 mg"), List.of("Capsule")),
            new MedicineTemplate("Acyclovir", List.of("200 mg", "400 mg", "800 mg"), List.of("Tablet", "Cream")),
            new MedicineTemplate("Ceftriaxone", List.of("500 mg", "1 g", "2 g"), List.of("Injection")),
            new MedicineTemplate("Enoxaparin", List.of("20 mg", "40 mg", "60 mg"), List.of("Injection")),
            new MedicineTemplate("Tamsulosin", List.of("0.2 mg", "0.4 mg"), List.of("Capsule")),
            new MedicineTemplate("Hydrocortisone", List.of("1%", "2.5%"), List.of("Cream", "Ointment")));

    public static final List<String> MANUFACTURERS = List.of(
            "Getz Pharma", "Searle Pharmaceuticals", "Abbott Laboratories", "GlaxoSmithKline", "Pfizer", "Novartis",
            "Sanofi", "Bayer", "Hilton Pharma", "Martin Dow", "Sami Pharmaceuticals", "Highnoon Laboratories",
            "Ferozsons Laboratories", "Bosch Pharmaceuticals", "Platinum Pharmaceuticals", "AstraZeneca", "Cipla", "Sun Pharma");

    public static final List<String> BLOOD_GROUPS = List.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

    public static final List<String> DOSAGES = List.of("1 tablet", "2 tablets", "1 capsule", "5 ml", "10 ml", "1 puff", "2 puffs", "1 injection");
    public static final List<String> FREQUENCIES = List.of("Once daily", "Twice daily", "Three times daily", "Four times daily", "At bedtime", "Every 8 hours", "Every 12 hours", "As needed");
    public static final List<String> DURATIONS = List.of("3 days", "5 days", "7 days", "10 days", "14 days", "1 month", "3 months");

    public static final Map<MedicalHistoryType, List<String>> HISTORY_TITLES = Map.of(
            MedicalHistoryType.ALLERGY, List.of("Penicillin allergy", "Peanut allergy", "Dust mite allergy", "Latex sensitivity", "Sulfa drug reaction"),
            MedicalHistoryType.CHRONIC_DISEASE, List.of("Type 2 diabetes mellitus", "Essential hypertension", "Chronic asthma", "Hypothyroidism", "Chronic kidney disease stage 2"),
            MedicalHistoryType.SURGERY, List.of("Appendectomy", "Cholecystectomy", "Knee arthroscopy", "Cesarean section", "Hernia repair"),
            MedicalHistoryType.MEDICATION, List.of("Long-term statin therapy", "Daily aspirin", "Inhaled corticosteroid use", "Levothyroxine replacement"),
            MedicalHistoryType.FAMILY_HISTORY, List.of("Family history of coronary artery disease", "Family history of diabetes", "Family history of breast cancer", "Family history of stroke"),
            MedicalHistoryType.IMMUNIZATION, List.of("Childhood immunization series", "Annual influenza vaccine", "COVID-19 vaccination", "Hepatitis B vaccination", "Tetanus booster"),
            MedicalHistoryType.SOCIAL_HISTORY, List.of("Former smoker", "Occasional alcohol use", "Sedentary occupation", "Shift-work sleep pattern"),
            MedicalHistoryType.OTHER, List.of("Previous hospital admission", "Recurrent migraines", "Chronic lower back pain"));

    public static final List<String> APPOINTMENT_REASONS = List.of(
            "Routine check-up", "Follow-up visit", "Persistent cough", "Chest pain evaluation", "Blood pressure review",
            "Diabetes management", "Back pain", "Skin rash", "Headache and dizziness", "Post-operative review",
            "Annual physical", "Medication review", "Abdominal pain", "Joint pain", "Vaccination", "Fever and fatigue");

    public static final List<String> NOTIFICATION_TITLES = List.of(
            "Appointment reminder", "Prescription ready", "Lab results available", "Appointment rescheduled",
            "Payment received", "Follow-up recommended", "Profile updated", "New message from your care team");

    public static final List<String> NOTIFICATION_BODIES = List.of(
            "You have an upcoming appointment. Please arrive ten minutes early and bring any previous reports.",
            "Your prescription has been issued and is available in your records.",
            "New laboratory results have been added to your medical record.",
            "Your appointment time has changed. Please review the updated schedule.",
            "We have received your payment. Thank you.",
            "Your doctor has recommended a follow-up visit within the next few weeks.",
            "Your profile information was updated successfully.",
            "Your care team has shared new information with you. Open the app to read it.");
}
