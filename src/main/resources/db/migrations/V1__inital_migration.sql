
-- Tables
create table appointments (
                              id bigint not null,
                              created_at timestamp(6) with time zone not null,
                              updated_at timestamp(6) with time zone not null,
                              end_time timestamp(6) not null,
                              notes TEXT,
                              paid boolean not null,
                              reason varchar(255),
                              start_time timestamp(6) not null,
                              status varchar(20) not null check ((status in ('SCHEDULED','CONFIRMED','CHECKED_IN','IN_PROGRESS','COMPLETED','CANCELLED','NO_SHOW'))),
                              type varchar(20) not null check ((type in ('CONSULTATION','FOLLOW_UP','EMERGENCY','ONLINE','HOME_VISIT'))),
                              doctor_id bigint not null,
                              patient_id bigint not null,
                              primary key (id)
)
  
create table department (
                            id bigint not null,
                            created_at timestamp(6) with time zone not null,
                            updated_at timestamp(6) with time zone not null,
                            name varchar(255),
                            primary key (id)
)
  
create table doctors (
                         id bigint not null,
                         created_at timestamp(6) with time zone not null,
                         updated_at timestamp(6) with time zone not null,
                         active boolean not null,
                         address varchar(255),
                         biography varchar(1000),
                         city varchar(100),
                         country varchar(100),
                         license_number varchar(50) not null,
                         name varchar(100) not null,
                         phone varchar(20) not null,
                         postal_code varchar(20),
                         qualification varchar(100),
                         specialization varchar(100) not null,
                         state varchar(100),
                         years_of_experience integer not null,
                         department_id bigint,
                         user_id bigint not null,
                         primary key (id)
)
  
create table medical_histories (
                                   id bigint not null,
                                   created_at timestamp(6) with time zone not null,
                                   updated_at timestamp(6) with time zone not null,
                                   active boolean,
                                   description TEXT,
                                   title varchar(200) not null,
                                   type varchar(255) not null check ((type in ('ALLERGY','CHRONIC_DISEASE','SURGERY','MEDICATION','FAMILY_HISTORY','IMMUNIZATION','SOCIAL_HISTORY','OTHER'))),
                                   patient_id bigint not null,
                                   primary key (id)
)
  
create table medical_records (
                                 id bigint not null,
                                 created_at timestamp(6) with time zone not null,
                                 updated_at timestamp(6) with time zone not null,
                                 chief_complaint TEXT,
                                 diagnosis TEXT,
                                 examination TEXT,
                                 notes TEXT,
                                 treatment_plan TEXT,
                                 appointment_id bigint,
                                 doctor_id bigint not null,
                                 medical_history_id bigint not null,
                                 patient_id bigint not null,
                                 primary key (id)
)
  
create table medicine (
                          id bigint not null,
                          created_at timestamp(6) with time zone not null,
                          updated_at timestamp(6) with time zone not null,
                          form varchar(255),
                          generic_name varchar(255),
                          manufacturer varchar(255),
                          name varchar(255) not null,
                          strength varchar(255),
                          primary key (id)
)
  
create table notification (
                              id bigint not null,
                              created_at timestamp(6) with time zone not null,
                              updated_at timestamp(6) with time zone not null,
                              description varchar(255),
                              title varchar(255),
                              user_id bigint,
                              primary key (id)
)
  
create table patients (
                          id bigint not null,
                          created_at timestamp(6) with time zone not null,
                          updated_at timestamp(6) with time zone not null,
                          active boolean not null,
                          address varchar(255),
                          allergies TEXT,
                          blood_group varchar(5),
                          city varchar(100),
                          country varchar(100),
                          date_of_birth date,
                          emergency_contact_name varchar(150),
                          emergency_contact_phone varchar(20),
                          gender varchar(10) check ((gender in ('MALE','FEMALE','OTHER'))),
                          id_card varchar(30),
                          name varchar(200) not null,
                          notes TEXT,
                          phone varchar(20) not null,
                          postal_code varchar(20),
                          state varchar(100),
                          user_id bigint not null,
                          primary key (id)
)
  
