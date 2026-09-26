package app.Seeder.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Builds long, structured clinical prose (headed sections, paragraphs) from sentence banks with random slot
 * fill-ins. Sentences are drawn without replacement per section, so a 1000+ word note doesn't repeat itself
 * until every sentence of a section has been used.
 */
public class TextGenerator {

    private static final String[] SECTION_ORDER = {
            "Presenting Complaint", "History of Present Illness", "Review of Systems", "Examination Findings",
            "Investigations", "Assessment and Reasoning", "Treatment Plan", "Patient Counselling",
            "Lifestyle and Dietary Advice", "Follow-up and Safety Netting"
    };

    private static final String[][] SENTENCES = {
            // Presenting Complaint
            {
                    "The patient presented with {symptom} that has been present for approximately {duration}.",
                    "On arrival the main concern reported was {symptom}, which the patient described as gradually worsening over {duration}.",
                    "The patient attended today because of {symptom} and asked for a full assessment rather than symptomatic treatment alone.",
                    "A family member accompanied the patient and confirmed that {symptom} had started about {duration} ago.",
                    "In their own words, the {symptom} has begun to interfere with sleep, work and ordinary household activities.",
                    "The onset was described as {onset}, with no single trigger that the patient could identify with confidence.",
                    "The patient rated the severity of the {symptom} as {pain} out of ten at its worst and about half of that at rest.",
                    "Secondary complaints included {symptom2}, which the patient believes may be connected to the main problem.",
                    "The patient denied any recent travel, sick contacts or unusual exposures that might explain the presentation.",
                    "The patient stated that over-the-counter remedies gave only short-lived relief and prompted today's visit.",
                    "There was no history of similar episodes in the past twelve months according to the patient and the available notes.",
                    "The patient was anxious about the possibility of a serious underlying cause and requested a clear explanation of the findings."
            },
            // History of Present Illness
            {
                    "Symptoms began {onset} and have followed a pattern of {course} since then.",
                    "The patient reports that {symptom} is aggravated by {trigger} and eased somewhat by rest and adequate fluids.",
                    "Appetite has been {appetite} and the patient reports {sleep} sleep over the affected period.",
                    "There has been no documented fever above {temp} degrees Celsius at home, although the patient did not monitor temperature regularly.",
                    "The patient has taken {drug} intermittently on their own initiative, with partial and inconsistent benefit.",
                    "Past medical history was reviewed in detail, including previous admissions, procedures, immunisations and known drug reactions.",
                    "The patient works in a role that involves {activity}, which they feel contributes to the current problem.",
                    "Family history was explored, and the patient mentioned relatives with {condition}, which raises the level of clinical vigilance.",
                    "Social history confirmed {habit}, and the patient was asked non-judgementally about alcohol, tobacco and other substances.",
                    "The patient has not noticed any weight change greater than {pct} percent of body weight in recent months.",
                    "Mood and stress levels were discussed, as psychological strain is a recognised amplifier of many physical symptoms.",
                    "Adherence to previously prescribed medication was reviewed and the patient acknowledged occasionally missing doses."
            },
            // Review of Systems
            {
                    "Cardiovascular review was negative for chest pain at rest, orthopnoea, palpitations and ankle swelling.",
                    "Respiratory review noted {resp}, with no haemoptysis and no history of asthma triggered by this episode.",
                    "Gastrointestinal review revealed {gi}, without melaena, persistent vomiting or change in bowel habit lasting more than a week.",
                    "Genitourinary review was unremarkable, with no dysuria, frequency, haematuria or flank pain reported.",
                    "Neurological review found no focal weakness, speech disturbance, visual change or episodes of loss of consciousness.",
                    "Musculoskeletal review identified {msk}, without a history of significant trauma or falls.",
                    "Skin review showed no new rashes, itching, bruising or wounds that were slow to heal.",
                    "Endocrine review was negative for heat or cold intolerance, excessive thirst and unexplained sweating.",
                    "Psychiatric screening did not suggest acute risk, although mild {mood} was acknowledged and will be monitored.",
                    "Haematological screening was negative for prolonged bleeding, easy bruising and recurrent infections.",
                    "Ear, nose and throat review recorded {ent}, which the patient considered minor compared with the main complaint.",
                    "All other systems were reviewed and found to be non-contributory to the presenting problem."
            },
            // Examination Findings
            {
                    "On general inspection the patient appeared {appearance}, well hydrated and in no acute respiratory distress.",
                    "Vital signs were recorded as blood pressure {bp} mmHg, heart rate {hr} beats per minute, temperature {temp} degrees Celsius and oxygen saturation {spo2} percent on room air.",
                    "Height, weight and body mass index were recorded, and the trend compared with previous visits was described as {trend}.",
                    "Cardiovascular examination revealed normal first and second heart sounds, no added murmurs and well perfused peripheries.",
                    "Chest examination showed equal air entry bilaterally with {chest}.",
                    "Abdominal examination found the abdomen soft, with {abd}, and no organomegaly or masses palpated.",
                    "Neurological screening confirmed intact cranial nerves, normal tone and power in all limbs, and symmetrical reflexes.",
                    "Musculoskeletal assessment recorded {msk} with a preserved range of movement apart from mild end-of-range discomfort.",
                    "Examination of the throat and ears showed {ent}, with no exudate or tympanic membrane abnormality.",
                    "Skin and lymph node examination detected no pallor, jaundice, cyanosis or significant lymphadenopathy.",
                    "Capillary refill was under two seconds and there were no signs of dehydration or peripheral oedema.",
                    "The patient co-operated fully with the examination and tolerated all manoeuvres without undue distress."
            },
            // Investigations
            {
                    "A full blood count was requested, and the {lab} result was {labval}, which was interpreted in the context of the presentation.",
                    "Renal function and electrolytes were ordered to guide safe prescribing and to exclude dehydration as a contributor.",
                    "Liver function tests were requested because several of the planned medications are metabolised hepatically.",
                    "A fasting glucose and glycated haemoglobin were requested to screen for undiagnosed disturbance of glucose metabolism.",
                    "Inflammatory markers were sent, and the results will be reviewed alongside the clinical picture rather than in isolation.",
                    "An electrocardiogram was performed in clinic and showed a regular rhythm without acute ischaemic change.",
                    "A urine dipstick was completed and found to be {urine}, which helps to narrow the differential diagnosis.",
                    "Imaging was discussed, and it was agreed to hold off unless symptoms persist beyond {weeks} weeks or red-flag features emerge.",
                    "Results already on file were compared with today's values to identify any meaningful trend over time.",
                    "The patient was told which results would be available the same day and which might take several working days.",
                    "Point-of-care testing was used where available so that management decisions did not have to wait for the laboratory.",
                    "Any abnormal result will be communicated by telephone with a clear explanation of what it means and what happens next."
            },
            // Assessment and Reasoning
            {
                    "Taking the history, examination and initial investigations together, the leading impression is {diagnosis}.",
                    "The differential diagnosis was considered carefully and includes {condition}, which is less likely but was not dismissed without reasoning.",
                    "Features in favour of the working diagnosis include the timing of onset, the character of the symptoms and the examination findings recorded above.",
                    "Features against more serious pathology include stable vital signs, a benign abdomen and chest, and the absence of red-flag symptoms.",
                    "Risk was stratified using the information available, and the patient was judged suitable for outpatient management.",
                    "Comorbidities were factored into the decision, in particular how they affect drug choice, dosing and monitoring requirements.",
                    "The patient's preferences, expectations and previous experience of treatment were taken into account in shaping the plan.",
                    "The clinical picture is consistent with a self-limiting course in most cases, but the possibility of complications was acknowledged.",
                    "Uncertainty was shared openly with the patient, with an explanation of why some tests are being done before others.",
                    "The working diagnosis will be revisited if the response to treatment differs from what would normally be expected.",
                    "Discussion with a senior colleague was considered but judged unnecessary at this stage given the reassuring findings.",
                    "The overall level of concern was recorded as {concern}, and this informed the follow-up interval chosen."
            },
            // Treatment Plan
            {
                    "Treatment was started with {drug} at a dose of {dose}, to be taken as directed for a defined period.",
                    "The rationale, expected benefit and common side effects of {drug} were explained in plain language.",
                    "Simple analgesia and adequate hydration were advised alongside the prescribed medication.",
                    "Doses were adjusted for age, weight and renal function, and interactions with current medicines were checked.",
                    "The patient was advised to complete the full course even if symptoms improve early, unless a side effect appears.",
                    "Non-pharmacological measures, including {activity} modification and structured rest, form an important part of the plan.",
                    "A referral to {specialty} was discussed and will be made if there is no improvement within {weeks} weeks.",
                    "Physiotherapy or nursing input will be arranged if functional limitation persists after the acute phase settles.",
                    "Vaccination status was reviewed and any overdue immunisations were offered or scheduled.",
                    "The patient was given written instructions summarising the medication schedule, warning signs and contact details.",
                    "A medication review is planned to identify any long-term drug that could be reduced or stopped safely.",
                    "Where a treatment choice was equally reasonable, the patient's preference was recorded and respected."
            },
            // Patient Counselling
            {
                    "The diagnosis and plan were explained using simple language, and the patient repeated the key points back accurately.",
                    "Questions were invited throughout, and the patient asked sensible questions about duration, cost and return to normal activity.",
                    "The patient was reassured that the most likely explanation is benign, while being honest about remaining uncertainty.",
                    "The importance of taking medication regularly was discussed, together with practical tips such as alarms and pill organisers.",
                    "Possible side effects were described, along with what to do if they occur and when to stop the medication.",
                    "Emotional impact was acknowledged, and the patient was encouraged to speak openly about worries or stress.",
                    "Carers were included in the conversation with the patient's permission, so that support at home is well informed.",
                    "Information leaflets and trustworthy online resources were suggested, with a warning about unreliable sources.",
                    "Confidentiality and data sharing were explained, including who in the care team can see the record.",
                    "Consent to the plan was obtained verbally and documented, and the patient understood they may change their mind at any time.",
                    "The patient was reminded that missed appointments delay care and was given an easy way to reschedule.",
                    "The consultation closed with a summary of agreed actions and a check that nothing important had been left out."
            },
            // Lifestyle and Dietary Advice
            {
                    "A balanced diet rich in vegetables, whole grains, lean protein and fibre was recommended, with attention to portion size.",
                    "Fluid intake of roughly {fluid} litres per day was advised unless there is a medical reason to restrict fluids.",
                    "Salt, refined sugar and heavily processed foods should be reduced, particularly given the recorded blood pressure.",
                    "Regular moderate activity such as brisk walking for thirty minutes on most days was encouraged once symptoms allow.",
                    "Sleep hygiene was discussed, including consistent bedtimes, limiting screens at night and avoiding late caffeine.",
                    "Alcohol should be kept within safe limits or avoided entirely while on the prescribed medication.",
                    "If the patient smokes, cessation support was offered, and the health benefits were described in concrete terms.",
                    "Stress management options such as breathing exercises, short walks and talking to a trusted person were recommended.",
                    "Weight management was discussed sensitively, with realistic goals rather than dramatic short-term targets.",
                    "Hand hygiene and sensible precautions were reviewed to protect household members from any infectious cause.",
                    "Ergonomic adjustments at work and at home were suggested to reduce strain and recurrent symptoms.",
                    "The patient was encouraged to keep a short symptom diary to help identify triggers and to guide review."
            },
            // Follow-up and Safety Netting
            {
                    "A review appointment was arranged in {weeks} weeks, or sooner if symptoms worsen or new concerns arise.",
                    "The patient was told to seek urgent care for chest pain, breathing difficulty, confusion, fainting or heavy bleeding.",
                    "Safety-netting advice was given verbally and in writing, including the hospital telephone number and emergency contacts.",
                    "Test results will be reviewed by the treating doctor and communicated within an agreed time frame.",
                    "The patient should return earlier if the fever persists beyond {weeks} days despite treatment.",
                    "Care will be coordinated with the referring team so that duplicated tests and conflicting advice are avoided.",
                    "The record was updated with the diagnosis, medications, allergies and the follow-up plan for continuity of care.",
                    "The patient confirmed understanding of the plan and left in a stable condition, able to mobilise independently.",
                    "A reminder message will be sent before the next visit, and the patient was asked to bring all current medication.",
                    "If the response to treatment is poor, the diagnosis will be reconsidered and further investigation will be arranged.",
                    "Long-term monitoring needs were noted, and the patient was added to the appropriate recall list.",
                    "The consultation was completed without incident and the plan was agreed by both the patient and the clinician."
            }
    };

