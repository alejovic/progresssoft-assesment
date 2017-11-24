-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: progresssoft
-- ------------------------------------------------------
-- Server version	5.7.10-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ps_deal_data_error`
--

DROP TABLE IF EXISTS `ps_deal_data_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ps_deal_data_error` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FK_ID_FILE` bigint(20) NOT NULL,
  `LINE` bigint(20) DEFAULT NULL,
  `DEAL_ERROR` varchar(200) DEFAULT NULL,
  `MESSSAGE_ERROR` varchar(200) DEFAULT NULL,
  `DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DATA_ERROR_FILE` (`FK_ID_FILE`),
  CONSTRAINT `FK_DATA_ERROR_FILE` FOREIGN KEY (`FK_ID_FILE`) REFERENCES `ps_file` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ps_deal_data_error`
--

LOCK TABLES `ps_deal_data_error` WRITE;
/*!40000 ALTER TABLE `ps_deal_data_error` DISABLE KEYS */;
INSERT INTO `ps_deal_data_error` VALUES (1,69,1,'DealDTO [id=150001, isoSource=AOA, isoTarget=ARS, date=2017/11/22x19:59:45, amount=36098.03, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [1]','2017-11-25 02:11:33'),(2,69,4,'DealDTO [id=150004, isoSource=AOA, isoTarget=2LL, date=2017/11/22 19:59:45, amount=20851.31, line=0]','ERROR: Field not valid. expect [ISO Code] instead [2LL] in line [4]','2017-11-25 02:11:33'),(3,69,6,'DealDTO [id=150007, isoSource=AMD, isoTarget=AED, date=2017/11/22x19:59:45, amount=20317.74, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [6]','2017-11-25 02:11:33'),(4,1,1,'DealDTO [id=150001, isoSource=AOA, isoTarget=ARS, date=2017/11/22x19:59:45, amount=36098.03, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [1]','2017-11-21 11:20:25'),(5,1,4,'DealDTO [id=150004, isoSource=AOA, isoTarget=2LL, date=2017/11/22 19:59:45, amount=20851.31, line=0]','ERROR: Field not valid. expect [ISO Code] instead [2LL] in line [4]','2017-11-21 11:20:25'),(6,1,6,'DealDTO [id=150007, isoSource=AMD, isoTarget=AED, date=2017/11/22x19:59:45, amount=20317.74, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [6]','2017-11-21 11:20:25'),(7,1,1,'DealDTO [id=150001, isoSource=AOA, isoTarget=ARS, date=2017/11/22x19:59:45, amount=36098.03, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [1]','2017-11-21 11:20:25'),(8,1,4,'DealDTO [id=150004, isoSource=AOA, isoTarget=2LL, date=2017/11/22 19:59:45, amount=20851.31, line=0]','ERROR: Field not valid. expect [ISO Code] instead [2LL] in line [4]','2017-11-21 11:20:25'),(9,1,6,'DealDTO [id=150007, isoSource=AMD, isoTarget=AED, date=2017/11/22x19:59:45, amount=20317.74, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [6]','2017-11-21 11:20:25'),(10,1,1,'DealDTO [id=150001, isoSource=AOA, isoTarget=ARS, date=2017/11/22x19:59:45, amount=36098.03, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [1]','2017-11-21 11:20:25'),(11,1,4,'DealDTO [id=150004, isoSource=AOA, isoTarget=2LL, date=2017/11/22 19:59:45, amount=20851.31, line=0]','ERROR: Field not valid. expect [ISO Code] instead [2LL] in line [4]','2017-11-21 11:20:25'),(12,1,6,'DealDTO [id=150007, isoSource=AMD, isoTarget=AED, date=2017/11/22x19:59:45, amount=20317.74, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [6]','2017-11-21 11:20:25'),(13,1,1,'DealDTO [id=150001, isoSource=AOA, isoTarget=ARS, date=2017/11/22x19:59:45, amount=36098.03, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [1]','2017-11-21 11:20:25'),(14,1,4,'DealDTO [id=150004, isoSource=AOA, isoTarget=2LL, date=2017/11/22 19:59:45, amount=20851.31, line=0]','ERROR: Field not valid. expect [ISO Code] instead [2LL] in line [4]','2017-11-21 11:20:25'),(15,1,6,'DealDTO [id=150007, isoSource=AMD, isoTarget=AED, date=2017/11/22x19:59:45, amount=20317.74, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [6]','2017-11-21 11:20:25'),(16,1,1,'DealDTO [id=150001, isoSource=AOA, isoTarget=ARS, date=2017/11/22x19:59:45, amount=36098.03, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [1]','2017-11-21 11:20:25'),(17,1,4,'DealDTO [id=150004, isoSource=AOA, isoTarget=2LL, date=2017/11/22 19:59:45, amount=20851.31, line=0]','ERROR: Field not valid. expect [ISO Code] instead [2LL] in line [4]','2017-11-21 11:20:25'),(18,1,6,'DealDTO [id=150007, isoSource=AMD, isoTarget=AED, date=2017/11/22x19:59:45, amount=20317.74, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [6]','2017-11-21 11:20:25'),(19,1,1,'DealDTO [id=150001, isoSource=AOA, isoTarget=ARS, date=2017/11/22x19:59:45, amount=36098.03, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [1]','2017-11-21 11:20:25'),(20,1,4,'DealDTO [id=150004, isoSource=AOA, isoTarget=2LL, date=2017/11/22 19:59:45, amount=20851.31, line=0]','ERROR: Field not valid. expect [ISO Code] instead [2LL] in line [4]','2017-11-21 11:20:25'),(21,1,6,'DealDTO [id=150007, isoSource=AMD, isoTarget=AED, date=2017/11/22x19:59:45, amount=20317.74, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [6]','2017-11-21 11:20:25'),(22,83,1,'DealDTO [id=150001, isoSource=AOA, isoTarget=ARS, date=2017/11/22x19:59:45, amount=36098.03, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [1]','2017-11-25 03:49:04'),(23,83,4,'DealDTO [id=150004, isoSource=AOA, isoTarget=2LL, date=2017/11/22 19:59:45, amount=20851.31, line=0]','ERROR: Field not valid. expect [ISO Code] instead [2LL] in line [4]','2017-11-25 03:49:04'),(24,83,6,'DealDTO [id=150007, isoSource=AMD, isoTarget=AED, date=2017/11/22x19:59:45, amount=20317.74, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [6]','2017-11-25 03:49:04'),(25,84,1,'DealDTO [id=150001, isoSource=AOA, isoTarget=ARS, date=2017/11/22x19:59:45, amount=36098.03, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [1]','2017-11-25 03:50:59'),(26,84,4,'DealDTO [id=150004, isoSource=AOA, isoTarget=2LL, date=2017/11/22 19:59:45, amount=20851.31, line=0]','ERROR: Field not valid. expect [ISO Code] instead [2LL] in line [4]','2017-11-25 03:50:59'),(27,84,6,'DealDTO [id=150007, isoSource=AMD, isoTarget=AED, date=2017/11/22x19:59:45, amount=20317.74, line=0]','ERROR: Field not valid. expect [Date Format (YYYY/MM/dd HH:mm:ss)] instead [2017/11/22x19:59:45] in line [6]','2017-11-25 03:50:59');
/*!40000 ALTER TABLE `ps_deal_data_error` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-25  3:55:48