create table prescription_items (
                                    id bigint not null,
                                    created_at timestamp(6) with time zone not null,
                                    updated_at timestamp(6) with time zone not null,
                                    dosage varchar(100),
                                    duration varchar(100),
                                    frequency varchar(100),
                                    instructions TEXT,
                                    quantity integer not null,
                                    medicine_id bigint not null,
                                    prescription_id bigint not null,
                                    primary key (id)
)
  
create table prescriptions (
                               id bigint not null,
                               created_at timestamp(6) with time zone not null,
                               updated_at timestamp(6) with time zone not null,
                               diagnosis TEXT,
                               notes TEXT,
                               prescribed_date date not null,
                               appointment_id bigint,
                               doctor_id bigint not null,
                               patient_id bigint not null,
                               primary key (id)
)
  
create table search (
                        id bigint not null,
                        created_at timestamp(6) with time zone not null,
                        updated_at timestamp(6) with time zone not null,
                        primary key (id)
)
  
create table users (
                       id bigint not null,
                       created_at timestamp(6) with time zone not null,
                       updated_at timestamp(6) with time zone not null,
                       email varchar(255) not null,
                       is_verified boolean,
                       otp_token varchar(255),
                       password varchar(255),
                       provider varchar(255),
                       role varchar(255) not null,
                       primary key (id)
)

-- indexes
create index idx_appointment_patient
    on appointments (patient_id)
  
create index idx_appointment_doctor
    on appointments (doctor_id)
  
create index idx_appointment_start
    on appointments (start_time)
  
create index idx_appointment_status
    on appointments (status)
  
create index idx_appointment_doctor_start
    on appointments (doctor_id, start_time)
  
create index idx_appointment_patient_start
    on appointments (patient_id, start_time)
  
create index idx_doctor_phone
    on doctors (phone)
  
create index idx_doctor_license
    on doctors (license_number)
  
create index idx_doctor_specialization
    on doctors (specialization)
  
create index idx_doctor_name
    on doctors (name)
  
alter table if exists doctors
drop constraint if exists uk_doctor_phone

alter table if exists doctors
    add constraint uk_doctor_phone unique (phone)
  
alter table if exists doctors
drop constraint if exists uk_doctor_license

alter table if exists doctors
    add constraint uk_doctor_license unique (license_number)
  
alter table if exists doctors
drop constraint if exists uk_doctor_user_id

alter table if exists doctors
    add constraint uk_doctor_user_id unique (user_id)
  
alter table if exists doctors
drop constraint if exists uk_doctor_user_department_id

alter table if exists doctors
    add constraint uk_doctor_user_department_id unique (department_id)
  
create index idx_history_patient
    on medical_histories (patient_id)
  
create index idx_history_patient_history_type
    on medical_histories (patient_id, type)
  
create index idx_record_patient
    on medical_records (patient_id)
  
create index idx_record_doctor
    on medical_records (doctor_id)
  
create index idx_record_appointment
    on medical_records (appointment_id)
  
create index idx_record_medical_history
    on medical_records (medical_history_id)
  
alter table if exists medical_records
drop constraint if exists uk_doctor_appointment

alter table if exists medical_records
    add constraint uk_doctor_appointment unique (doctor_id, appointment_id)
  
alter table if exists medical_records
drop constraint if exists uk_patient_appointment

alter table if exists medical_records
    add constraint uk_patient_appointment unique (patient_id, appointment_id)
  
alter table if exists medical_records
drop constraint if exists uk_appointment_id

alter table if exists medical_records
    add constraint uk_appointment_id unique (appointment_id)
  
create index idx_medicine_name
    on medicine (name)
  
create index idx_medicine_generic_name
    on medicine (generic_name)
  
create index idx_medicine_manufacturer
    on medicine (manufacturer)
  
alter table if exists medicine
drop constraint if exists uk_medicine_name

