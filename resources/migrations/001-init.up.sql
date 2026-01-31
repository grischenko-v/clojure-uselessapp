create table if not exists events (
  id int generated always as identity primary key,
  event varchar(200)
);