    private static final String[] SYMPTOMS = {"persistent cough", "recurrent headache", "lower back pain", "abdominal discomfort",
            "shortness of breath on exertion", "intermittent chest tightness", "joint stiffness and swelling", "unexplained fatigue",
            "dizziness on standing", "sore throat with hoarseness", "nausea with reduced appetite", "palpitations at night",
            "burning pain on urination", "skin rash with itching", "blurred vision", "swelling of the ankles", "knee pain after activity",
            "episodes of wheeze", "difficulty sleeping", "loose stools"};
    private static final String[] DURATIONS = {"three days", "five days", "one week", "ten days", "two weeks", "three weeks", "about a month", "six weeks", "several months"};
    private static final String[] ONSETS = {"suddenly", "gradually over several days", "after a viral illness", "following a period of heavy physical work", "insidiously over weeks", "after a change of medication"};
    private static final String[] COURSES = {"steady progression", "fluctuation with brief good days", "slow improvement followed by relapse", "worsening in the evenings", "morning predominance"};
    private static final String[] TRIGGERS = {"physical exertion", "cold air", "prolonged sitting", "heavy meals", "emotional stress", "lying flat", "climbing stairs"};
    private static final String[] APPETITES = {"reduced", "normal", "somewhat poor", "variable", "increased"};
    private static final String[] SLEEP = {"disturbed", "broken", "adequate", "shortened", "restless"};
    private static final String[] DRUGS = {"paracetamol", "ibuprofen", "amoxicillin", "azithromycin", "omeprazole", "cetirizine", "metformin", "amlodipine", "salbutamol", "atorvastatin", "loratadine", "naproxen", "levothyroxine", "ciprofloxacin"};
    private static final String[] DOSES = {"500 mg twice daily", "250 mg three times daily", "10 mg once daily", "20 mg at night", "100 mg twice daily", "5 mg once daily", "two puffs as required"};
    private static final String[] ACTIVITIES = {"prolonged desk work", "heavy lifting", "long-distance driving", "shift work", "manual labour", "standing for many hours", "frequent travel"};
    private static final String[] CONDITIONS = {"hypertension", "type 2 diabetes", "ischaemic heart disease", "asthma", "thyroid disease", "migraine", "osteoarthritis", "peptic ulcer disease", "chronic kidney disease", "anaemia"};
    private static final String[] HABITS = {"a non-smoker with occasional caffeine intake", "a light smoker of about five cigarettes a day", "regular exercise three times a week", "a largely sedentary lifestyle", "no alcohol use", "irregular meal times because of work"};
    private static final String[] RESP = {"a mild dry cough without sputum", "occasional wheeze at night", "no cough or wheeze", "mild breathlessness only on climbing stairs"};
    private static final String[] GI = {"mild epigastric burning after meals", "occasional bloating", "normal appetite and regular bowel habit", "intermittent nausea without vomiting"};
    private static final String[] MSK = {"tenderness over the lumbar paraspinal muscles", "stiffness in both knees in the morning", "mild pain in the right shoulder on abduction", "no joint swelling or deformity"};
    private static final String[] MOODS = {"low mood", "anxiety", "irritability", "tiredness"};
    private static final String[] ENT = {"mild pharyngeal erythema", "clear ear canals bilaterally", "nasal congestion without discharge", "no sinus tenderness"};
    private static final String[] APPEARANCE = {"comfortable", "mildly tired", "anxious but cooperative", "alert and oriented", "pale but stable"};
    private static final String[] TRENDS = {"stable", "slightly increased", "slightly decreased", "unchanged since the last review"};
    private static final String[] CHEST = {"no crackles or wheeze", "scattered expiratory wheeze that cleared after coughing", "vesicular breath sounds throughout", "mild basal crepitations that resolved on deep breathing"};
    private static final String[] ABD = {"mild epigastric tenderness without guarding", "no tenderness or rebound", "normal bowel sounds", "slight right iliac fossa discomfort without peritonism"};
    private static final String[] LABS = {"haemoglobin", "white cell count", "C-reactive protein", "serum creatinine", "fasting glucose", "platelet count", "ferritin"};
    private static final String[] LABVALS = {"within the reference range", "marginally raised", "mildly reduced", "at the upper limit of normal", "consistent with the clinical picture"};
    private static final String[] URINE = {"negative for blood, protein and nitrites", "trace positive for leucocytes", "clear with normal specific gravity"};
    private static final String[] DIAGNOSES = {"an acute viral upper respiratory tract infection", "uncomplicated lower back strain", "functional dyspepsia", "tension-type headache",
            "mild persistent asthma", "essential hypertension requiring optimisation", "early osteoarthritis of the knee", "iron deficiency anaemia", "an uncomplicated urinary tract infection", "allergic rhinitis"};
    private static final String[] CONCERNS = {"low", "low to moderate", "moderate", "raised but manageable in the community"};
    private static final String[] SPECIALTIES = {"cardiology", "gastroenterology", "orthopaedics", "endocrinology", "pulmonology", "neurology", "dermatology", "urology"};