alter table if exists medicine
    add constraint uk_medicine_name unique (name)
  
create index idx_notification_user
    on notification (user_id)
  
create index idx_patient_name
    on patients (name)
  
create index idx_patient_dob
    on patients (date_of_birth)
  
alter table if exists patients
drop constraint if exists uk_patient_email

alter table if exists patients
    add constraint uk_patient_email unique (email)
  
alter table if exists patients
drop constraint if exists uk_patient_phone

alter table if exists patients
    add constraint uk_patient_phone unique (phone)
  
alter table if exists patients
drop constraint if exists uk_patient_cnic

alter table if exists patients
    add constraint uk_patient_cnic unique (id_card)
  
alter table if exists patients
drop constraint if exists uk_patient_user_id

alter table if exists patients
    add constraint uk_patient_user_id unique (user_id)
  
create index idx_prescription
    on prescription_items (prescription_id)
  
create index idx_medicine
    on prescription_items (medicine_id)
  
create index idx_prescription_patient
    on prescriptions (patient_id)
  
create index idx_prescription_doctor
    on prescriptions (doctor_id)
  
create index idx_prescription_appointment
    on prescriptions (appointment_id)
  
create index idx_prescription_date
    on prescriptions (prescribed_date)
  
alter table if exists users
drop constraint if exists uk_user_email

alter table if exists users
    add constraint uk_user_email unique (email)
  
create sequence appointments_seq start with 1 increment by 50
  
create sequence department_seq start with 1 increment by 50
  
create sequence doctors_seq start with 1 increment by 50
  
create sequence medical_histories_seq start with 1 increment by 50
  
create sequence medical_records_seq start with 1 increment by 50
  
create sequence medicine_seq start with 1 increment by 50
  
create sequence notification_seq start with 1 increment by 50
  
create sequence patients_seq start with 1 increment by 50
  
create sequence prescription_items_seq start with 1 increment by 50
  
create sequence prescriptions_seq start with 1 increment by 50
  
create sequence search_seq start with 1 increment by 50
  
create sequence users_seq start with 1 increment by 50
  
alter table if exists appointments
    add constraint fk_appointment_doctor
    foreign key (doctor_id)
    references doctors
  
alter table if exists appointments
    add constraint fk_appointment_patient
    foreign key (patient_id)
    references patients
  
alter table if exists doctors
    add constraint fk_department_doctor
    foreign key (department_id)
    references department
  
alter table if exists doctors
    add constraint fk_user_doctor
    foreign key (user_id)
    references users
  
alter table if exists medical_histories
    add constraint fk_patient_medical_history
    foreign key (patient_id)
    references patients
  
alter table if exists medical_records
    add constraint fk_appointment_medical_record
    foreign key (appointment_id)
    references appointments
  
alter table if exists medical_records
    add constraint fk_doctor_medical_record
    foreign key (doctor_id)
    references doctors
  
alter table if exists medical_records
    add constraint fk_medical_history_medical_record
    foreign key (medical_history_id)
    references medical_histories
  
alter table if exists medical_records
    add constraint fk_patient_medical_record
    foreign key (patient_id)
    references patients
  
alter table if exists notification
    add constraint fk_user_notification
    foreign key (user_id)
    references users
  
alter table if exists patients
    add constraint fk_user_patient
    foreign key (user_id)
    references users
  
alter table if exists prescription_items
    add constraint fk_medicine_prescription_item
    foreign key (medicine_id)
    references medicine
  
alter table if exists prescription_items
    add constraint fk_prescription_prescription_item
    foreign key (prescription_id)
    references prescriptions
  
alter table if exists prescriptions
    add constraint fk_prescription_appointment
    foreign key (appointment_id)
    references appointments
  
alter table if exists prescriptions
    add constraint fk_prescription_doctor
    foreign key (doctor_id)
    references doctors
  
alter table if exists prescriptions
    add constraint fk_prescription_patient
    foreign key (patient_id)
    references patients