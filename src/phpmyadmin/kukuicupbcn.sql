-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 10-07-2016 a las 21:12:40
-- Versión del servidor: 5.5.24-log
-- Versión de PHP: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `kukuicupbcn`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `badges`
--

CREATE TABLE IF NOT EXISTS `badges` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Volcado de datos para la tabla `badges`
--

INSERT INTO `badges` (`id`, `name`) VALUES
(1, 'level1'),
(2, 'level2'),
(3, 'level3'),
(4, 'offBeforeSleep'),
(5, 'useStairs'),
(6, 'lightsOff'),
(7, 'ledBulb'),
(8, 'energyQuest'),
(9, 'cleanEnergy');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `players`
--

CREATE TABLE IF NOT EXISTS `players` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `passwd` varchar(20) NOT NULL DEFAULT 'passwd',
  `points` int(4) NOT NULL DEFAULT '0',
  `team_id` int(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

--
-- Volcado de datos para la tabla `players`
--

INSERT INTO `players` (`id`, `name`, `passwd`, `points`, `team_id`) VALUES
(2, 'jarias', 'passwd', 15, 2),
(10, 'leomartin', 'passwd', 5, 4),
(11, 'danielaferrer', 'passwd', 8, 3),
(12, 'inmarodriguez', 'passwd', 15, 3),
(13, 'rafasuares', 'passwd', 15, 4),
(14, 'juliolopez', 'passwd', 10, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `players_badges`
--

CREATE TABLE IF NOT EXISTS `players_badges` (
  `player_id` int(4) NOT NULL,
  `badge_id` int(4) NOT NULL,
  KEY `player_id` (`player_id`,`badge_id`),
  KEY `badge_id` (`badge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `teams`
--

CREATE TABLE IF NOT EXISTS `teams` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `points` int(4) NOT NULL DEFAULT '0',
  `tournament_id` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tournament_id` (`tournament_id`),
  KEY `tournament_id_2` (`tournament_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Volcado de datos para la tabla `teams`
--

INSERT INTO `teams` (`id`, `name`, `points`, `tournament_id`) VALUES
(2, 'Team Yellow', 25, 1),
(3, 'Team Red', 23, 1),
(4, 'Team Blue', 20, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `teams_badges`
--

CREATE TABLE IF NOT EXISTS `teams_badges` (
  `team_id` int(4) NOT NULL,
  `badge_id` int(4) NOT NULL,
  KEY `team_id` (`team_id`,`badge_id`),
  KEY `team_id_2` (`team_id`),
  KEY `badge_id` (`badge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tournament`
--

CREATE TABLE IF NOT EXISTS `tournament` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `init_date` date DEFAULT NULL,
  `finish_date` date DEFAULT NULL,
  `duration` int(4) DEFAULT NULL,
  `n_teams` int(4) DEFAULT '0',
  `n_players` int(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `duration` (`duration`),
  UNIQUE KEY `duration_2` (`duration`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `tournament`
--

INSERT INTO `tournament` (`id`, `init_date`, `finish_date`, `duration`, `n_teams`, `n_players`) VALUES
(1, NULL, NULL, NULL, 0, 0);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `players`
--
ALTER TABLE `players`
  ADD CONSTRAINT `players_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `players_badges`
--
ALTER TABLE `players_badges`
  ADD CONSTRAINT `players_badges_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `players_badges_ibfk_2` FOREIGN KEY (`badge_id`) REFERENCES `badges` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `teams`
--
ALTER TABLE `teams`
  ADD CONSTRAINT `teams_ibfk_1` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `teams_badges`
--
ALTER TABLE `teams_badges`
  ADD CONSTRAINT `teams_badges_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `teams_badges_ibfk_2` FOREIGN KEY (`badge_id`) REFERENCES `badges` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
