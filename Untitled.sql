CREATE TABLE "users" (
  "user_id" INTEGER PRIMARY KEY,
  "name" TEXT,
  "email" TEXT,
  "phone" TEXT,
  "password_hash" TEXT,
  "address" TEXT,
  "registration_date" TIMESTAMP
);

CREATE TABLE "pets" ("pet_id" INTEGER PRIMARY KEY,"user_id" INT,"name" TEXT, "species" TEXT, "breed" TEXT,"age" INT, "gender" TEXT);

CREATE TABLE "lost_pets" (
  "lost_id" INTEGER PRIMARY KEY,
  "pet_id" INT,
  "user_id" INT,
  "owner_name" TEXT,
  "owner_phone" TEXT,
  "owner_email" TEXT,
  "date_lost" DATE,
  "location_lost" TEXT,
  "description" TEXT,
  "status" TEXT
);

CREATE TABLE "found_pets" (
  "found_id" INTEGER PRIMARY KEY,
  "user_id" INT,
  "species" TEXT,
  "breed" TEXT,
  "color" TEXT,
  "date_found" DATE,
  "location_found" TEXT,
  "description" TEXT,
  "shelter_id" INT,
  "status" TEXT
);

CREATE TABLE "animal_shelters" (
  "shelter_id" INTEGER PRIMARY KEY,
  "name" TEXT,
  "address" TEXT,
  "phone" TEXT,
  "email" TEXT,
  "website" TEXT,
  "opening_hours" TEXT
);

CREATE TABLE "pet_special_marks" (
  "mark_id" INTEGER PRIMARY KEY,
  "pet_id" INT,
  "special_mark" TEXT
);

CREATE TABLE "pet_colors" (
  "color_id" INTEGER PRIMARY KEY,
  "pet_id" INT,
  "color" TEXT
);

CREATE TABLE "pet_photos" (
  "photo_id" INTEGER PRIMARY KEY,
  "pet_id" INT,
  "photo_url" TEXT,
  "uploaded_at" TIMESTAMP
);

CREATE TABLE "messages" (
  "message_id" INTEGER PRIMARY KEY,
  "sender_id" INT,
  "receiver_id" INT,
  "content" TEXT,
  "sent_at" TIMESTAMP
);

ALTER TABLE "pets" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "lost_pets" ADD FOREIGN KEY ("pet_id") REFERENCES "pets" ("pet_id");

ALTER TABLE "lost_pets" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "found_pets" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "found_pets" ADD FOREIGN KEY ("shelter_id") REFERENCES "animal_shelters" ("shelter_id");

ALTER TABLE "pet_special_marks" ADD FOREIGN KEY ("pet_id") REFERENCES "pets" ("pet_id");

ALTER TABLE "pet_colors" ADD FOREIGN KEY ("pet_id") REFERENCES "pets" ("pet_id");

ALTER TABLE "pet_photos" ADD FOREIGN KEY ("pet_id") REFERENCES "pets" ("pet_id");

ALTER TABLE "messages" ADD FOREIGN KEY ("sender_id") REFERENCES "users" ("user_id");

ALTER TABLE "messages" ADD FOREIGN KEY ("receiver_id") REFERENCES "users" ("user_id");
