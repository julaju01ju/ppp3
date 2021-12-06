CREATE TABLE "role" (
  "id" bigint PRIMARY KEY NOT NULL,
  "name" VARCHAR(45)
);

CREATE TABLE "users_entity" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "fullName" VARCHAR(255),
  "password" VARCHAR(255),
  "persist_date" DATETIME,
  "role_id" bigint NOT NULL,
  "last_redaction_date" DATETIME,
  "email" VARCHAR(255),
  "about" LONGTEXT,
  "city" VARCHAR(255),
  "link_site" VARCHAR(255),
  "link_github" VARCHAR(255),
  "link_vk" VARCHAR(255),
  "is_enabled" boolean,
  "is_deleted" boolean,
  "image_link" VARCHAR(255)
);

CREATE TABLE "question" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "title" VARCHAR(255) NOT NULL,
  "description" LONGTEXT NOT NULL,
  "persist_date" DATETIME,
  "view_count" INT,
  "user_id" bigint NOT NULL,
  "is_deleted" boolean,
  "last_redaction_date" DATETIME
);

CREATE TABLE "question_viewed" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "user_id" bigint NOT NULL,
  "question_id" bigint NOT NULL,
  "persist_date" DATETIME
);

CREATE TABLE "answer" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "user_id" bigint,
  "question_id" bigint,
  "html_body" LONGTEXT,
  "persist_date" DATETIME,
  "is_helpful" boolean,
  "is_deleted" boolean,
  "date_accept_time" DATETIME,
  "update_date" DATETIME
);

CREATE TABLE "votes_on_answers" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "user_id" bigint NOT NULL,
  "answer_id" bigint NOT NULL,
  "persist_date" DATETIME,
  "vote" INT
);

CREATE TABLE "votes_on_questions" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "user_id" bigint NOT NULL,
  "question_id" bigint NOT NULL,
  "persist_date" DATETIME,
  "vote" INT
);

CREATE TABLE "tag" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "name" VARCHAR(45),
  "description" LONGTEXT,
  "persist_date" DATETIME
);

CREATE TABLE "tag_ignore" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "ignored_tag_id" bigint,
  "user_id" bigint,
  "persist_date" DATETIME
);

CREATE TABLE "tag_tracked" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "tracked_tag_id" bigint,
  "user_id" bigint,
  "persist_date" DATETIME
);

CREATE TABLE "question_has_tag" (
  "tag_id" bigint NOT NULL,
  "question_id" bigint NOT NULL
);

CREATE TABLE "comment" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "user_id" INT,
  "text" LONGTEXT,
  "persist_date" DATETIME,
  "last_redaction_date" DATETIME,
  "conmment_type" INT
);

CREATE TABLE "user_favorite_question" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "persist_date" DATETIME,
  "user_id" bigint NOT NULL,
  "question_id" bigint NOT NULL
);

CREATE TABLE "comment_answer" (
  "comment_id" bigint NOT NULL,
  "answer_id" bigint NOT NULL
);

CREATE TABLE "comment_question" (
  "comment_id" bigint NOT NULL,
  "question_id" bigint NOT NULL
);

CREATE TABLE "related_tag" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "child_tag" bigint NOT NULL,
  "main_tag" bigint NOT NULL
);

CREATE TABLE "reputaion" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "count" int,
  "persist_date" DATE,
  "type" INT,
  "author_id" bigint,
  "sender_id" bigint,
  "question_id" bigint
);

CREATE TABLE "badges" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "badge_name" VARCHAR(255),
  "description" VARCHAR(255),
  "reputations_for_merit" int
);

CREATE TABLE "user_badges" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "ready" boolean,
  "user_id" bigint,
  "badges_id" bigint
);

CREATE TABLE "chat" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "chat_type" INT,
  "persist_date" DATE,
  "title" VARCHAR(255)
);

CREATE TABLE "group_chat" (
  "chat_id" bigint
);

CREATE TABLE "groupchat_has_users" (
  "chat_id" bigint,
  "user_id" bigint
);

CREATE TABLE "message" (
  "id" BIGSERIAL PRIMARY KEY NOT NULL,
  "last_redaction_date" DATE,
  "message" LONGTEXT,
  "persist_date" DATE,
  "user_sender_id" bigint,
  "chat_id" bigint
);

CREATE TABLE "single_chat" (
  "chat_id" bigint,
  "use_two_id" bigint,
  "user_one_id" bigint
);

CREATE TABLE "bookmarks" (
  "id" bigint PRIMARY KEY NOT NULL,
  "question_id" bigint,
  "user_id" bigint
);

