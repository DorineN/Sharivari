-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Dim 24 Avril 2016 à 18:40
-- Version du serveur :  5.6.17
-- Version de PHP :  5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `sharin_web`
--

-- --------------------------------------------------------

--
-- Structure de la table `plugin`
--

CREATE TABLE IF NOT EXISTS `plugin` (
  `idPlugin` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `url` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`idPlugin`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `plugin`
--

INSERT INTO `plugin` (`idPlugin`, `name`, `description`, `url`, `created_at`, `updated_at`, `deleted_at`) VALUES
(1, 'monPlugin', 'ceci est une superbe description', 'test.pdf', '2016-04-18 20:00:00', '0000-00-00 00:00:00', NULL),
(2, 'myName', 'myDescript', 'SkypeSetupFull.exe', '0000-00-00 00:00:00', '0000-00-00 00:00:00', NULL),
(3, 'testName', 'testName', 'vide', '2016-04-24 12:47:28', '2016-04-24 12:47:28', NULL),
(4, 'testName', 'testName', 'vide', '2016-04-24 12:48:02', '2016-04-24 12:48:02', NULL),
(5, 'testName', 'testName', 'vide', '2016-04-24 12:48:04', '2016-04-24 12:48:04', NULL),
(6, 'tdsfgdg', 'tdsfgdg', 'vide', '2016-04-24 16:33:13', '2016-04-24 16:33:13', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `login` varchar(255) NOT NULL DEFAULT '',
  `pwd` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`login`, `pwd`, `created_at`, `updated_at`, `deleted_at`) VALUES
('admin', 'admin', '2018-04-18 20:00:00', '0000-00-00 00:00:00', NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
