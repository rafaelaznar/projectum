-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: May 27, 2023 at 06:21 PM
-- Server version: 8.0.18
-- PHP Version: 7.2.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `projectum`
--

-- --------------------------------------------------------

--
-- Table structure for table `developer`
--

CREATE TABLE `developer` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `surname` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `last_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `developer`
--

INSERT INTO `developer` (`id`, `name`, `surname`, `last_name`, `email`, `username`) VALUES
(1, 'raimon', 'vilar', 'morera', 'test@email.com', 'raivi'),
(2, 'alvaro', 'talaya', 'romance', 'test@email.com', 'alta'),
(3, 'mario', 'tomas', 'zanon', 'test@email.com', 'mato'),
(4, 'aitana', 'collado', 'soler', 'test@email.com', 'aico'),
(5, 'carlos', 'merlos', 'pilar', 'test@email.com', 'cam777'),
(6, 'luis', 'perez', 'derecho', 'test@email.com', 'lupe'),
(7, 'estefania', 'boriko', 'izquierdo', 'test@email.com', 'esbo'),
(8, 'quique', 'aroca', 'garcia', 'test@email.com', 'quiga'),
(9, 'adrian', 'duyang', 'liang', 'test@email.com', 'adu'),
(10, 'rafael', 'aznar', 'aparici', 'test@email.com', 'raza'),
(12, 'Mario', 'Benito', 'Luz', 'mariobenito120@projectum.net', 'mariobenito120');

-- --------------------------------------------------------

--
-- Table structure for table `issue`
--

CREATE TABLE `issue` (
  `id` bigint(20) NOT NULL,
  `open_datetime` datetime DEFAULT NULL,
  `id_task` bigint(20) NOT NULL,
  `id_developer` bigint(20) NOT NULL,
  `observations` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `value` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `issue`
--

INSERT INTO `issue` (`id`, `open_datetime`, `id_task`, `id_developer`, `observations`, `value`) VALUES
(1, '2022-09-25 00:00:00', 2, 1, 'example observation 1', 6),
(2, '2022-09-10 00:00:00', 2, 2, 'example observation 2', 4),
(3, '2022-10-01 00:00:00', 2, 3, 'example observation 3', 1),
(4, '2022-10-06 00:00:00', 3, 4, 'example observation 4', 9),
(5, '2022-10-15 00:00:00', 4, 5, 'example observation 5', 5),
(6, '2022-10-25 00:00:00', 6, 6, 'example observation 6', 2),
(7, '2022-11-11 00:00:00', 3, 7, 'example observation 7', 0),
(8, '2022-11-25 00:00:00', 8, 8, 'example observation 8', 3),
(9, '2022-12-02 00:00:00', 9, 9, 'example observation 9', 2),
(10, '2022-12-15 00:00:00', 2, 10, 'example observation 10', 3);

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE `project` (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `id_team` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `project`
--

INSERT INTO `project` (`id`, `code`, `description`, `url`, `id_team`) VALUES
(1, 'aabb', 'Example1', 'https://example1/andamios.net', 1),
(2, 'aaabbb', 'Example2', 'https://example2/andamios.net', 1),
(3, 'ccdd', 'Example3', 'https://example3/andamios.net', 1),
(4, 'cccddd', 'Example4', 'https://example4/andamios.net', 1),
(5, 'eeff', 'Example5', 'https://example5/andamios.net', 1),
(6, 'eeefff', 'Example6', 'https://example6/andamios.net', 1),
(7, 'gghh', 'Example7', 'https://example7/andamios.net', 1),
(8, 'ggghhh', 'Example8', 'https://example8/andamios.net', 1),
(9, 'iijj', 'Example9', 'https://example9/andamios.net', 1),
(10, 'iiijjj', 'Example10', 'https://example10/andamios.net', 1);

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE `task` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `id_project` bigint(20) NOT NULL,
  `priority` int(11) NOT NULL DEFAULT '0',
  `complexity` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`id`, `description`, `id_project`, `priority`, `complexity`) VALUES
(1, 'SQL db test', 2, 4, 8),
(2, 'Inno db is cool', 4, 3, 9),
(3, 'administrador SQL test', 2, 4, 8),
(4, 'MongoDB', 2, 4, 8),
(5, 'Hola mundo!', 4, 4, 8),
(6, 'Adios Mundo!', 6, 3, 8),
(7, 'Say hello!', 9, 5, 8),
(8, 'My cat bigotillos', 2, 4, 8),
(9, 'The mexican', 9, 9, 8),
(10, 'Another one', 1, 1, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `developer`
--
ALTER TABLE `developer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `issue`
--
ALTER TABLE `issue`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `developer`
--
ALTER TABLE `developer`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `issue`
--
ALTER TABLE `issue`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `project`
--
ALTER TABLE `project`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `task`
--
ALTER TABLE `task`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