    private final Random random;

    public TextGenerator(Random random) {
        this.random = random;
    }

    /** Sectioned clinical note of at least {@code minWords} words (target drawn between min and max). */
    public String note(int minWords, int maxWords) {
        return build(minWords, maxWords, true);
    }

    /** Plain paragraphs, no headings - for diagnosis / examination / instructions style columns. */
    public String detail(int minWords, int maxWords) {
        return build(minWords, maxWords, false);
    }

    public static int wordCount(String text) {
        return text == null || text.isBlank() ? 0 : text.trim().split("\\s+").length;
    }

    private String build(int minWords, int maxWords, boolean headings) {
        int target = minWords + (maxWords > minWords ? random.nextInt(maxWords - minWords + 1) : 0);
        List<List<String>> pools = new ArrayList<>();
        for (int i = 0; i < SENTENCES.length; i++) {
            pools.add(new ArrayList<>());
        }
        StringBuilder out = new StringBuilder(target * 7);
        int words = 0;
        int section = random.nextInt(SECTION_ORDER.length);
        while (words < target) {
            List<String> pool = pools.get(section);
            if (pool.size() < 6) {
                pool.clear();
                pool.addAll(List.of(SENTENCES[section]));
                Collections.shuffle(pool, random);
            }
            if (headings) {
                out.append(SECTION_ORDER[section]).append('\n');
            }
            int sentences = 4 + random.nextInt(3);
            StringBuilder para = new StringBuilder();
            for (int i = 0; i < sentences; i++) {
                String s = fill(pool.remove(pool.size() - 1));
                para.append(s).append(' ');
                words += wordCount(s);
            }
            out.append(para.toString().trim()).append("\n\n");
            section = (section + 1) % SECTION_ORDER.length;
        }
        return out.toString().trim();
    }