ALTER TABLE "users_entity" ADD FOREIGN KEY ("role_id") REFERENCES "role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "question" ADD FOREIGN KEY ("user_id") REFERENCES "users_entity" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "answer" ADD FOREIGN KEY ("user_id") REFERENCES "users_entity" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "answer" ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "question_has_tag" ADD FOREIGN KEY ("tag_id") REFERENCES "tag" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "question_has_tag" ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "reputaion" ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "comment" ADD FOREIGN KEY ("user_id") REFERENCES "users_entity" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "comment_answer" ADD FOREIGN KEY ("comment_id") REFERENCES "comment" ("id");

ALTER TABLE "comment_answer" ADD FOREIGN KEY ("answer_id") REFERENCES "answer" ("id");

ALTER TABLE "comment" ADD FOREIGN KEY ("id") REFERENCES "comment_question" ("comment_id");

ALTER TABLE "question" ADD FOREIGN KEY ("id") REFERENCES "comment_question" ("question_id");

ALTER TABLE "user_favorite_question" ADD FOREIGN KEY ("user_id") REFERENCES "users_entity" ("id");

ALTER TABLE "user_favorite_question" ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id");

ALTER TABLE "tag" ADD FOREIGN KEY ("id") REFERENCES "related_tag" ("child_tag");

ALTER TABLE "tag" ADD FOREIGN KEY ("id") REFERENCES "related_tag" ("main_tag");

ALTER TABLE "reputaion" ADD FOREIGN KEY ("author_id") REFERENCES "users_entity" ("id");

ALTER TABLE "reputaion" ADD FOREIGN KEY ("sender_id") REFERENCES "users_entity" ("id");

ALTER TABLE "user_badges" ADD FOREIGN KEY ("user_id") REFERENCES "users_entity" ("id");

ALTER TABLE "user_badges" ADD FOREIGN KEY ("badges_id") REFERENCES "badges" ("id");

ALTER TABLE "votes_on_answers" ADD FOREIGN KEY ("user_id") REFERENCES "users_entity" ("id");

ALTER TABLE "votes_on_answers" ADD FOREIGN KEY ("answer_id") REFERENCES "answer" ("id");

ALTER TABLE "question_viewed" ADD FOREIGN KEY ("user_id") REFERENCES "users_entity" ("id");

ALTER TABLE "question_viewed" ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id");

ALTER TABLE "users_entity" ADD FOREIGN KEY ("link_site") REFERENCES "users_entity" ("link_vk");

ALTER TABLE "chat" ADD FOREIGN KEY ("id") REFERENCES "single_chat" ("chat_id");

ALTER TABLE "chat" ADD FOREIGN KEY ("id") REFERENCES "group_chat" ("chat_id");

ALTER TABLE "users_entity" ADD FOREIGN KEY ("id") REFERENCES "single_chat" ("user_one_id");

ALTER TABLE "users_entity" ADD FOREIGN KEY ("id") REFERENCES "single_chat" ("use_two_id");

ALTER TABLE "chat" ADD FOREIGN KEY ("id") REFERENCES "groupchat_has_users" ("chat_id");

ALTER TABLE "users_entity" ADD FOREIGN KEY ("id") REFERENCES "groupchat_has_users" ("chat_id");

ALTER TABLE "users_entity" ADD FOREIGN KEY ("id") REFERENCES "message" ("id");

ALTER TABLE "chat" ADD FOREIGN KEY ("id") REFERENCES "message" ("chat_id");

ALTER TABLE "tag" ADD FOREIGN KEY ("id") REFERENCES "tag_ignore" ("ignored_tag_id");

ALTER TABLE "users_entity" ADD FOREIGN KEY ("id") REFERENCES "tag_ignore" ("user_id");

ALTER TABLE "tag" ADD FOREIGN KEY ("id") REFERENCES "tag_tracked" ("tracked_tag_id");

ALTER TABLE "users_entity" ADD FOREIGN KEY ("id") REFERENCES "tag_tracked" ("user_id");

ALTER TABLE "question" ADD FOREIGN KEY ("id") REFERENCES "votes_on_questions" ("question_id");

ALTER TABLE "users_entity" ADD FOREIGN KEY ("id") REFERENCES "votes_on_questions" ("user_id");

ALTER TABLE "question" ADD FOREIGN KEY ("id") REFERENCES "bookmarks" ("question_id");

ALTER TABLE "users_entity" ADD FOREIGN KEY ("id") REFERENCES "bookmarks" ("user_id");
