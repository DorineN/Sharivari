-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Lun 23 Mai 2016 à 07:01
-- Version du serveur :  5.6.17
-- Version de PHP :  5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `sharin`
--

-- --------------------------------------------------------

--
-- Structure de la table `document`
--

CREATE TABLE IF NOT EXISTS `document` (
  `idDocument` int(11) NOT NULL AUTO_INCREMENT,
  `linkDocument` text,
  `idProject` int(11) DEFAULT NULL,
  `idTask` int(11) DEFAULT NULL,
  PRIMARY KEY (`idDocument`),
  KEY `idProject` (`idProject`),
  KEY `idTask` (`idTask`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `execute`
--

CREATE TABLE IF NOT EXISTS `execute` (
  `idUser` int(11) DEFAULT NULL,
  `idTask` int(11) DEFAULT NULL,
  KEY `idUser` (`idUser`),
  KEY `idTask` (`idTask`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `participate`
--

CREATE TABLE IF NOT EXISTS `participate` (
  `idRole` int(11) NOT NULL DEFAULT '0',
  `idUser` int(11) NOT NULL DEFAULT '0',
  `idProject` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idRole`,`idProject`,`idUser`),
  KEY `idUser` (`idUser`),
  KEY `idProject` (`idProject`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `participate`
--

INSERT INTO `participate` (`idRole`, `idUser`, `idProject`) VALUES
(1, 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `priority`
--

CREATE TABLE IF NOT EXISTS `priority` (
  `idPriority` int(11) NOT NULL AUTO_INCREMENT,
  `namePriority` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idPriority`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `priority`
--

INSERT INTO `priority` (`idPriority`, `namePriority`) VALUES
(1, 'faible'),
(2, 'moyenne'),
(3, 'forte');

-- --------------------------------------------------------

--
-- Structure de la table `project`
--

CREATE TABLE IF NOT EXISTS `project` (
  `idProject` int(11) NOT NULL AUTO_INCREMENT,
  `nameProject` varchar(500) DEFAULT NULL,
  `descriptionProject` text,
  `startDateProject` date DEFAULT NULL,
  `realEndDateProject` date DEFAULT NULL,
  `estimateEndDateProject` date DEFAULT NULL,
  PRIMARY KEY (`idProject`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Contenu de la table `project`
--

INSERT INTO `project` (`idProject`, `nameProject`, `descriptionProject`, `startDateProject`, `realEndDateProject`, `estimateEndDateProject`) VALUES
(1, 'myProject', 'mydesc', '2016-04-24', NULL, '2016-05-24');

-- --------------------------------------------------------

--
-- Structure de la table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `idRole` int(11) NOT NULL AUTO_INCREMENT,
  `nameRole` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idRole`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Contenu de la table `role`
--

INSERT INTO `role` (`idRole`, `nameRole`) VALUES
(1, 'Chef de projet'),
(2, 'Développeur');

-- --------------------------------------------------------

--
-- Structure de la table `task`
--

CREATE TABLE IF NOT EXISTS `task` (
  `idTask` int(11) NOT NULL AUTO_INCREMENT,
  `nameTask` varchar(500) DEFAULT NULL,
  `descriptionTask` text,
  `estimateStartDateTask` date DEFAULT NULL,
  `realStartDateTask` date DEFAULT NULL,
  `estimateEndDateTask` date DEFAULT NULL,
  `realEndDateTask` date DEFAULT NULL,
  `idProject` int(11) DEFAULT NULL,
  `idPriority` int(11) DEFAULT NULL,
  PRIMARY KEY (`idTask`),
  KEY `idProject` (`idProject`),
  KEY `idPriority` (`idPriority`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `task`
--

INSERT INTO `task` (`idTask`, `nameTask`, `descriptionTask`, `estimateStartDateTask`, `realStartDateTask`, `estimateEndDateTask`, `realEndDateTask`, `idProject`, `idPriority`) VALUES
(1, 't1', 'd1', NULL, NULL, NULL, NULL, 1, 1),
(2, 't2', 'd2', NULL, NULL, NULL, NULL, 1, 2),
(3, 't3', 'd3', NULL, NULL, NULL, NULL, 1, 3);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `loginUser` varchar(50) NOT NULL,
  `pwdUser` varchar(50) DEFAULT NULL,
  `lastNameUser` varchar(100) DEFAULT NULL,
  `firstNameUser` varchar(100) DEFAULT NULL,
  `mailUser` varchar(500) DEFAULT NULL,
  `phoneUser` int(11) NOT NULL,
  `companyUser` varchar(200) NOT NULL,
  `typeUser` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`idUser`, `loginUser`, `pwdUser`, `lastNameUser`, `firstNameUser`, `mailUser`, `phoneUser`, `companyUser`, `typeUser`) VALUES
(1, 'user', 'azerty', 'monNom', 'monPrenom', 'monMail@gmail.com', 0, '', 'adm');

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `document`
--
ALTER TABLE `document`
  ADD CONSTRAINT `document_ibfk_1` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`),
  ADD CONSTRAINT `document_ibfk_2` FOREIGN KEY (`idTask`) REFERENCES `task` (`idTask`);

--
-- Contraintes pour la table `execute`
--
ALTER TABLE `execute`
  ADD CONSTRAINT `execute_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`),
  ADD CONSTRAINT `execute_ibfk_2` FOREIGN KEY (`idTask`) REFERENCES `task` (`idTask`);

--
-- Contraintes pour la table `participate`
--
ALTER TABLE `participate`
  ADD CONSTRAINT `participate_ibfk_1` FOREIGN KEY (`idRole`) REFERENCES `role` (`idRole`),
  ADD CONSTRAINT `participate_ibfk_2` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`),
  ADD CONSTRAINT `participate_ibfk_3` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`);

--
-- Contraintes pour la table `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `task_ibfk_1` FOREIGN KEY (`idProject`) REFERENCES `project` (`idProject`),
  ADD CONSTRAINT `task_ibfk_2` FOREIGN KEY (`idPriority`) REFERENCES `priority` (`idPriority`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