    private String fill(String template) {
        StringBuilder sb = new StringBuilder(template.length() + 32);
        int i = 0;
        while (i < template.length()) {
            char c = template.charAt(i);
            int end = c == '{' ? template.indexOf('}', i) : -1;
            if (end > 0) {
                sb.append(slot(template.substring(i + 1, end)));
                i = end + 1;
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    private String slot(String key) {
        return switch (key) {
            case "symptom", "symptom2" -> pick(SYMPTOMS);
            case "duration" -> pick(DURATIONS);
            case "onset" -> pick(ONSETS);
            case "course" -> pick(COURSES);
            case "trigger" -> pick(TRIGGERS);
            case "appetite" -> pick(APPETITES);
            case "sleep" -> pick(SLEEP);
            case "drug" -> pick(DRUGS);
            case "dose" -> pick(DOSES);
            case "activity" -> pick(ACTIVITIES);
            case "condition" -> pick(CONDITIONS);
            case "habit" -> pick(HABITS);
            case "resp" -> pick(RESP);
            case "gi" -> pick(GI);
            case "msk" -> pick(MSK);
            case "mood" -> pick(MOODS);
            case "ent" -> pick(ENT);
            case "appearance" -> pick(APPEARANCE);
            case "trend" -> pick(TRENDS);
            case "chest" -> pick(CHEST);
            case "abd" -> pick(ABD);
            case "lab" -> pick(LABS);
            case "labval" -> pick(LABVALS);
            case "urine" -> pick(URINE);
            case "diagnosis" -> pick(DIAGNOSES);
            case "concern" -> pick(CONCERNS);
            case "specialty" -> pick(SPECIALTIES);
            case "pain" -> String.valueOf(3 + random.nextInt(7));
            case "pct" -> String.valueOf(2 + random.nextInt(6));
            case "weeks" -> String.valueOf(2 + random.nextInt(5));
            case "temp" -> String.format("%.1f", 36.4 + random.nextDouble() * 2.4);
            case "bp" -> (105 + random.nextInt(50)) + "/" + (65 + random.nextInt(30));
            case "hr" -> String.valueOf(58 + random.nextInt(50));
            case "spo2" -> String.valueOf(94 + random.nextInt(6));
            case "fluid" -> String.format("%.1f", 1.5 + random.nextInt(3) * 0.5);
            default -> key;
        };
    }

    private String pick(String[] values) {
        return values[random.nextInt(values.length)];
    }
}
