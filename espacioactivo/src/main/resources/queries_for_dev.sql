

select * from activity;
select * from address;
select * from appointment;
select * from appointment_state;
select * from discipline;
select * from request_to_create_discipline;
select * from reservation;
select * from role;
select * from token;
select * from user;
select * from user_roles;


select * from appointment
join activity where appointment.activity_id = activity.id
and activity.user_id = 7;