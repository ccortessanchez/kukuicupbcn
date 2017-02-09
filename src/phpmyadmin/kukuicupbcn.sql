-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 09-02-2017 a las 16:32:15
-- Versión del servidor: 5.7.14
-- Versión de PHP: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `kukuicupbcn`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `activity_reg`
--

CREATE TABLE `activity_reg` (
  `player_name` varchar(30) NOT NULL,
  `activity_name` varchar(30) NOT NULL,
  `tournament_id` int(10) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `activity_reg`
--

INSERT INTO `activity_reg` (`player_name`, `activity_name`, `tournament_id`) VALUES
('ccortes', 'SA: Desk Light', 0),
('ccortes', 'SA: Comp. Responsible', 0),
('ccortes', 'EC: Off Bef. Sleep', 0),
('ccortes', 'V+Q: Power Energy', 0),
('ccortes', 'EC: Off Bef. Sleep', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `badges`
--

CREATE TABLE `badges` (
  `id` int(4) NOT NULL,
  `name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

CREATE TABLE `players` (
  `id` int(4) NOT NULL,
  `name` varchar(30) NOT NULL,
  `passwd` varchar(20) NOT NULL DEFAULT 'passwd',
  `points` int(4) NOT NULL DEFAULT '0',
  `team_id` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `players`
--

INSERT INTO `players` (`id`, `name`, `passwd`, `points`, `team_id`) VALUES
(1, 'ccortes', 'passwd', 73, 6),
(2, 'usertest', 'passwd', 0, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `players_badges`
--

CREATE TABLE `players_badges` (
  `player_id` int(11) NOT NULL,
  `badge_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `players_badges`
--

INSERT INTO `players_badges` (`player_id`, `badge_id`) VALUES
(15, 1),
(15, 2),
(15, 1),
(1, 4),
(1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `teams`
--

CREATE TABLE `teams` (
  `id` int(4) NOT NULL,
  `name` varchar(30) NOT NULL,
  `points` int(4) NOT NULL DEFAULT '0',
  `tournament_id` int(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `teams`
--

INSERT INTO `teams` (`id`, `name`, `points`, `tournament_id`) VALUES
(5, 'Team Green', 0, NULL),
(6, 'Team Black', 73, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `teams_badges`
--

CREATE TABLE `teams_badges` (
  `team_id` int(4) NOT NULL,
  `badge_id` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tournament`
--

CREATE TABLE `tournament` (
  `id` int(4) NOT NULL,
  `init_date` date DEFAULT NULL,
  `finish_date` date DEFAULT NULL,
  `duration` int(4) DEFAULT NULL,
  `n_teams` int(4) DEFAULT '0',
  `n_players` int(4) DEFAULT '0',
  `name` varchar(75) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `tournament`
--

INSERT INTO `tournament` (`id`, `init_date`, `finish_date`, `duration`, `n_teams`, `n_players`, `name`) VALUES
(33, '2017-02-09', '2017-03-02', 21, 0, 0, 'Torneo prueba');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `badges`
--
ALTER TABLE `badges`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`id`),
  ADD KEY `team_id` (`team_id`);

--
-- Indices de la tabla `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tournament_id` (`tournament_id`),
  ADD KEY `tournament_id_2` (`tournament_id`);

--
-- Indices de la tabla `teams_badges`
--
ALTER TABLE `teams_badges`
  ADD KEY `team_id` (`team_id`,`badge_id`),
  ADD KEY `team_id_2` (`team_id`),
  ADD KEY `badge_id` (`badge_id`);

--
-- Indices de la tabla `tournament`
--
ALTER TABLE `tournament`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `duration` (`duration`),
  ADD UNIQUE KEY `duration_2` (`duration`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `badges`
--
ALTER TABLE `badges`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT de la tabla `players`
--
ALTER TABLE `players`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT de la tabla `teams`
--
ALTER TABLE `teams`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT de la tabla `tournament`
--
ALTER TABLE `tournament`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `players`
--
ALTER TABLE `players`
  ADD CONSTRAINT `players_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
