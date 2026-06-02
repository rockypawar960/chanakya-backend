-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: shuttle.proxy.rlwy.net    Database: railway
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin_activity_logs`
--

DROP TABLE IF EXISTS `admin_activity_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_activity_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `details` text,
  `entity_id` bigint DEFAULT NULL,
  `entity_type` varchar(255) NOT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `admin_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKitbisfifqj9fqo71et0bl1xob` (`admin_id`),
  CONSTRAINT `FKitbisfifqj9fqo71et0bl1xob` FOREIGN KEY (`admin_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_activity_logs`
--

LOCK TABLES `admin_activity_logs` WRITE;
/*!40000 ALTER TABLE `admin_activity_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_activity_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assessments`
--

DROP TABLE IF EXISTS `assessments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assessments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `total_score` int NOT NULL,
  `raw_responses` json DEFAULT NULL,
  `bucket_scores` json DEFAULT NULL,
  `completed_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  `score` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_completed_at` (`completed_at`),
  CONSTRAINT `fk_assessment_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessments`
--

LOCK TABLES `assessments` WRITE;
/*!40000 ALTER TABLE `assessments` DISABLE KEYS */;
INSERT INTO `assessments` VALUES (1,1,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-21 12:58:25','2026-02-21 18:28:25',1,NULL),(2,1,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-23 01:42:44','2026-02-23 07:12:43',1,NULL),(3,1,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 01:01:18','2026-02-27 06:31:18',1,NULL),(4,1,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 02:52:09','2026-02-27 08:22:09',1,NULL),(5,1,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 02:53:40','2026-02-27 08:23:39',1,NULL),(6,2,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 05:22:31','2026-02-27 10:52:30',1,NULL),(7,2,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 05:26:43','2026-02-27 10:56:43',1,NULL),(8,2,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 05:32:03','2026-02-27 11:02:03',1,NULL),(9,2,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 07:04:04','2026-02-27 12:34:04',1,NULL),(10,2,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 07:07:13','2026-02-27 12:37:12',1,NULL),(11,2,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 07:20:04','2026-02-27 12:50:04',1,NULL),(12,2,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 07:30:28','2026-02-27 13:00:28',1,NULL),(13,2,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 07:44:26','2026-02-27 13:14:26',1,NULL),(14,2,0,'{\"1\": 0, \"2\": 0, \"3\": 0, \"4\": 0, \"5\": 0, \"6\": 0, \"7\": 0, \"8\": 0}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 13:32:29','2026-02-27 19:02:28',1,NULL),(15,2,0,'{\"1\": 0, \"2\": 0, \"3\": 0, \"4\": 0, \"5\": 0, \"6\": 0, \"7\": 0, \"8\": 0}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 13:56:40','2026-02-27 19:26:40',1,NULL),(16,2,0,'{\"1\": null, \"2\": null, \"3\": null, \"4\": null, \"5\": null, \"6\": null, \"7\": null, \"8\": null}','{\"SOCIAL\": 0, \"BUSINESS\": 0, \"CREATIVE\": 0, \"TECHNICAL\": 0, \"ANALYTICAL\": 0}','2026-02-27 14:08:56','2026-02-27 19:38:55',1,NULL),(17,2,17,'{\"1\": 1, \"2\": 2, \"3\": 2, \"4\": 3, \"5\": 3, \"6\": 2, \"7\": 2, \"8\": 2}','{\"SOCIAL\": 5, \"BUSINESS\": 3, \"CREATIVE\": 3, \"TECHNICAL\": 2, \"ANALYTICAL\": 4}','2026-02-27 15:10:04','2026-02-27 20:40:03',1,NULL),(18,2,15,'{\"1\": 1, \"2\": 2, \"3\": 2, \"4\": 2, \"5\": 2, \"6\": 2, \"7\": 2, \"8\": 2}','{\"SOCIAL\": 4, \"BUSINESS\": 2, \"CREATIVE\": 3, \"TECHNICAL\": 2, \"ANALYTICAL\": 4}','2026-02-27 15:18:37','2026-02-27 20:48:37',1,NULL),(19,2,160,'{\"1\": \"6\", \"2\": \"10\", \"3\": \"14\", \"4\": \"18\", \"5\": \"22\", \"6\": \"26\", \"7\": \"30\", \"8\": \"34\"}','{\"SOCIAL\": 48, \"BUSINESS\": 18, \"CREATIVE\": 36, \"TECHNICAL\": 14, \"ANALYTICAL\": 44}','2026-04-19 23:05:15','2026-04-20 04:35:15',1,NULL),(20,2,161,'{\"1\": \"6\", \"2\": \"9\", \"3\": \"16\", \"4\": \"19\", \"5\": \"22\", \"6\": \"26\", \"7\": \"29\", \"8\": \"34\"}','{\"SOCIAL\": 48, \"BUSINESS\": 19, \"CREATIVE\": 35, \"TECHNICAL\": 16, \"ANALYTICAL\": 43}','2026-04-23 05:10:32','2026-04-23 10:40:32',1,NULL);
/*!40000 ALTER TABLE `assessments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buckets`
--

DROP TABLE IF EXISTS `buckets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buckets` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buckets`
--

LOCK TABLES `buckets` WRITE;
/*!40000 ALTER TABLE `buckets` DISABLE KEYS */;
INSERT INTO `buckets` VALUES (1,'CREATIVE',NULL),(2,'ANALYTICAL',NULL),(3,'SOCIAL',NULL),(4,'BUSINESS',NULL),(5,'TECHNICAL',NULL);
/*!40000 ALTER TABLE `buckets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `career_attributes`
--

DROP TABLE IF EXISTS `career_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `career_attributes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_active` bit(1) NOT NULL,
  `weight` int NOT NULL,
  `career_id` bigint NOT NULL,
  `bucket_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9uqngkum86w7kbkqsausjh18b` (`career_id`,`bucket_id`),
  KEY `fk_attribute_bucket` (`bucket_id`),
  CONSTRAINT `FK2pc8gise4clakmt6509amdhpw` FOREIGN KEY (`career_id`) REFERENCES `careers` (`id`),
  CONSTRAINT `fk_attribute_bucket` FOREIGN KEY (`bucket_id`) REFERENCES `buckets` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `career_attributes`
--

LOCK TABLES `career_attributes` WRITE;
/*!40000 ALTER TABLE `career_attributes` DISABLE KEYS */;
INSERT INTO `career_attributes` VALUES (1,_binary '',9,1,1),(2,_binary '',7,1,3),(3,_binary '',8,2,1),(4,_binary '',9,2,3),(5,_binary '',9,3,2),(6,_binary '',7,4,4),(7,_binary '',6,4,2),(8,_binary '',8,5,1),(9,_binary '',7,6,1),(10,_binary '',9,7,5),(11,_binary '',6,7,3),(12,_binary '',9,8,5),(13,_binary '',8,9,3),(14,_binary '',7,9,4),(15,_binary '',9,10,3),(16,_binary '',8,10,4),(17,_binary '',8,11,4),(18,_binary '',6,11,1),(19,_binary '',9,12,2),(20,_binary '',6,12,1),(21,_binary '',10,13,1),(22,_binary '',9,13,3),(23,_binary '',8,14,2),(24,_binary '',9,15,4),(25,_binary '',8,15,3),(26,_binary '',8,16,5),(27,_binary '',7,16,4),(28,_binary '',9,17,4),(29,_binary '',7,17,2),(30,_binary '',8,18,2),(31,_binary '',7,18,1),(32,_binary '',9,19,1),(33,_binary '',8,19,3),(34,_binary '',9,20,5),(35,_binary '',6,20,3);
/*!40000 ALTER TABLE `career_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `careers`
--

DROP TABLE IF EXISTS `careers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `careers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` text,
  `is_active` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `popularity_score` int DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_slwv6pp9gbsve3807gxo8dqty` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `careers`
--

LOCK TABLES `careers` WRITE;
/*!40000 ALTER TABLE `careers` DISABLE KEYS */;
INSERT INTO `careers` VALUES (1,'2026-02-27 06:08:45','Develops software applications and systems.',_binary '','Software Engineer',95,'2026-02-27 06:08:45'),(2,'2026-02-27 06:08:45','Analyzes data to extract insights and build predictive models.',_binary '','Data Scientist',92,'2026-02-27 06:08:45'),(3,'2026-02-27 06:08:45','Creates visual concepts and designs for branding and media.',_binary '','Graphic Designer',80,'2026-02-27 06:08:45'),(4,'2026-02-27 06:08:45','Promotes brands using digital channels and strategies.',_binary '','Digital Marketer',85,'2026-02-27 06:08:45'),(5,'2026-02-27 06:08:45','Designs and develops mechanical systems and machines.',_binary '','Mechanical Engineer',78,'2026-02-27 06:08:45'),(6,'2026-02-27 06:08:45','Plans and supervises construction projects and infrastructure.',_binary '','Civil Engineer',75,'2026-02-27 06:08:45'),(7,'2026-02-27 06:08:45','Diagnoses and treats medical conditions.',_binary '','Doctor',98,'2026-02-27 06:08:45'),(8,'2026-02-27 06:08:45','Studies human behavior and provides mental health support.',_binary '','Psychologist',82,'2026-02-27 06:08:45'),(9,'2026-02-27 06:08:45','Represents clients in legal matters and court proceedings.',_binary '','Lawyer',88,'2026-02-27 06:08:45'),(10,'2026-02-27 06:08:45','Manages financial accounts, audits, and taxation.',_binary '','Chartered Accountant',90,'2026-02-27 06:08:45'),(11,'2026-02-27 06:08:45','Leads product strategy and development lifecycle.',_binary '','Product Manager',87,'2026-02-27 06:08:45'),(12,'2026-02-27 06:08:45','Designs user-friendly digital interfaces and experiences.',_binary '','UI/UX Designer',84,'2026-02-27 06:08:45'),(13,'2026-02-27 06:08:45','Builds artificial intelligence and machine learning systems.',_binary '','AI Engineer',93,'2026-02-27 06:08:45'),(14,'2026-02-27 06:08:45','Creates engaging written content for various platforms.',_binary '','Content Writer',70,'2026-02-27 06:08:45'),(15,'2026-02-27 06:08:45','Manages financial assets and corporate investments.',_binary '','Investment Banker',89,'2026-02-27 06:08:45'),(16,'2026-02-27 06:08:45','Handles recruitment and employee management.',_binary '','HR Manager',76,'2026-02-27 06:08:45'),(17,'2026-02-27 06:08:45','Builds and manages new business ventures.',_binary '','Entrepreneur',91,'2026-02-27 06:08:45'),(18,'2026-02-27 06:08:45','Designs buildings and oversees construction planning.',_binary '','Architect',79,'2026-02-27 06:08:45'),(19,'2026-02-27 06:08:45','Protects systems and networks from cyber threats.',_binary '','Cybersecurity Analyst',94,'2026-02-27 06:08:45'),(20,'2026-02-27 06:08:45','Educates and mentors students in academic subjects.',_binary '','Teacher',83,'2026-02-27 06:08:45');
/*!40000 ALTER TABLE `careers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_response` text,
  `created_at` datetime(6) NOT NULL,
  `feedback_type` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `rating` int DEFAULT NULL,
  `resolved_at` datetime(6) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpwwmhguqianghvi1wohmtsm8l` (`user_id`),
  CONSTRAINT `FKpwwmhguqianghvi1wohmtsm8l` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learning_paths`
--

DROP TABLE IF EXISTS `learning_paths`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learning_paths` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `career_id` bigint NOT NULL,
  `path_name` varchar(150) NOT NULL,
  `description` text,
  `duration_months` int DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_career_id` (`career_id`),
  CONSTRAINT `fk_path_career` FOREIGN KEY (`career_id`) REFERENCES `careers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learning_paths`
--

LOCK TABLES `learning_paths` WRITE;
/*!40000 ALTER TABLE `learning_paths` DISABLE KEYS */;
INSERT INTO `learning_paths` VALUES (1,1,'Software Engineer Roadmap','Complete roadmap covering programming, DSA, system design and backend development',6,1,'2026-04-10 18:56:42','2026-04-10 18:56:42'),(2,2,'Data Scientist Roadmap','Learn data analysis, statistics, machine learning and real-world projects',6,1,'2026-04-10 18:56:42','2026-04-10 18:56:42'),(3,13,'AI Engineer Roadmap','Master machine learning, deep learning and AI model deployment',8,1,'2026-04-10 18:56:42','2026-04-10 18:56:42'),(4,1,'Software Engineer Career Roadmap','Become a job-ready software engineer with a strong foundation in technical and analytical skills',9,1,'2026-05-29 02:30:59','2026-05-29 02:30:59'),(5,7,'Doctor Career Roadmap','Become a skilled doctor with expertise in medical sciences and patient care',12,1,'2026-06-01 05:01:21','2026-06-01 05:01:21'),(6,7,'Doctor Career Roadmap','Become a licensed medical doctor with expertise in patient care and medical research',72,1,'2026-06-01 05:01:21','2026-06-01 05:01:21'),(7,19,'Cybersecurity Analyst Roadmap','Become a job-ready Cybersecurity Analyst with this 6-month roadmap',6,1,'2026-06-01 22:55:19','2026-06-01 22:55:19'),(8,19,'Cybersecurity Analyst Roadmap','Become a job-ready Cybersecurity Analyst with this 6-month learning roadmap',6,1,'2026-06-01 22:55:19','2026-06-01 22:55:19');
/*!40000 ALTER TABLE `learning_paths` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learning_steps`
--

DROP TABLE IF EXISTS `learning_steps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learning_steps` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `learning_path_id` bigint NOT NULL,
  `level` varchar(255) DEFAULT NULL,
  `step_name` varchar(255) NOT NULL,
  `description` text,
  `video_link` text,
  `task` text,
  `step_order` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_path_id` (`learning_path_id`),
  KEY `idx_order` (`step_order`),
  CONSTRAINT `fk_step_path` FOREIGN KEY (`learning_path_id`) REFERENCES `learning_paths` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learning_steps`
--

LOCK TABLES `learning_steps` WRITE;
/*!40000 ALTER TABLE `learning_steps` DISABLE KEYS */;
INSERT INTO `learning_steps` VALUES (1,1,'beginner','Programming Basics','Learn basics of programming using Java or Python','https://youtu.be/grEKMHGYyns','Solve 20 basic problems',1,'2026-04-10 18:58:51'),(2,1,'beginner','Data Structures','Understand arrays, linked lists, stacks and queues','https://youtu.be/RBSGKlAvoiM','Implement all DS in code',2,'2026-04-10 18:58:51'),(3,1,'intermediate','Algorithms','Learn sorting, searching and recursion','https://youtu.be/8hly31xKli0','Solve 50 LeetCode problems',3,'2026-04-10 18:58:51'),(4,1,'intermediate','Backend Development','Learn Spring Boot or Node.js basics','https://youtu.be/9SGDpanrc8U','Build REST API project',4,'2026-04-10 18:58:51'),(5,1,'advanced','System Design Basics','Learn scalability, load balancing and databases','https://youtu.be/xpDnVSmNFX0','Design URL shortener system',5,'2026-04-10 18:58:51'),(6,2,'beginner','Python for Data Science','Learn Python basics and libraries like pandas, numpy','https://youtu.be/rfscVS0vtbw','Analyze CSV dataset',1,'2026-04-10 18:59:11'),(7,2,'beginner','Statistics Basics','Understand mean, median, probability concepts','https://youtu.be/xxpc-HPKN28','Solve basic stats problems',2,'2026-04-10 18:59:11'),(8,2,'intermediate','Data Visualization','Use matplotlib and seaborn','https://youtu.be/3Xc3CA655Y4','Create charts from dataset',3,'2026-04-10 18:59:11'),(9,2,'intermediate','Machine Learning','Learn regression and classification models','https://youtu.be/GwIo3gDZCVQ','Build prediction model',4,'2026-04-10 18:59:11'),(10,2,'advanced','Projects','Work on real-world datasets and case studies','https://youtu.be/ua-CiDNNj30','Complete 2 end-to-end projects',5,'2026-04-10 18:59:11'),(11,3,'beginner','Python & Math Basics','Learn Python and linear algebra basics','https://youtu.be/rfscVS0vtbw','Solve basic math problems',1,'2026-04-10 18:59:49'),(12,3,'intermediate','Machine Learning','Understand ML algorithms and workflows','https://youtu.be/GwIo3gDZCVQ','Train ML model',2,'2026-04-10 18:59:49'),(13,3,'intermediate','Deep Learning','Learn neural networks and frameworks like TensorFlow','https://youtu.be/aircAruvnKk','Build neural network',3,'2026-04-10 18:59:49'),(14,3,'advanced','NLP / Computer Vision','Specialize in NLP or CV tasks','https://youtu.be/fOvTtapxa9c','Build chatbot or image classifier',4,'2026-04-10 18:59:49'),(15,3,'advanced','Deployment','Deploy models using APIs and cloud','https://youtu.be/ZtC4r9tJ7Zk','Deploy ML model on cloud',5,'2026-04-10 18:59:49'),(16,4,'beginner','Introduction to Programming','Learn the basics of programming using Python','https://www.youtube.com/results?search_query=python+programming+for+beginners','Complete 10 Python exercises on a coding platform like LeetCode or HackerRank',1,'2026-05-29 08:00:58'),(17,4,'beginner','Data Structures and Algorithms','Understand the fundamentals of data structures and algorithms','https://www.youtube.com/results?search_query=data+structures+and+algorithms+for+software+engineers','Implement a binary search algorithm and a stack data structure in Python',2,'2026-05-29 08:00:58'),(18,4,'intermediate','Object-Oriented Programming','Learn object-oriented programming concepts and apply them to real-world problems','https://www.youtube.com/results?search_query=object-oriented+programming+in+python','Design and implement a simple banking system using object-oriented programming principles',3,'2026-05-29 08:00:58'),(19,4,'intermediate','Database Management Systems','Understand the basics of database management systems and learn to interact with databases using SQL','https://www.youtube.com/results?search_query=database+management+systems+for+software+engineers','Design and implement a simple database schema for a e-commerce application and perform CRUD operations using SQL',4,'2026-05-29 08:00:58'),(20,4,'advanced','Software Design Patterns and Principles','Learn software design patterns and principles to write efficient and scalable code','https://www.youtube.com/results?search_query=software+design+patterns+and+principles','Implement a singleton design pattern and a factory method pattern in a real-world application',5,'2026-05-29 08:00:58'),(21,4,'advanced','Web Development Fundamentals','Learn the basics of web development using HTML, CSS, and JavaScript','https://www.youtube.com/results?search_query=web+development+fundamentals+for+software+engineers','Build a simple web application using HTML, CSS, and JavaScript that interacts with a backend API',6,'2026-05-29 08:00:58'),(22,4,'advanced','Testing and Deployment','Learn testing frameworks and deployment strategies for software applications','https://www.youtube.com/results?search_query=testing+and+deployment+strategies+for+software+engineers','Write unit tests and integration tests for a simple web application and deploy it to a cloud platform like AWS or Google Cloud',7,'2026-05-29 08:00:58'),(23,5,'beginner','Introduction to Human Anatomy','Understand the basic structure and functions of the human body','https://www.youtube.com/results?search_query=introduction+to+human+anatomy','Label and identify 10 major organs in the human body',1,'2026-06-01 10:31:20'),(24,5,'beginner','Medical Terminology','Learn the basic medical terms and vocabulary used in the medical field','https://www.youtube.com/results?search_query=medical+terminology+for+beginners','Define and use 20 common medical terms in a sentence',2,'2026-06-01 10:31:20'),(25,5,'intermediate','Pharmacology and Medications','Understand the basics of pharmacology and common medications used in medical practice','https://www.youtube.com/results?search_query=pharmacology+and+medications+for+doctors','Identify and describe the uses of 5 common medications',3,'2026-06-01 10:31:20'),(26,5,'intermediate','Clinical Skills and Patient Assessment','Develop clinical skills and learn to assess patients effectively','https://www.youtube.com/results?search_query=clinical+skills+and+patient+assessment','Conduct a mock patient assessment and document findings',4,'2026-06-01 10:31:20'),(27,5,'advanced','Medical Ethics and Law','Understand the ethical and legal aspects of medical practice','https://www.youtube.com/results?search_query=medical+ethics+and+law','Write a case study on a medical ethics dilemma and propose a solution',5,'2026-06-01 10:31:20'),(28,5,'advanced','Specialized Medical Topics','Explore specialized medical topics such as cardiology, neurology, or pediatrics','https://www.youtube.com/results?search_query=specialized+medical+topics','Research and present on a specialized medical topic of choice',6,'2026-06-01 10:31:20'),(29,5,'advanced','Clinical Practice and Case Management','Apply clinical skills and knowledge to manage patient cases effectively','https://www.youtube.com/results?search_query=clinical+practice+and+case+management','Develop a treatment plan for a complex patient case',7,'2026-06-01 10:31:20'),(30,6,'beginner','Introduction to Human Anatomy','Understand the basic structure and function of the human body','https://www.youtube.com/results?search_query=introduction+to+human+anatomy','Label and identify all major organs in the human body diagram',1,'2026-06-01 10:31:21'),(31,6,'beginner','Medical Terminology','Learn the fundamental language of medicine and healthcare','https://www.youtube.com/results?search_query=medical+terminology+for+beginners','Define and use 50 common medical terms in sentences',2,'2026-06-01 10:31:21'),(32,6,'intermediate','Physiology and Pharmacology','Study the functions of the human body and the effects of medications','https://www.youtube.com/results?search_query=physiology+and+pharmacology+for+medical+students','Explain the mechanism of action of 5 common medications',3,'2026-06-01 10:31:21'),(33,6,'intermediate','Clinical Skills and Patient Assessment','Develop essential skills for patient evaluation and diagnosis','https://www.youtube.com/results?search_query=clinical+skills+and+patient+assessment','Conduct a mock patient interview and write a patient history report',4,'2026-06-01 10:31:21'),(34,6,'advanced','Medical Research and Evidence-Based Practice','Learn to critically evaluate medical research and apply evidence-based practice','https://www.youtube.com/results?search_query=medical+research+and+evidence-based+practice','Critically evaluate a medical research article and write a summary',5,'2026-06-01 10:31:21'),(35,6,'advanced','Medical Ethics and Law','Understand the ethical and legal principles guiding medical practice','https://www.youtube.com/results?search_query=medical+ethics+and+law','Write a case study on a medical ethics dilemma and propose a solution',6,'2026-06-01 10:31:21'),(36,6,'advanced','Clinical Specializations and Subspecializations','Explore various medical specialties and subspecialties','https://www.youtube.com/results?search_query=medical+specialties+and+subspecialties','Research and write a report on a chosen medical specialty',7,'2026-06-01 10:31:21'),(37,6,'advanced','Licensure and Board Certification','Prepare for medical licensure and board certification exams','https://www.youtube.com/results?search_query=medical+licensure+and+board+certification','Create a study plan and practice questions for a medical licensure exam',8,'2026-06-01 10:31:21'),(38,7,'beginner','Introduction to Cybersecurity','Understand the basics of cybersecurity, threats, and vulnerabilities','https://www.youtube.com/results?search_query=introduction+to+cybersecurity','Complete a quiz on basic cybersecurity concepts with 80% accuracy',1,'2026-06-02 04:25:18'),(39,7,'beginner','Networking Fundamentals','Learn the basics of networking, including protocols and devices','https://www.youtube.com/results?search_query=networking+fundamentals','Configure a virtual network with 3 devices and 2 subnets',2,'2026-06-02 04:25:18'),(40,7,'intermediate','Operating System Security','Learn to secure Windows and Linux operating systems','https://www.youtube.com/results?search_query=operating+system+security','Configure a Windows and Linux system with basic security settings and pass a vulnerability scan',3,'2026-06-02 04:25:18'),(41,7,'intermediate','Cryptography and Access Control','Understand cryptography, access control, and identity management','https://www.youtube.com/results?search_query=cryptography+and+access+control','Implement a basic encryption scheme using a cryptographic library and authenticate 5 users',4,'2026-06-02 04:25:18'),(42,7,'advanced','Threat Analysis and Incident Response','Learn to analyze threats and respond to security incidents','https://www.youtube.com/results?search_query=threat+analysis+and+incident+response','Conduct a threat analysis on a given scenario and develop an incident response plan',5,'2026-06-02 04:25:18'),(43,7,'advanced','Security Frameworks and Compliance','Understand security frameworks and compliance regulations','https://www.youtube.com/results?search_query=security+frameworks+and+compliance','Develop a security plan for an organization using a security framework and ensure compliance with relevant regulations',6,'2026-06-02 04:25:18'),(44,7,'advanced','Penetration Testing and Vulnerability Assessment','Learn to conduct penetration testing and vulnerability assessments','https://www.youtube.com/results?search_query=penetration+testing+and+vulnerability+assessment','Conduct a penetration test on a given system and identify 5 vulnerabilities',7,'2026-06-02 04:25:18'),(45,8,'beginner','Introduction to Cybersecurity','Understand the fundamentals of cybersecurity, including security threats, vulnerabilities, and risk management','https://www.youtube.com/results?search_query=introduction+to+cybersecurity','Complete a quiz on basic cybersecurity concepts with 80% accuracy',1,'2026-06-02 04:25:19'),(46,8,'beginner','Networking Fundamentals','Learn the basics of computer networking, including protocols, devices, and architectures','https://www.youtube.com/results?search_query=networking+fundamentals','Configure a virtual network with 3 devices and test connectivity',2,'2026-06-02 04:25:19'),(47,8,'intermediate','Operating System Security','Understand how to secure operating systems, including Windows, Linux, and macOS','https://www.youtube.com/results?search_query=operating+system+security','Configure a secure user account on a virtual machine with password policies and access controls',3,'2026-06-02 04:25:19'),(48,8,'intermediate','Threat Analysis and Risk Management','Learn how to identify and analyze security threats, and develop risk management strategies','https://www.youtube.com/results?search_query=threat+analysis+and+risk+management','Conduct a threat analysis on a case study and develop a risk management plan with 5 recommendations',4,'2026-06-02 04:25:19'),(49,8,'advanced','Incident Response and Forensics','Understand how to respond to security incidents, and conduct digital forensics investigations','https://www.youtube.com/results?search_query=incident+response+and+forensics','Develop an incident response plan for a simulated security breach and conduct a forensic analysis on a virtual machine',5,'2026-06-02 04:25:19'),(50,8,'advanced','Security Frameworks and Compliance','Learn about security frameworks, such as NIST and ISO 27001, and understand compliance requirements','https://www.youtube.com/results?search_query=security+frameworks+and+compliance','Develop a security plan that aligns with a chosen security framework and includes 3 compliance requirements',6,'2026-06-02 04:25:19'),(51,8,'advanced','Cybersecurity Tools and Technologies','Learn about various cybersecurity tools and technologies, including firewalls, intrusion detection systems, and encryption','https://www.youtube.com/results?search_query=cybersecurity+tools+and+technologies','Configure and test 2 cybersecurity tools, such as a firewall and an intrusion detection system',7,'2026-06-02 04:25:19');
/*!40000 ALTER TABLE `learning_steps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_tokens`
--

DROP TABLE IF EXISTS `password_reset_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `expiry_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_tokens`
--

LOCK TABLES `password_reset_tokens` WRITE;
/*!40000 ALTER TABLE `password_reset_tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_options`
--

DROP TABLE IF EXISTS `question_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_options` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_active` bit(1) NOT NULL,
  `option_text` text NOT NULL,
  `option_value` int NOT NULL,
  `sequence_number` int NOT NULL,
  `question_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsb9v00wdrgc9qojtjkv7e1gkp` (`question_id`),
  CONSTRAINT `FKsb9v00wdrgc9qojtjkv7e1gkp` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_options`
--

LOCK TABLES `question_options` WRITE;
/*!40000 ALTER TABLE `question_options` DISABLE KEYS */;
INSERT INTO `question_options` VALUES (5,_binary '','Creating designs, art, or original content',1,1,1),(6,_binary '','Solving logical puzzles or analytical problems',2,2,1),(7,_binary '','Interacting and engaging with people',3,3,1),(8,_binary '','Thinking about business ideas or strategies',4,4,1),(9,_binary '','Developing innovative and creative solutions',1,1,2),(10,_binary '','Breaking the problem down logically step-by-step',2,2,2),(11,_binary '','Collaborating with a team to solve it',3,3,2),(12,_binary '','Evaluating its market or practical impact',4,4,2),(13,_binary '','Designing or presenting ideas creatively',1,1,3),(14,_binary '','Analyzing data or working with numbers',2,2,3),(15,_binary '','Organizing events or coordinating people',3,3,3),(16,_binary '','Working with software or technical tools',5,4,3),(17,_binary '','Flexible and experimental environment',1,1,4),(18,_binary '','Structured and rule-based environment',2,2,4),(19,_binary '','Highly collaborative team environment',3,3,4),(20,_binary '','Target-driven and competitive environment',4,4,4),(21,_binary '','Idea generator or creative designer',1,1,5),(22,_binary '','Analyst or problem solver',2,2,5),(23,_binary '','Leader or communicator',3,3,5),(24,_binary '','Strategist or business planner',4,4,5),(25,_binary '','Building or creating something new',1,1,6),(26,_binary '','Cracking complex logical challenges',2,2,6),(27,_binary '','Helping and guiding people',3,3,6),(28,_binary '','Optimizing systems or technical processes',5,4,6),(29,_binary '','Working in a creative industry',1,1,7),(30,_binary '','Working in research or analytical roles',2,2,7),(31,_binary '','Working in public-facing or networking roles',3,3,7),(32,_binary '','Building or managing a business/startup',4,4,7),(33,_binary '','Experimenting with new ideas freely',1,1,8),(34,_binary '','Following clearly defined plans and structures',2,2,8),(35,_binary '','Collaborating closely with teams',3,3,8),(36,_binary '','Working deeply with technology and systems',5,4,8);
/*!40000 ALTER TABLE `question_options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` bit(1) NOT NULL,
  `question_text` text NOT NULL,
  `question_type` varchar(255) NOT NULL,
  `sequence_number` int NOT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bucket_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2w07mo7fsy6tn4rk83w4dj7bj` (`bucket_id`),
  CONSTRAINT `FK2w07mo7fsy6tn4rk83w4dj7bj` FOREIGN KEY (`bucket_id`) REFERENCES `buckets` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'2026-02-19 12:13:48',_binary '','What do you naturally enjoy doing in your free time?','SINGLE_CHOICE',1,'2026-02-21 18:18:21',1),(2,'2026-02-19 12:13:48',_binary '','When faced with a challenging problem, how do you usually approach it?','SINGLE_CHOICE',2,'2026-02-21 18:18:21',2),(3,'2026-02-19 12:13:48',_binary '','Which type of task excites you the most?','SINGLE_CHOICE',3,'2026-02-21 18:18:21',5),(4,'2026-02-19 12:13:48',_binary '','In what kind of work environment do you feel most productive?','SINGLE_CHOICE',4,'2026-02-21 18:18:21',4),(5,'2026-02-19 12:13:48',_binary '','If assigned a group project, which role would you prefer?','SINGLE_CHOICE',5,'2026-02-21 18:18:21',3),(6,'2026-02-19 12:13:48',_binary '','Which activity gives you the greatest sense of satisfaction?','SINGLE_CHOICE',6,'2026-02-21 18:18:21',3),(7,'2026-02-19 12:13:48',_binary '','What kind of future career sounds most exciting to you?','SINGLE_CHOICE',7,'2026-02-21 18:18:21',1),(8,'2026-02-19 12:13:48',_binary '','Do you prefer structured planning or experimenting with new ideas?','SINGLE_CHOICE',8,'2026-02-21 18:18:21',2);
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recommendation_weight_config`
--

DROP TABLE IF EXISTS `recommendation_weight_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recommendation_weight_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bucket_name` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` text,
  `is_active` bit(1) NOT NULL,
  `threshold_score` double DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `weight` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recommendation_weight_config`
--

LOCK TABLES `recommendation_weight_config` WRITE;
/*!40000 ALTER TABLE `recommendation_weight_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `recommendation_weight_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recommendations`
--

DROP TABLE IF EXISTS `recommendations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recommendations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_active` bit(1) DEFAULT NULL,
  `match_score` double DEFAULT NULL,
  `reasoning` varchar(1000) DEFAULT NULL,
  `assessment_id` bigint DEFAULT NULL,
  `career_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `version` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FKtib1pb8ufmbfoeafwqrp6ultp` (`career_id`),
  KEY `FK3c9w1lipqdutm65a9inevwfp0` (`user_id`),
  KEY `fk_recommendation_assessment` (`assessment_id`),
  CONSTRAINT `FK3c9w1lipqdutm65a9inevwfp0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_recommendation_assessment` FOREIGN KEY (`assessment_id`) REFERENCES `assessments` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKtib1pb8ufmbfoeafwqrp6ultp` FOREIGN KEY (`career_id`) REFERENCES `careers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recommendations`
--

LOCK TABLES `recommendations` WRITE;
/*!40000 ALTER TABLE `recommendations` DISABLE KEYS */;
INSERT INTO `recommendations` VALUES (4,_binary '',40.666666666666664,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',17,9,2,0),(5,_binary '',40.588235294117645,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',17,2,2,0),(6,_binary '',40.588235294117645,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',17,10,2,0),(25,_binary '',40,'Based on your strengths in SOCIAL, BUSINESS, CREATIVE, TECHNICAL, ANALYTICAL',18,3,2,0),(26,_binary '',40,'Based on your strengths in SOCIAL, BUSINESS, CREATIVE, TECHNICAL, ANALYTICAL',18,14,2,0),(27,_binary '',36,'Based on your strengths in SOCIAL, BUSINESS, CREATIVE, TECHNICAL, ANALYTICAL',18,12,2,0),(34,_binary '',100,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',19,7,2,0),(35,_binary '',100,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',19,1,2,0),(36,_binary '',100,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',19,19,2,0),(37,_binary '',100,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',20,7,2,0),(38,_binary '',100,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',20,1,2,0),(39,_binary '',100,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',20,19,2,0),(40,_binary '',100,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',20,7,2,0),(41,_binary '',100,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',20,1,2,0),(42,_binary '',100,'Based on your strengths in SOCIAL, CREATIVE, BUSINESS, TECHNICAL, ANALYTICAL',20,19,2,0);
/*!40000 ALTER TABLE `recommendations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resources`
--

DROP TABLE IF EXISTS `resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resources` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` text,
  `difficulty` varchar(255) DEFAULT NULL,
  `estimated_duration` varchar(255) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `resource_type` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `career_id` bigint DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `skill` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8bep09me4nt4euirp4ehs5bju` (`career_id`),
  CONSTRAINT `FK8bep09me4nt4euirp4ehs5bju` FOREIGN KEY (`career_id`) REFERENCES `careers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resources`
--

LOCK TABLES `resources` WRITE;
/*!40000 ALTER TABLE `resources` DISABLE KEYS */;
INSERT INTO `resources` VALUES (1,'2026-05-30 04:41:35.302409','Learn the basics of Java programming, including data types, variables, operators, control structures, functions, and object-oriented programming concepts','BEGINNER','20 hours',_binary '','Apna College','PLAYLIST','Java Programming Tutorial for Beginners','2026-05-30 04:41:35.302409','https://www.youtube.com/playlist?list=PLu0W_9lII9agS67Uits0UnJhg9vc6wEB8',1,'Hindi','Java'),(2,'2026-05-30 04:41:35.349309','Learn the fundamentals of Java programming, including syntax, data types, operators, control structures, functions, and object-oriented programming concepts','BEGINNER','15 hours',_binary '','freeCodeCamp','PLAYLIST','Java Tutorial for Beginners','2026-05-30 04:41:35.349309','https://www.youtube.com/playlist?list=PLWKjhJtqVAblfum5WiQXTBWSGbzySLG3',1,'English','Java'),(3,'2026-05-30 04:41:35.357724','Learn the basics of Java programming, including data types, variables, operators, control structures, functions, and object-oriented programming concepts','BEGINNER','4 weeks',_binary '','Coursera','COURSE','Java Programming Course','2026-05-30 04:41:35.357724','https://www.coursera.org/learn/java-programming',1,'English','Java'),(4,'2026-05-30 04:41:35.359735','Learn the basics of Java programming, including data types, variables, operators, control structures, functions, and object-oriented programming concepts','BEGINNER','10 hours',_binary '','YouTube','PLAYLIST','Java Tutorial by Kunal Kushwaha','2026-05-30 04:41:35.359735','https://www.youtube.com/playlist?list=PL9C4B0X5XwZ8j1y3QxohQ5vBZ_mV4Tf5',1,'Hindi','Java'),(5,'2026-05-30 04:41:35.359735','Official Java documentation, including tutorials, guides, and reference materials','BEGINNER','Self-paced',_binary '','Oracle','ARTICLE','Java Documentation','2026-05-30 04:41:35.359735','https://docs.oracle.com/javase/tutorial/',1,'English','Java'),(6,'2026-05-30 04:41:35.366054','Practice solving Java problems on LeetCode, including algorithms, data structures, and object-oriented programming concepts','BEGINNER','Self-paced',_binary '','LeetCode','PLATFORM','LeetCode Java Problems','2026-05-30 04:41:35.366054','https://leetcode.com/problemset/all/',1,'Both','Java'),(7,'2026-05-30 04:41:35.370801','Learn the basics of Java programming, including data types, variables, operators, control structures, functions, and object-oriented programming concepts','BEGINNER','15 hours',_binary '','YouTube','PLAYLIST','Java Programming by Telusko','2026-05-30 04:41:35.370801','https://www.youtube.com/playlist?list=PLsyeobzWxl7q2eaU3j7X9tYxrHIigAA7S',1,'Hindi','Java'),(8,'2026-05-30 04:41:35.372598','Learn the basics of Java programming, including data types, variables, operators, control structures, functions, and object-oriented programming concepts','BEGINNER','12 weeks',_binary '','NPTEL','COURSE','Introduction to Java Programming by NPTEL','2026-05-30 04:41:35.372598','https://nptel.ac.in/courses/106/102/106102002/',1,'English','Java'),(9,'2026-05-30 05:11:24.788282','Learn the basics of Python programming, including data types, functions, and control structures, in this comprehensive Hindi tutorial series by CodeWithHarry','BEGINNER','10 hours',_binary '','YouTube','PLAYLIST','Python Tutorial for Beginners','2026-05-30 05:11:24.788282','https://www.youtube.com/results?search_query=python+tutorial+hindi+codewithharry+2024',2,'Hindi','Python'),(10,'2026-05-30 05:11:24.831003','Get started with Python programming and learn the fundamentals, including data structures, file input/output, and object-oriented programming, in this free English course by freeCodeCamp','BEGINNER','12 hours',_binary '','YouTube','VIDEO','Python Full Course','2026-05-30 05:11:24.831003','https://www.youtube.com/results?search_query=python+full+course+freeCodeCamp+english',2,'English','Python'),(11,'2026-05-30 05:11:24.836002','Learn the basics of Python programming, including variables, data types, and control structures, in this free course by Coursera','BEGINNER','4 weeks',_binary '','Coursera','COURSE','Python Programming Course','2026-05-30 05:11:24.836002','https://www.coursera.org/search?query=Python&price=free',2,'English','Python'),(12,'2026-05-30 05:11:24.838011','Practice your Python skills with a wide range of problems, from basic to advanced, on LeetCode','BEGINNER','5 hours',_binary '','LeetCode','PRACTICE','Python Practice Problems','2026-05-30 05:11:24.838011','https://leetcode.com/problemset/?search=Python',2,'English','Python'),(13,'2026-05-30 05:11:24.842128','Get official documentation for Python, including tutorials, guides, and reference materials, on the official Python website','BEGINNER','2 hours',_binary '','Official Docs','DOCUMENTATION','Python Documentation','2026-05-30 05:11:24.842128','https://www.google.com/search?q=Python+official+documentation',2,'English','Python'),(14,'2026-05-30 05:11:24.843683','Learn the basics of Python programming, including data types, functions, and control structures, in this comprehensive Hindi tutorial series by Apna College','BEGINNER','8 hours',_binary '','YouTube','PLAYLIST','Python Tutorial for Beginners in Hindi','2026-05-30 05:11:24.843683','https://www.youtube.com/results?search_query=python+tutorial+hindi+apna+college+2024',2,'Hindi','Python'),(15,'2026-05-30 05:11:24.846321','Get a structured guide to learning Python, including the best resources, tutorials, and projects, on roadmap.sh','BEGINNER','1 hour',_binary '','roadmap.sh','ROADMAP','Python Roadmap','2026-05-30 05:11:24.846321','https://roadmap.sh/python',2,'English','Python'),(16,'2026-05-30 05:21:59.672219','Learn the basics of MongoDB, including data modeling, queries, and data retrieval, in this comprehensive tutorial series','BEGINNER','10 hours',_binary '','YouTube','PLAYLIST','MongoDB Tutorial for Beginners','2026-05-30 05:21:59.672219','https://www.youtube.com/results?search_query=mongodb+tutorial+hindi+codewithharry+2024',2,'Hindi','MONGO DB'),(17,'2026-05-30 05:21:59.757041','Get started with MongoDB and learn how to build scalable and efficient databases with this free course','BEGINNER','5 hours',_binary '','YouTube','VIDEO','MongoDB Crash Course','2026-05-30 05:21:59.757041','https://www.youtube.com/results?search_query=mongodb+crash+course+freeCodeCamp+english',2,'English','MONGO DB'),(18,'2026-05-30 05:21:59.762135','Learn the fundamentals of MongoDB, including data types, queries, and indexing, with this free online course','BEGINNER','4 hours',_binary '','Coursera','COURSE','MongoDB Basics','2026-05-30 05:21:59.762135','https://www.coursera.org/search?query=MONGO+DB&price=free',2,'Both','MONGO DB'),(19,'2026-05-30 05:21:59.764770','Learn how to develop scalable and efficient MongoDB applications with this free course from IBM','BEGINNER','12 hours',_binary '','IBM SkillsBuild','COURSE','IBM MongoDB Developer Course','2026-05-30 05:21:59.764770','https://skillsbuild.org/students/course-catalog?search=MONGO+DB',2,'English','MONGO DB'),(20,'2026-05-30 05:21:59.772426','Practice your MongoDB skills with these interactive exercises and quizzes','BEGINNER','5 hours',_binary '','LeetCode','PRACTICE','MongoDB Practice Exercises','2026-05-30 05:21:59.772426','https://leetcode.com/problemset/?search=MONGO+DB',2,'Both','MONGO DB'),(21,'2026-05-30 05:21:59.776061','Get the most up-to-date and accurate information on MongoDB with the official documentation','BEGINNER','10 hours',_binary '','Official Docs','DOCUMENTATION','MongoDB Official Documentation','2026-05-30 05:21:59.776061','https://www.google.com/search?q=MONGO+DB+official+documentation',2,'Both','MONGO DB'),(22,'2026-05-30 05:21:59.779258','Learn MongoDB from scratch with this comprehensive tutorial series in Hindi','BEGINNER','8 hours',_binary '','YouTube','PLAYLIST','MongoDB Tutorial by Apna College','2026-05-30 05:21:59.779258','https://www.youtube.com/results?search_query=mongodb+tutorial+hindi+apna+college+2024',2,'Hindi','MONGO DB'),(23,'2026-05-30 05:21:59.782660','Get a structured learning path for MongoDB with this free roadmap','BEGINNER','5 hours',_binary '','roadmap.sh','ROADMAP','MongoDB Roadmap','2026-05-30 05:21:59.782660','https://roadmap.sh/mongo-db',2,'Both','MONGO DB'),(24,'2026-06-01 13:11:21.440177','Learn the basics of Spring Boot and how to build web applications with this tutorial series. This resource covers the fundamentals of Spring Boot, including configuration, dependency injection, and more.','BEGINNER','10 hours',_binary '','YouTube','PLAYLIST','Spring Boot Tutorial for Beginners','2026-06-01 13:11:21.440177','https://www.youtube.com/results?search_query=spring+boot+tutorial+hindi+codewithharry+2024',NULL,'Hindi','SpringBoot'),(25,'2026-06-01 13:11:21.471539','This comprehensive course covers everything you need to know to get started with Spring Boot, from setting up your development environment to building and deploying a real-world application.','BEGINNER','20 hours',_binary '','YouTube','VIDEO','Spring Boot Full Course','2026-06-01 13:11:21.471539','https://www.youtube.com/results?search_query=spring+boot+full+course+freeCodeCamp+english',NULL,'English','SpringBoot'),(26,'2026-06-01 13:11:21.473547','This free course covers the basics of Spring Boot and how to use it to build web applications. It includes hands-on exercises and projects to help you practice your skills.','BEGINNER','4 weeks',_binary '','Coursera','COURSE','Spring Boot Course','2026-06-01 13:11:21.473547','https://www.coursera.org/search?query=SpringBoot&price=free',NULL,'English','SpringBoot'),(27,'2026-06-01 13:11:21.478975','This tutorial series from IBM covers the basics of Spring Boot and how to use it to build cloud-native applications. It includes hands-on exercises and projects to help you practice your skills.','BEGINNER','5 hours',_binary '','IBM SkillsBuild','COURSE','IBM Spring Boot Tutorial','2026-06-01 13:11:21.478975','https://skillsbuild.org/students/course-catalog?search=SpringBoot',NULL,'English','SpringBoot'),(28,'2026-06-01 13:11:21.478975','Practice your Spring Boot skills with this platform, which includes a variety of exercises and projects to help you improve your coding abilities.','BEGINNER','5 hours',_binary '','LeetCode','PRACTICE','Spring Boot Practice','2026-06-01 13:11:21.478975','https://leetcode.com/problemset/?search=SpringBoot',NULL,'Both','SpringBoot'),(29,'2026-06-01 13:11:21.484791','The official Spring Boot documentation, which includes a comprehensive guide to getting started with the framework, as well as reference materials and tutorials.','BEGINNER','2 hours',_binary '','Official Docs','DOCUMENTATION','Spring Boot Documentation','2026-06-01 13:11:21.484791','https://www.google.com/search?q=SpringBoot+official+documentation',NULL,'English','SpringBoot'),(30,'2026-06-01 13:11:21.486191','A structured learning guide to help you get started with Spring Boot, including a roadmap of the skills and knowledge you need to acquire.','BEGINNER','1 hour',_binary '','roadmap.sh','ROADMAP','Spring Boot Roadmap','2026-06-01 13:11:21.486191','https://roadmap.sh/springboot',NULL,'Both','SpringBoot'),(31,'2026-06-01 13:11:21.490830','This free course from TCS covers the basics of Spring Boot and how to use it to build web applications. It includes hands-on exercises and projects to help you practice your skills.','BEGINNER','4 weeks',_binary '','TCS iON','COURSE','TCS Spring Boot Course','2026-06-01 13:11:21.490830','https://learning.tcsionhub.in/hub/search?q=SpringBoot',NULL,'English','SpringBoot'),(32,'2026-06-01 13:12:08.977642','Learn the basics of Spring Boot and how to build web applications with this tutorial, covering topics such as dependency injection and RESTful APIs. This resource is ideal for beginners who want to get started with Spring Boot development.','BEGINNER','10 hours',_binary '','YouTube','PLAYLIST','Spring Boot Tutorial for Beginners','2026-06-01 13:12:08.977642','https://www.youtube.com/results?search_query=spring+boot+tutorial+hindi+codewithharry+2024',NULL,'Hindi','SpringBoot course by Infosys'),(33,'2026-06-01 13:12:08.981151','This comprehensive course covers all aspects of Spring Boot, including configuration, security, and deployment. It\'s perfect for beginners who want to learn Spring Boot from scratch and become proficient in building enterprise-level applications.','BEGINNER','20 hours',_binary '','YouTube','PLAYLIST','Spring Boot Full Course','2026-06-01 13:12:08.981151','https://www.youtube.com/results?search_query=spring+boot+full+course+freeCodeCamp+english',NULL,'English','SpringBoot course by Infosys'),(34,'2026-06-01 13:12:08.988775','Learn Spring Boot from Infosys, one of the leading IT companies in the world, and gain hands-on experience in building web applications. This course covers the fundamentals of Spring Boot and provides a comprehensive understanding of the framework.','BEGINNER','15 hours',_binary '','Infosys Springboard','COURSE','Spring Boot Course by Infosys','2026-06-01 13:12:08.988775','https://infyspringboard.onwingspan.com/web/en/search?q=SpringBoot+course+by+Infosys',NULL,'Both','SpringBoot course by Infosys'),(35,'2026-06-01 13:12:08.991327','Practice your Spring Boot skills with these problems and projects, covering topics such as RESTful APIs, database integration, and security. This resource helps beginners to apply their knowledge and gain practical experience in building Spring Boot applications.','BEGINNER','5 hours',_binary '','LeetCode','PRACTICE','Spring Boot Practice Problems','2026-06-01 13:12:08.991327','https://leetcode.com/problemset/?search=SpringBoot+course+by+Infosys',NULL,'Both','SpringBoot course by Infosys'),(36,'2026-06-01 13:12:08.993328','Get the official documentation for Spring Boot, covering all aspects of the framework, including configuration, security, and deployment. This resource is essential for beginners who want to learn Spring Boot and become proficient in building web applications.','BEGINNER','5 hours',_binary '','Official Docs','DOCUMENTATION','Official Spring Boot Documentation','2026-06-01 13:12:08.993328','https://www.google.com/search?q=SpringBoot+course+by+Infosys+official+documentation',NULL,'English','SpringBoot course by Infosys'),(37,'2026-06-01 13:12:08.995329','Follow this roadmap to learn Spring Boot, covering topics such as dependency injection, RESTful APIs, and security. This resource provides a structured approach to learning Spring Boot and helps beginners to stay on track and achieve their goals.','BEGINNER','10 hours',_binary '','roadmap.sh','ROADMAP','Spring Boot Roadmap','2026-06-01 13:12:08.995329','https://roadmap.sh/springboot-course-by-infosys',NULL,'Both','SpringBoot course by Infosys'),(38,'2026-06-01 13:12:08.997349','Learn Spring Boot from IBM, one of the leading IT companies in the world, and gain hands-on experience in building web applications. This course covers the fundamentals of Spring Boot and provides a comprehensive understanding of the framework.','BEGINNER','10 hours',_binary '','IBM SkillsBuild','COURSE','IBM Spring Boot Course','2026-06-01 13:12:08.997349','https://skillsbuild.org/students/course-catalog?search=SpringBoot+course+by+Infosys',NULL,'English','SpringBoot course by Infosys'),(39,'2026-06-01 13:12:09.002340','Learn Spring Boot with this tutorial, covering topics such as configuration, security, and deployment. This resource is ideal for beginners who want to get started with Spring Boot development and build web applications.','BEGINNER','15 hours',_binary '','YouTube','VIDEO','Spring Boot Tutorial by Telusko','2026-06-01 13:12:09.002340','https://www.youtube.com/results?search_query=spring+boot+tutorial+telusko+english',NULL,'English','SpringBoot course by Infosys'),(40,'2026-06-02 04:42:02.889874','Learn the basics of React and how to build simple applications with this tutorial series by CodeWithHarry','BEGINNER','10 hours',_binary '','YouTube','PLAYLIST','React Tutorial for Beginners','2026-06-02 04:42:02.889874','https://www.youtube.com/results?search_query=react+tutorial+hindi+codewithharry+2024',NULL,'Hindi','react'),(41,'2026-06-02 04:42:02.950825','Learn React from scratch with this comprehensive course by freeCodeCamp','BEGINNER','20 hours',_binary '','YouTube','PLAYLIST','React Full Course','2026-06-02 04:42:02.950825','https://www.youtube.com/results?search_query=react+full+course+freeCodeCamp+english',NULL,'English','react'),(42,'2026-06-02 04:42:02.950825','Learn the fundamentals of React with this free course on Coursera','BEGINNER','4 hours',_binary '','Coursera','COURSE','React Basics','2026-06-02 04:42:02.950825','https://www.coursera.org/search?query=react&price=free',NULL,'English','react'),(43,'2026-06-02 04:42:02.950825','Learn React and become a certified developer with this course by IBM SkillsBuild','BEGINNER','15 hours',_binary '','IBM SkillsBuild','COURSE','React Developer Course','2026-06-02 04:42:02.950825','https://skillsbuild.org/students/course-catalog?search=react',NULL,'English','react'),(44,'2026-06-02 04:42:02.950825','Practice your React skills with this set of exercises on LeetCode','BEGINNER','5 hours',_binary '','LeetCode','PRACTICE','React Practice','2026-06-02 04:42:02.950825','https://leetcode.com/problemset/?search=react',NULL,'Both','react'),(45,'2026-06-02 04:42:02.950825','Learn React with the official documentation and guides','BEGINNER','10 hours',_binary '','Official Docs','DOCUMENTATION','React Documentation','2026-06-02 04:42:02.950825','https://www.google.com/search?q=react+official+documentation',NULL,'English','react'),(46,'2026-06-02 04:42:02.964542','Follow this structured learning guide to become a React expert','BEGINNER','20 hours',_binary '','roadmap.sh','ROADMAP','React Roadmap','2026-06-02 04:42:02.964542','https://roadmap.sh/react',NULL,'Both','react'),(47,'2026-06-02 04:42:02.973751','Learn React with this tutorial series by Telusko, covering the basics and advanced topics','BEGINNER','15 hours',_binary '','YouTube','VIDEO','React Tutorial by Telusko','2026-06-02 04:42:02.973751','https://www.youtube.com/results?search_query=react+tutorial+telusko+english',NULL,'English','react');
/*!40000 ALTER TABLE `resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Standard user role','ROLE_USER'),(2,'Standard Admin role','ROLE_ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_learning_paths`
--

DROP TABLE IF EXISTS `user_learning_paths`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_learning_paths` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `is_current` bit(1) NOT NULL,
  `last_accessed_at` datetime(6) DEFAULT NULL,
  `progress_percentage` int NOT NULL,
  `started_at` datetime(6) NOT NULL,
  `status` enum('ACTIVE','COMPLETED','PAUSED','DROPPED') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `learning_path_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKit1euswme9jb1ovvbnnlqkio0` (`user_id`,`learning_path_id`),
  KEY `FKmiojwmyx1pky64n2xvq7c79xt` (`learning_path_id`),
  CONSTRAINT `FKmiojwmyx1pky64n2xvq7c79xt` FOREIGN KEY (`learning_path_id`) REFERENCES `learning_paths` (`id`),
  CONSTRAINT `FKsxsenulcb6ncvq4v0dqg9xg4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_learning_paths`
--

LOCK TABLES `user_learning_paths` WRITE;
/*!40000 ALTER TABLE `user_learning_paths` DISABLE KEYS */;
INSERT INTO `user_learning_paths` VALUES (1,NULL,'2026-06-01 09:46:38.433898',_binary '\0','2026-06-01 09:46:38.433898',0,'2026-06-01 09:46:38.433898','ACTIVE','2026-06-01 09:46:38.433898',1,2),(2,NULL,'2026-06-01 10:31:20.963369',_binary '\0','2026-06-01 10:31:20.963369',0,'2026-06-01 10:31:20.963369','ACTIVE','2026-06-01 10:31:20.963369',5,2),(3,NULL,'2026-06-01 10:31:21.136652',_binary '\0','2026-06-01 10:31:21.136652',0,'2026-06-01 10:31:21.136652','ACTIVE','2026-06-01 10:31:21.136652',6,2),(4,NULL,'2026-06-02 04:25:18.981593',_binary '\0','2026-06-02 04:25:18.981593',0,'2026-06-02 04:25:18.981593','ACTIVE','2026-06-02 04:25:18.981593',7,2),(5,NULL,'2026-06-02 04:25:19.075460',_binary '','2026-06-02 04:25:19.075460',0,'2026-06-02 04:25:19.075460','ACTIVE','2026-06-02 04:25:19.075460',8,2);
/*!40000 ALTER TABLE `user_learning_paths` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_progress`
--

DROP TABLE IF EXISTS `user_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `learning_step_id` bigint NOT NULL,
  `started_at` datetime(6) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqgs3ht3rv1v01w2kiqta75159` (`user_id`,`learning_step_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_progress`
--

LOCK TABLES `user_progress` WRITE;
/*!40000 ALTER TABLE `user_progress` DISABLE KEYS */;
INSERT INTO `user_progress` VALUES (1,NULL,'2026-06-01 09:46:38.488905',1,'2026-06-01 09:46:38.487909','in_progress','2026-06-01 09:46:38.488905',2);
/*!40000 ALTER TABLE `user_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (2,1),(1,2),(3,2);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_step_progress`
--

DROP TABLE IF EXISTS `user_step_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_step_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `step_id` bigint NOT NULL,
  `completed` tinyint(1) DEFAULT '0',
  `completed_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_step` (`user_id`,`step_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_step_progress`
--

LOCK TABLES `user_step_progress` WRITE;
/*!40000 ALTER TABLE `user_step_progress` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_step_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `challenges` varchar(255) DEFAULT NULL,
  `class_or_year` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `interests` varchar(255) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `last_login` datetime(6) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `stream` varchar(255) DEFAULT NULL,
  `strengths` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,NULL,'2026-02-18 20:36:53.855768','rockypawar960@gmail.com','Rocky',NULL,_binary '','2026-05-29 06:46:05.328706','Pawar','$2a$10$z5ZrswC9gtwJrooAyn916uunGSnhJlDNTrmE.2mdgNZ9UrLrjs1FG',NULL,NULL,'2026-05-29 06:46:05.485007'),(2,NULL,NULL,'2026-02-19 11:04:33.213116','rockypawar154@gmail.com','Jayshree',NULL,_binary '','2026-06-02 02:50:58.810668','Pawar','$2a$10$1yvYCrftabxWapNu2o2DbOoZxymT..ySskiEPvEMZLvORBp8WcOw6',NULL,NULL,'2026-06-02 02:50:58.941445'),(3,NULL,NULL,'2026-04-15 09:48:32.444324','rockypawar2004@gmail.com','Laksh',NULL,_binary '',NULL,'Pawar','$2a$10$1pihKrtZzS5hHCSO/Iahne2aSAX3k.4eYPS1oVEbWXbyyJHmGfWK6',NULL,NULL,'2026-04-15 10:12:30.776665');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-02 14:04:48
