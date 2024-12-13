-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 12, 2024 at 03:38 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS `librarymanagement`;
USE `librarymanagement`;


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `librarymanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `author`
--

CREATE TABLE `author` (
  `ID` int(11) NOT NULL,
  `Firstname` varchar(50) NOT NULL,
  `Lastname` varchar(50) NOT NULL,
  `Description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `author`
--

INSERT INTO `author` (`ID`, `Firstname`, `Lastname`, `Description`) VALUES
(1, 'Harper', 'Lee', 'Harper Lee'),
(4, 'Various', 'Contributors', 'Various Contributors');

-- --------------------------------------------------------

--
-- Table structure for table `borrow`
--

CREATE TABLE `borrow` (
  `ID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `StaffID` int(11) NOT NULL,
  `BorrowDate` datetime NOT NULL,
  `ReturnDate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrow`
--

INSERT INTO `borrow` (`ID`, `UserID`, `StaffID`, `BorrowDate`, `ReturnDate`) VALUES
(1, 1, 1, '2024-11-10 23:38:27', '2024-11-14 05:38:27'),
(8, 1, 1, '2024-11-11 00:00:00', '2024-11-18 00:00:00'),
(9, 1, 1, '2024-11-12 00:00:00', '2024-11-28 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `borrowdetail`
--

CREATE TABLE `borrowdetail` (
  `ID` int(11) NOT NULL,
  `BorrowID` int(11) NOT NULL,
  `DocumentID` int(11) NOT NULL,
  `Status` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrowdetail`
--

INSERT INTO `borrowdetail` (`ID`, `BorrowID`, `DocumentID`, `Status`) VALUES
(1, 1, 2, 0),
(2, 1, 3, 1),
(6, 8, 2, 1),
(7, 8, 4, 1),
(8, 9, 3, 0),
(9, 9, 4, 0);

-- --------------------------------------------------------

--
-- Table structure for table `document`
--

CREATE TABLE `document` (
  `ID` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `AuthorID` int(11) NOT NULL,
  `Price` double NOT NULL,
  `Description` text DEFAULT NULL,
  `Type` varchar(100) DEFAULT NULL,
  `Quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `document`
--

INSERT INTO `document` (`ID`, `Name`, `AuthorID`, `Price`, `Description`, `Type`, `Quantity`) VALUES
(2, 'To Kill a Mockingbird', 1, 12, 'Một câu chuyện', 'BOOK', 2),
(3, 'Exploring the Roots of Social Injustice in the American South', 1, 123, 'Một nghiên cứu', 'THESIS', 3),
(4, 'National Geographic', 4, 1234, 'Tạp chí nổi tiếng', 'MAGAZINE', 5);

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

CREATE TABLE `staff` (
  `ID` int(11) NOT NULL,
  `Firstname` varchar(50) NOT NULL,
  `Lastname` varchar(50) NOT NULL,
  `Address` text DEFAULT NULL,
  `Phonenumber` varchar(10) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`ID`, `Firstname`, `Lastname`, `Address`, `Phonenumber`, `Username`, `Password`) VALUES
(1, 'Lao Van', 'Hung', 'Lang Son', '0766425669', 'admin123', '123456');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `ID` int(11) NOT NULL,
  `Firstname` varchar(50) NOT NULL,
  `Lastname` varchar(50) NOT NULL,
  `Address` text NOT NULL,
  `Phonenumber` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID`, `Firstname`, `Lastname`, `Address`, `Phonenumber`) VALUES
(1, 'Lao Văn', 'Hùng', 'Hà Nội', '0766425669');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `author`
--
ALTER TABLE `author`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `borrow`
--
ALTER TABLE `borrow`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `UserID` (`UserID`),
  ADD KEY `StaffID` (`StaffID`);

--
-- Indexes for table `borrowdetail`
--
ALTER TABLE `borrowdetail`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `BorrowID` (`BorrowID`),
  ADD KEY `DocumentID` (`DocumentID`);

--
-- Indexes for table `document`
--
ALTER TABLE `document`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `AuthorID` (`AuthorID`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `author`
--
ALTER TABLE `author`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `borrow`
--
ALTER TABLE `borrow`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `borrowdetail`
--
ALTER TABLE `borrowdetail`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `document`
--
ALTER TABLE `document`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `staff`
--
ALTER TABLE `staff`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `borrow`
--
ALTER TABLE `borrow`
  ADD CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`ID`),
  ADD CONSTRAINT `borrow_ibfk_2` FOREIGN KEY (`StaffID`) REFERENCES `staff` (`ID`);

--
-- Constraints for table `borrowdetail`
--
ALTER TABLE `borrowdetail`
  ADD CONSTRAINT `borrowdetail_ibfk_1` FOREIGN KEY (`BorrowID`) REFERENCES `borrow` (`ID`),
  ADD CONSTRAINT `borrowdetail_ibfk_2` FOREIGN KEY (`DocumentID`) REFERENCES `document` (`ID`);

--
-- Constraints for table `document`
--
ALTER TABLE `document`
  ADD CONSTRAINT `document_ibfk_1` FOREIGN KEY (`AuthorID`) REFERENCES `author` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
