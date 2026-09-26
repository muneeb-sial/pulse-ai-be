-- A department must be able to have many doctors; the old constraint capped it at one.
alter table if exists doctors
    drop constraint if exists uk_doctor_user_department_id;
