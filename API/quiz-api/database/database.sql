CREATE DATABASE quiz_app;
USE quiz_app;

-- 1. Users
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100),
                       email VARCHAR(100) UNIQUE,
                       password VARCHAR(255),
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Subjects (Môn học)
CREATE TABLE subjects (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255),
                          description TEXT,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Exams (Đề thi)
CREATE TABLE exams (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       subject_id INT,
                       title VARCHAR(255),
                       description TEXT,
                       duration_minutes INT,
                       total_questions INT,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- 4. Questions
CREATE TABLE questions (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           exam_id INT,
                           question_text TEXT,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE
);

-- 5. Options
CREATE TABLE options (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         question_id INT,
                         option_text TEXT,
                         is_correct TINYINT(1) DEFAULT 0,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- 6. Exam Attempts (Lần làm bài)
CREATE TABLE exam_attempts (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT,
                               exam_id INT,
                               started_at DATETIME,
                               finished_at DATETIME,
                               score INT,
                               correct_count INT,
                               wrong_count INT,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE
);

-- 7. Exam Attempt Answers (Chi tiết bài làm)
CREATE TABLE exam_attempt_answers (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      exam_attempt_id INT,
                                      question_id INT,
                                      selected_option_id INT,
                                      is_correct TINYINT(1),
                                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (exam_attempt_id) REFERENCES exam_attempts(id) ON DELETE CASCADE,
                                      FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
                                      FOREIGN KEY (selected_option_id) REFERENCES options(id) ON DELETE CASCADE
);
