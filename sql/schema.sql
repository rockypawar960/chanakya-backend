-- Create Database
CREATE DATABASE IF NOT EXISTS chanakya_db;
USE chanakya_db;

-- Roles Table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    CONSTRAINT uk_role_name UNIQUE (name)
);

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    class_or_year VARCHAR(100),
    stream VARCHAR(100),
    interests TEXT,
    strengths TEXT,
    challenges TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    CONSTRAINT uk_email UNIQUE (email),
    INDEX idx_email (email),
    INDEX idx_is_active (is_active)
);

-- User Roles Junction Table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Questions Table
CREATE TABLE IF NOT EXISTS questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_text TEXT NOT NULL,
    question_type VARCHAR(50) NOT NULL,
    sequence_number INT NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_sequence (sequence_number),
    INDEX idx_is_active (is_active)
);

-- Question Options Table
CREATE TABLE IF NOT EXISTS question_options (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    option_text TEXT NOT NULL,
    option_value INT NOT NULL,
    sequence_number INT NOT NULL,
    is_active BOOLEAN DEFAULT true,
    CONSTRAINT fk_option_question FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    INDEX idx_question_id (question_id),
    INDEX idx_is_active (is_active)
);

-- Careers Table
CREATE TABLE IF NOT EXISTS careers (
    id BIGINT AUTO_INCREMENT,

    name VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,

    popularity_score INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_careers PRIMARY KEY (id),
    CONSTRAINT uk_career_name UNIQUE (name),
    CONSTRAINT chk_popularity_score CHECK (popularity_score >= 0),

    INDEX idx_active_popularity (is_active, popularity_score)
);

-- Career Attributes Table
CREATE TABLE IF NOT EXISTS career_attributes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    career_id BIGINT NOT NULL,
    attribute_name VARCHAR(100) NOT NULL,
    weight INT NOT NULL,
    is_active BOOLEAN DEFAULT true,
    CONSTRAINT fk_attribute_career FOREIGN KEY (career_id) REFERENCES careers(id) ON DELETE CASCADE,
    INDEX idx_career_id (career_id)
);

-- Assessments Table
CREATE TABLE IF NOT EXISTS assessments (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    total_score INT NOT NULL,
    raw_responses LONGTEXT,
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_assessment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_completed_at (completed_at)
);

-- Recommendations Table
CREATE TABLE IF NOT EXISTS recommendations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    assessment_id BIGINT NOT NULL,
    career_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    match_percentage INT NOT NULL,
    recommendation_rank INT,
    reasoning TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recommendation_assessment FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE,
    CONSTRAINT fk_recommendation_career FOREIGN KEY (career_id) REFERENCES careers(id),
    CONSTRAINT fk_recommendation_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_assessment_id (assessment_id),
    INDEX idx_rank (recommendation_rank)
);

-- Learning Paths Table
--learning_paths (Parent Table)

 --Ye table overview store karega

CREATE TABLE learning_paths (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    career_id BIGINT NOT NULL,

    path_name VARCHAR(150) NOT NULL,
    description TEXT,
    duration_months INT,

    is_active BOOLEAN DEFAULT true,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_path_career
    FOREIGN KEY (career_id) REFERENCES careers(id) ON DELETE CASCADE,

    INDEX idx_career_id (career_id)
);

--************************learning_steps (Child Table)

-- Ye table actual steps / roadmap store karega

CREATE TABLE learning_steps (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    learning_path_id BIGINT NOT NULL,

    level ENUM('beginner','intermediate','advanced') NOT NULL,

    step_name VARCHAR(255) NOT NULL,
    description TEXT,

    video_link TEXT,
    task TEXT,

    step_order INT NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_step_path
    FOREIGN KEY (learning_path_id) REFERENCES learning_paths(id) ON DELETE CASCADE,

    INDEX idx_path_id (learning_path_id),
    INDEX idx_order (step_order));

    -- Tracker

CREATE TABLE user_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    user_id BIGINT NOT NULL,
    learning_step_id BIGINT NOT NULL,

    status ENUM('not_started','in_progress','completed') DEFAULT 'not_started',

    started_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_progress_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_progress_step
    FOREIGN KEY (learning_step_id) REFERENCES learning_steps(id) ON DELETE CASCADE,

    UNIQUE KEY unique_user_step (user_id, learning_step_id),

    INDEX idx_user (user_id),
    INDEX idx_step (learning_step_id)
);

--**************** steps completion track karne ke liye******************************
CREATE TABLE user_step_progress (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,

        user_id BIGINT NOT NULL,
        step_id BIGINT NOT NULL,

        completed BOOLEAN DEFAULT FALSE,

        completed_at TIMESTAMP NULL,

        UNIQUE KEY unique_user_step (user_id, step_id)
);

-- Resources Table
CREATE TABLE IF NOT EXISTS resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    career_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    resource_type VARCHAR(50) NOT NULL,
    url VARCHAR(500) NOT NULL,
    provider VARCHAR(100),
    difficulty VARCHAR(50),
    estimated_duration VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_resource_career FOREIGN KEY (career_id) REFERENCES careers(id) ON DELETE CASCADE,
    INDEX idx_career_id (career_id),
    INDEX idx_resource_type (resource_type)
);

