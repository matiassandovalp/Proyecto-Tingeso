-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: tingeso
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Current Database: `tingeso`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `tingeso` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `tingeso`;

--
-- Table structure for table `bloque_horario`
--

DROP TABLE IF EXISTS `bloque_horario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bloque_horario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha` datetime(6) NOT NULL,
  `hora_fin` varchar(255) NOT NULL,
  `hora_inicio` varchar(255) NOT NULL,
  `reserva_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd3g8am1jerjaewjvlht71vdmp` (`reserva_id`),
  CONSTRAINT `FKd3g8am1jerjaewjvlht71vdmp` FOREIGN KEY (`reserva_id`) REFERENCES `reserva` (`reserva_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bloque_horario`
--

LOCK TABLES `bloque_horario` WRITE;
/*!40000 ALTER TABLE `bloque_horario` DISABLE KEYS */;
INSERT INTO `bloque_horario` VALUES (1,'2025-04-29 00:00:00.000000','00:40','00:00',5),(2,'2025-04-24 11:31:00.000000','12:01','11:31',6),(3,'2025-04-24 11:31:00.000000','12:01','11:31',6),(4,'2025-04-24 11:31:00.000000','12:01','11:31',6),(5,'2025-04-26 15:41:00.000000','16:11','15:41',7),(6,'2025-04-26 15:41:00.000000','16:11','15:41',7),(7,'2025-04-26 15:41:00.000000','16:11','15:41',7),(8,'2025-04-30 13:42:00.000000','14:17','13:42',8),(9,'2025-04-30 13:42:00.000000','14:17','13:42',8),(10,'2025-04-30 13:42:00.000000','14:17','13:42',8),(11,'2025-04-30 17:41:00.000000','18:16','17:41',9),(12,'2025-04-30 17:41:00.000000','18:16','17:41',9),(13,'2025-04-30 19:23:00.000000','20:03','19:23',10),(14,'2025-04-30 19:23:00.000000','20:03','19:23',10),(15,'2025-04-25 13:28:00.000000','14:03','13:28',11),(16,'2025-04-25 13:28:00.000000','14:03','13:28',11),(17,'2025-04-29 17:25:00.000000','17:55','17:25',12),(18,'2025-04-30 17:26:00.000000','18:01','17:26',13),(19,'2025-04-30 17:30:00.000000','18:00','17:30',14),(20,'2025-04-30 08:48:00.000000','09:23','08:48',15),(21,'2025-04-30 17:56:00.000000','18:26','17:56',16),(22,'2025-04-30 22:53:00.000000','23:28','22:53',17),(23,'2025-05-01 16:56:00.000000','17:26','16:56',18),(24,'2025-05-01 16:56:00.000000','17:26','16:56',18),(25,'2025-05-02 12:57:00.000000','13:32','12:57',19),(26,'2025-05-02 09:22:00.000000','09:57','09:22',20),(27,'2025-04-30 17:02:00.000000','17:32','17:02',21),(28,'2025-05-03 17:27:00.000000','17:57','17:27',22),(29,'2025-05-03 08:15:00.000000','08:50','08:15',23),(30,'2025-05-02 14:29:00.000000','15:09','14:29',24),(31,'2025-05-03 17:34:00.000000','18:09','17:34',25),(32,'2025-05-03 17:34:00.000000','18:09','17:34',25),(33,'2025-04-29 10:00:00.000000','10:35','10:00',26),(34,'2025-04-29 10:00:00.000000','10:35','10:00',26),(35,'2025-04-29 10:00:00.000000','10:35','10:00',26),(36,'2025-04-29 10:00:00.000000','10:35','10:00',26),(37,'2025-05-03 19:31:00.000000','20:06','19:31',27);
/*!40000 ALTER TABLE `bloque_horario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bloque_horario_entity_karts_ocupados`
--

DROP TABLE IF EXISTS `bloque_horario_entity_karts_ocupados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bloque_horario_entity_karts_ocupados` (
  `bloque_horario_entity_id` int NOT NULL,
  `karts_ocupados` varchar(255) DEFAULT NULL,
  KEY `FK8fwgbv2u4qrgbik19o8bkw481` (`bloque_horario_entity_id`),
  CONSTRAINT `FK8fwgbv2u4qrgbik19o8bkw481` FOREIGN KEY (`bloque_horario_entity_id`) REFERENCES `bloque_horario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bloque_horario_entity_karts_ocupados`
--

LOCK TABLES `bloque_horario_entity_karts_ocupados` WRITE;
/*!40000 ALTER TABLE `bloque_horario_entity_karts_ocupados` DISABLE KEYS */;
INSERT INTO `bloque_horario_entity_karts_ocupados` VALUES (1,'K005'),(2,'K002'),(3,'K003'),(4,'K004'),(5,'K001'),(6,'K002'),(7,'K004'),(8,'K002'),(9,'K004'),(10,'K001'),(11,'K001'),(12,'K002'),(13,'K003'),(14,'K004'),(15,'K004'),(16,'K005'),(17,'K005'),(18,'K006'),(19,'K004'),(20,'K006'),(21,'K004'),(22,'K007'),(23,'K005'),(24,'K006'),(25,'K007'),(26,'K007'),(27,'K007'),(28,'K006'),(29,'K006'),(30,'K013'),(31,'K001'),(32,'K005'),(33,'K004'),(34,'K002'),(35,'K001'),(36,'K005'),(37,'K005');
/*!40000 ALTER TABLE `bloque_horario_entity_karts_ocupados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `client_id` varchar(255) NOT NULL,
  `nombre_cliente` varchar(255) DEFAULT NULL,
  `visitas_mensuales` int NOT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES ('C001','Juan Pérez',3),('C002','Ana González Actualizado',7),('C003','Luis Martínez',2),('C004','Sofía Herrera',8),('C005','Roberto Díaz',1),('C006','Marcela Silva',4),('C007','Fernando Ríos',6),('C008','Natalia Fuentes',10),('C009','Diego Torres',7),('C010','Valentina López',9);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprobante`
--

DROP TABLE IF EXISTS `comprobante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprobante` (
  `comprobante_id` int NOT NULL AUTO_INCREMENT,
  `descuento_personal` int NOT NULL,
  `descuento_personas` int NOT NULL,
  `numero_vueltas` int NOT NULL,
  `precio_ajustado` int NOT NULL,
  `precio_estandar` int NOT NULL,
  `precio_final` int NOT NULL,
  `valoriva` int NOT NULL,
  `reserva_id` int NOT NULL,
  `fecha_emision` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`comprobante_id`),
  UNIQUE KEY `UKjd4o2y2lhijlc1wpm10n9o9dm` (`reserva_id`),
  CONSTRAINT `FK4bt04oi3h9chu4ma6hj5j6iht` FOREIGN KEY (`reserva_id`) REFERENCES `reserva` (`reserva_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comprobante`
--

LOCK TABLES `comprobante` WRITE;
/*!40000 ALTER TABLE `comprobante` DISABLE KEYS */;
INSERT INTO `comprobante` VALUES (1,2000,2000,15,10000,20000,11900,1900,1,'2025-04-27 18:12:16.893000'),(2,2000,2000,15,10000,20000,11900,1900,2,'2025-04-27 18:12:30.989000'),(3,0,2000,15,10000,20000,11900,1900,3,'2025-04-27 18:28:06.380000'),(4,1500,0,10,13500,15000,16065,2565,4,'2025-04-27 18:28:36.012000'),(5,2500,0,20,22500,25000,26775,4275,5,'2025-04-27 18:38:56.274000'),(6,1500,1500,10,7500,15000,8925,1425,6,'2025-04-29 11:32:16.971000'),(7,13500,4500,10,22500,45000,26775,4275,7,'2025-04-29 11:38:16.354000'),(8,6000,6000,15,30000,60000,35700,5700,8,'2025-04-29 11:40:42.411000'),(9,4000,0,15,20000,40000,23800,3800,9,'2025-04-29 15:39:20.425000'),(10,5000,0,20,45000,50000,53550,8550,10,'2025-04-29 16:21:05.144000'),(11,12000,0,15,28000,40000,33320,5320,11,'2025-04-29 16:25:22.919000'),(12,3000,0,10,7500,15000,8925,1425,12,'2025-04-29 22:21:17.548000'),(13,6000,0,15,14000,20000,16660,2660,13,'2025-04-29 22:22:32.481000'),(14,3000,0,10,7500,15000,8925,1425,14,'2025-04-29 22:26:21.005000'),(15,0,0,15,10000,20000,11900,1900,15,'2025-04-29 22:46:35.861000'),(16,4500,0,10,7500,15000,8925,1425,16,'2025-04-29 22:53:07.298000'),(17,2000,0,15,10000,20000,11900,1900,17,'2025-04-29 22:53:40.982000'),(18,3000,0,10,15000,30000,17850,2850,18,'2025-04-29 22:54:09.187000'),(19,6000,0,15,10000,20000,11900,1900,19,'2025-04-29 22:54:46.621000'),(20,6000,0,15,14000,20000,16660,2660,20,'2025-04-29 22:56:00.859000'),(21,0,0,10,7500,15000,8925,1425,21,'2025-04-29 22:58:15.485000'),(22,1500,0,10,7500,15000,8925,1425,22,'2025-04-29 23:07:37.756000'),(23,6000,0,15,10000,20000,11900,1900,23,'2025-04-29 23:20:50.506000'),(24,7500,0,20,17500,25000,20825,3325,24,'2025-04-29 23:24:47.968000'),(25,12000,0,15,28000,40000,33320,5320,25,'2025-04-29 23:30:26.686000'),(26,24000,8000,15,40000,80000,47600,7600,26,'2025-04-30 00:07:58.256000'),(27,2000,0,15,10000,20000,11900,1900,27,'2025-04-30 00:27:14.362000');
/*!40000 ALTER TABLE `comprobante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kart`
--

DROP TABLE IF EXISTS `kart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kart` (
  `kart_id` varchar(255) NOT NULL,
  `modelo` varchar(255) DEFAULT NULL,
  `ultimo_mantenimiento` date DEFAULT NULL,
  `estado` bit(1) NOT NULL,
  PRIMARY KEY (`kart_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kart`
--

LOCK TABLES `kart` WRITE;
/*!40000 ALTER TABLE `kart` DISABLE KEYS */;
INSERT INTO `kart` VALUES ('K001','Sodikart RT8','2025-04-09',_binary '\0'),('K002','Birel N35','2025-04-11',_binary '\0'),('K003','Tony Kart Racer','2025-04-14',_binary '\0'),('K004','CRG Road Rebel','2025-04-17',_binary '\0'),('K005','Kosmic Mercury','2025-04-19',_binary '\0'),('K006','Arrow X5','2025-04-20',_binary '\0'),('K007','Birel ART S8','2025-04-21',_binary '\0'),('K008','Intrepid Cruiser','2025-04-22',_binary '\0'),('K009','Sodikart GT5','2025-04-23',_binary '\0'),('K010','Tony Kart Krypton','2025-04-24',_binary '\0'),('K011','CRG Dark Knight','2025-04-25',_binary '\0'),('K012','Parolin Invader','2025-04-26',_binary '\0'),('K013','Sodikart SR5','2025-04-27',_binary '\0'),('K014','Kosmic Lynx','2025-04-28',_binary '\0'),('K015','Birel T30','2025-04-29',_binary '\0');
/*!40000 ALTER TABLE `kart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kart_entity_reservas_ids`
--

DROP TABLE IF EXISTS `kart_entity_reservas_ids`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kart_entity_reservas_ids` (
  `kart_entity_kart_id` varchar(255) NOT NULL,
  `reservas_ids` varchar(255) DEFAULT NULL,
  KEY `FKgcde5bkp0kehfj4yl4b27pp4o` (`kart_entity_kart_id`),
  CONSTRAINT `FKgcde5bkp0kehfj4yl4b27pp4o` FOREIGN KEY (`kart_entity_kart_id`) REFERENCES `kart` (`kart_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kart_entity_reservas_ids`
--

LOCK TABLES `kart_entity_reservas_ids` WRITE;
/*!40000 ALTER TABLE `kart_entity_reservas_ids` DISABLE KEYS */;
/*!40000 ALTER TABLE `kart_entity_reservas_ids` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reserva`
--

DROP TABLE IF EXISTS `reserva`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserva` (
  `reserva_id` int NOT NULL AUTO_INCREMENT,
  `cant_personas` int NOT NULL,
  `duracion` time(6) DEFAULT NULL,
  `fecha` datetime(6) NOT NULL,
  `cliente_id` varchar(255) NOT NULL,
  `es_cumpleaños` bit(1) NOT NULL,
  `tarifa_seleccionada` varchar(255) NOT NULL,
  PRIMARY KEY (`reserva_id`),
  KEY `FK7cg2jiyn5cf6f6elccvb6963k` (`cliente_id`),
  CONSTRAINT `FK7cg2jiyn5cf6f6elccvb6963k` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva`
--

LOCK TABLES `reserva` WRITE;
/*!40000 ALTER TABLE `reserva` DISABLE KEYS */;
INSERT INTO `reserva` VALUES (1,4,NULL,'2025-04-29 00:00:00.000000','C003',_binary '','15_vueltas_15_min'),(2,5,NULL,'2025-04-27 00:00:00.000000','C003',_binary '','15_vueltas_15_min'),(3,4,NULL,'2025-12-12 00:00:00.000000','C005',_binary '','15_vueltas_15_min'),(4,2,NULL,'2025-12-12 00:00:00.000000','C001',_binary '\0','10_vueltas_10_min'),(5,1,NULL,'2025-04-29 00:00:00.000000','C006',_binary '\0','20_vueltas_20_min'),(6,3,NULL,'2025-04-24 11:31:00.000000','C001',_binary '','10_vueltas_10_min'),(7,3,NULL,'2025-04-26 15:41:00.000000','C004',_binary '','10_vueltas_10_min'),(8,3,NULL,'2025-04-30 13:42:00.000000','C006',_binary '','15_vueltas_15_min'),(9,2,NULL,'2025-04-30 17:41:00.000000','C006',_binary '','15_vueltas_15_min'),(10,2,NULL,'2025-04-30 19:23:00.000000','C006',_binary '\0','20_vueltas_20_min'),(11,2,NULL,'2025-04-25 13:28:00.000000','C004',_binary '\0','15_vueltas_15_min'),(12,1,NULL,'2025-04-29 17:25:00.000000','C007',_binary '','10_vueltas_10_min'),(13,1,NULL,'2025-04-30 17:26:00.000000','C008',_binary '\0','15_vueltas_15_min'),(14,1,NULL,'2025-04-30 17:30:00.000000','C007',_binary '','10_vueltas_10_min'),(15,1,NULL,'2025-04-30 08:48:00.000000','C005',_binary '','15_vueltas_15_min'),(16,1,NULL,'2025-04-30 17:56:00.000000','C008',_binary '','10_vueltas_10_min'),(17,1,NULL,'2025-04-30 22:53:00.000000','C001',_binary '','15_vueltas_15_min'),(18,2,NULL,'2025-05-01 16:56:00.000000','C006',_binary '','10_vueltas_10_min'),(19,1,NULL,'2025-05-02 12:57:00.000000','C008',_binary '','15_vueltas_15_min'),(20,1,NULL,'2025-05-02 09:22:00.000000','C004',_binary '\0','15_vueltas_15_min'),(21,1,NULL,'2025-04-30 17:02:00.000000','C005',_binary '','10_vueltas_10_min'),(22,1,NULL,'2025-05-03 17:27:00.000000','C006',_binary '','10_vueltas_10_min'),(23,1,NULL,'2025-05-03 08:15:00.000000','C009',_binary '','15_vueltas_15_min'),(24,1,NULL,'2025-05-02 14:29:00.000000','C010',_binary '\0','20_vueltas_20_min'),(25,2,NULL,'2025-05-03 17:34:00.000000','C008',_binary '\0','15_vueltas_15_min'),(26,4,NULL,'2025-04-29 10:00:00.000000','C010',_binary '','15_vueltas_15_min'),(27,1,NULL,'2025-05-03 19:31:00.000000','C003',_binary '','15_vueltas_15_min');
/*!40000 ALTER TABLE `reserva` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reserva_entity_kart_ids`
--

DROP TABLE IF EXISTS `reserva_entity_kart_ids`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserva_entity_kart_ids` (
  `reserva_entity_reserva_id` int NOT NULL,
  `kart_ids` varchar(255) DEFAULT NULL,
  KEY `FKiuw03fmk0uyd68lo3uj970803` (`reserva_entity_reserva_id`),
  CONSTRAINT `FKiuw03fmk0uyd68lo3uj970803` FOREIGN KEY (`reserva_entity_reserva_id`) REFERENCES `reserva` (`reserva_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva_entity_kart_ids`
--

LOCK TABLES `reserva_entity_kart_ids` WRITE;
/*!40000 ALTER TABLE `reserva_entity_kart_ids` DISABLE KEYS */;
/*!40000 ALTER TABLE `reserva_entity_kart_ids` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reserva_kart`
--

DROP TABLE IF EXISTS `reserva_kart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserva_kart` (
  `reserva_id` int NOT NULL,
  `kart_id` varchar(255) NOT NULL,
  KEY `FK49eb2q8h57qgve89jv6dwpqo4` (`kart_id`),
  KEY `FK6rvq44k25ea6fny7htm13ck3m` (`reserva_id`),
  CONSTRAINT `FK49eb2q8h57qgve89jv6dwpqo4` FOREIGN KEY (`kart_id`) REFERENCES `kart` (`kart_id`),
  CONSTRAINT `FK6rvq44k25ea6fny7htm13ck3m` FOREIGN KEY (`reserva_id`) REFERENCES `reserva` (`reserva_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva_kart`
--

LOCK TABLES `reserva_kart` WRITE;
/*!40000 ALTER TABLE `reserva_kart` DISABLE KEYS */;
/*!40000 ALTER TABLE `reserva_kart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reserva_karts`
--

DROP TABLE IF EXISTS `reserva_karts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserva_karts` (
  `reserva_id` int NOT NULL,
  `kart_id` varchar(255) DEFAULT NULL,
  KEY `FKdsb7oh0qycggoyevc2riyeulh` (`reserva_id`),
  CONSTRAINT `FKdsb7oh0qycggoyevc2riyeulh` FOREIGN KEY (`reserva_id`) REFERENCES `reserva` (`reserva_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva_karts`
--

LOCK TABLES `reserva_karts` WRITE;
/*!40000 ALTER TABLE `reserva_karts` DISABLE KEYS */;
INSERT INTO `reserva_karts` VALUES (1,'K003'),(1,'K006'),(1,'K007'),(1,'K005'),(2,'K003'),(2,'K004'),(2,'K005'),(2,'K006'),(3,'K003'),(3,'K005'),(3,'K007'),(3,'K008'),(4,'K001'),(4,'K002'),(5,'K005'),(6,'K002'),(6,'K003'),(6,'K004'),(7,'K001'),(7,'K002'),(7,'K004'),(8,'K002'),(8,'K004'),(8,'K001'),(9,'K001'),(9,'K002'),(10,'K003'),(10,'K004'),(11,'K004'),(11,'K005'),(12,'K005'),(13,'K006'),(14,'K004'),(15,'K006'),(16,'K004'),(17,'K007'),(18,'K005'),(18,'K006'),(19,'K007'),(20,'K007'),(21,'K007'),(22,'K006'),(23,'K006'),(24,'K013'),(25,'K001'),(25,'K005'),(26,'K004'),(26,'K002'),(26,'K001'),(26,'K005'),(27,'K005');
/*!40000 ALTER TABLE `reserva_karts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reserva_nombres_personas`
--

DROP TABLE IF EXISTS `reserva_nombres_personas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserva_nombres_personas` (
  `reserva_id` int NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  KEY `FK186wce9u303hrsbrceutpprwr` (`reserva_id`),
  CONSTRAINT `FK186wce9u303hrsbrceutpprwr` FOREIGN KEY (`reserva_id`) REFERENCES `reserva` (`reserva_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva_nombres_personas`
--

LOCK TABLES `reserva_nombres_personas` WRITE;
/*!40000 ALTER TABLE `reserva_nombres_personas` DISABLE KEYS */;
INSERT INTO `reserva_nombres_personas` VALUES (6,'SDF'),(6,'SDF'),(6,'SDF'),(7,'Miguelito'),(7,'Saraa'),(7,'Pepe'),(8,'Fabiola'),(8,'Fabian'),(8,'Fabi'),(9,'Axel'),(9,'SAA'),(10,'Axel'),(10,'Poal'),(11,'Mish'),(11,'Crocodilo'),(12,'Miguel'),(13,'SDG'),(14,'Popu'),(15,'Joselo'),(16,'12'),(17,'1qadsa'),(18,'Axel'),(18,'SAA'),(19,'Saavedra'),(20,'1qadsa'),(21,'DEEE'),(22,'Ariel'),(23,'muu'),(24,'Sando'),(25,'Jose'),(25,'Miguel'),(26,'Ana'),(26,'Carlos'),(26,'Luis'),(26,'Marta'),(27,'sda');
/*!40000 ALTER TABLE `reserva_nombres_personas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-30  8:54:53
