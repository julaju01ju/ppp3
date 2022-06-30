alter table bookmarks
add column persist_date timestamp;
alter table single_chat
add column sort_ascending_flag boolean;