-- Insert default roles
INSERT INTO roles (name, description) VALUES 
('ROLE_ADMIN', 'Administrator role'),
('ROLE_USER', 'User role');

-- Insert sample careers
INSERT INTO careers (name, description, required_skills, job_scope, popularity_score, is_active) VALUES
('Software Engineer', 'Develops software solutions and applications', 'Java,Python,JavaScript,Problem Solving', 'Backend, Frontend, Full Stack Development', 9, true),
('Data Scientist', 'Analyzes data and builds predictive models', 'Python,R,Machine Learning,Statistics', 'Analytics, ML Engineering, Data Engineering', 8, true),
('Product Manager', 'Manages product development and strategy', 'Leadership,Strategy,Analytics,Communication', 'Product Strategy, Roadmap Planning', 7, true),
('UI/UX Designer', 'Designs user interfaces and experiences', 'Design Thinking,Figma,User Research,Prototyping', 'Interface Design, User Experience', 7, true),
('DevOps Engineer', 'Manages infrastructure and deployment', 'Docker,Kubernetes,CI/CD,Cloud Platforms', 'Infrastructure, Deployment, Automation', 8, true);

-- Insert sample questions
INSERT INTO questions (question_text, question_type, sequence_number, is_active) VALUES
('How interested are you in solving complex problems?', 'RATING', 1, true),
('How comfortable are you with learning new technologies?', 'RATING', 2, true),
('Do you prefer working independently or in teams?', 'MULTIPLE_CHOICE', 3, true),
('What is your strongest subject?', 'MULTIPLE_CHOICE', 4, true),
('How interested are you in data and analytics?', 'RATING', 5, true),
('Do you enjoy creative design and aesthetics?', 'RATING', 6, true),
('How important is job stability to you?', 'RATING', 7, true),
('What is your preferred work environment?', 'MULTIPLE_CHOICE', 8, true);

-- Insert sample question options
INSERT INTO question_options (question_id, option_text, option_value, sequence_number, is_active) VALUES
(1, 'Not at all', 1, 1, true),
(1, 'Somewhat', 5, 2, true),
(1, 'Very much', 10, 3, true),
(2, 'Not comfortable', 1, 1, true),
(2, 'Fairly comfortable', 6, 2, true),
(2, 'Very comfortable', 10, 3, true),
(3, 'Independently', 5, 1, true),
(3, 'In teams', 8, 2, true),
(3, 'Both equally', 7, 3, true),
(4, 'Mathematics', 9, 1, true),
(4, 'Science', 8, 2, true),
(4, 'Arts/Literature', 4, 3, true),
(4, 'Social Studies', 5, 4, true),
(5, 'Not interested', 1, 1, true),
(5, 'Somewhat interested', 5, 2, true),
(5, 'Very interested', 10, 3, true),
(6, 'Not at all', 1, 1, true),
(6, 'Somewhat', 5, 2, true),
(6, 'Very much', 10, 3, true),
(7, 'Not important', 1, 1, true),
(7, 'Moderately important', 6, 2, true),
(7, 'Very important', 10, 3, true),
(8, 'Remote work', 8, 1, true),
(8, 'Office environment', 6, 2, true),
(8, 'Hybrid', 9, 3, true);

-- Insert sample learning paths
INSERT INTO learning_paths (career_id, path_name, description, skills, sequence_number, duration_months, is_active) VALUES
(1, 'Foundation Programming', 'Learn the basics of programming and data structures', 'Java,Python,OOP,Data Structures', 1, 3, true),
(1, 'Web Development', 'Master frontend and backend technologies', 'JavaScript,React,Node.js,REST APIs', 2, 4, true),
(1, 'Advanced Topics', 'System design and advanced concepts', 'System Design,Microservices,Architecture', 3, 3, true),
(2, 'Python for Data Science', 'Learn Python libraries for data analysis', 'Pandas,NumPy,Matplotlib,Scikit-learn', 1, 3, true),
(2, 'Machine Learning', 'Master ML algorithms and techniques', 'ML Algorithms,Deep Learning,TensorFlow', 2, 4, true);

-- Insert sample resources
INSERT INTO resources (career_id, title, description, resource_type, url, provider, difficulty, estimated_duration, is_active) VALUES
(1, 'The Complete Java Developer Course', 'Comprehensive Java programming course', 'COURSE', 'https://www.udemy.com/course/complete-java', 'Udemy', 'BEGINNER', '40 hours', true),
(1, 'System Design Interview Course', 'Learn system design patterns', 'COURSE', 'https://www.educative.io/courses/system-design', 'Educative', 'ADVANCED', '20 hours', true),
(2, 'Introduction to Machine Learning', 'ML fundamentals and applications', 'COURSE', 'https://www.coursera.org/learn/machine-learning', 'Coursera', 'INTERMEDIATE', '45 hours', true),
(3, 'Product Management Fundamentals', 'Learn PM basics and frameworks', 'COURSE', 'https://www.reforge.com/programs/product-management', 'Reforge', 'BEGINNER', '25 hours', true),
(4, 'UI/UX Design Course', 'Complete guide to modern design', 'COURSE', 'https://www.interaction-design.org/courses', 'Interaction Design Foundation', 'BEGINNER', '30 hours', true);
