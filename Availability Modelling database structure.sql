-- MySQL Script generated by MySQL Workbench
-- Thu Dec 26 07:45:15 2019
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema tree
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema tree
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tree` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `tree` ;

-- -----------------------------------------------------
-- Table `tree`.`node`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tree`.`node` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `identifier` VARCHAR(255) NOT NULL,
  `parent_id` VARCHAR(255) NULL DEFAULT NULL,
  `node_name` VARCHAR(255) NULL DEFAULT NULL,
  `node_type` VARCHAR(255) NULL DEFAULT NULL,
  `project_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `project_name` (`project_name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 94
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
