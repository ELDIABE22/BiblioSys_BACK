-- SQLServer
CREATE DATABASE BiblioSys;
GO

USE BiblioSys;
GO

CREATE TABLE Usuario (
	Id INT IDENTITY(1,1) PRIMARY KEY,
	Nombres VARCHAR(255) NOT NULL,
	Apellidos VARCHAR(255) NOT NULL,
	Usuario VARCHAR(10) UNIQUE NOT NULL,
	Correo VARCHAR(50) UNIQUE NOT NULL,
	Contraseña VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL DEFAULT 'Asistente', -- Puede ser 'Administrador' o 'Asistente'
	Estado VARCHAR(50) NOT NULL DEFAULT 'Activo', -- Puede ser 'Activo' o 'Inactivo'
	Creado_fecha DATETIME DEFAULT GETDATE(),
    Actualizado_fecha DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE Estudiante (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Nombres VARCHAR(255) NOT NULL,
    Apellidos VARCHAR(255) NOT NULL,
    Correo VARCHAR(50) UNIQUE NOT NULL,
    Direccion VARCHAR(255) NOT NULL,
    Telefono CHAR(10) UNIQUE NOT NULL,
    Carrera VARCHAR(100) NOT NULL,
    Foto VARCHAR(255),
    Estado VARCHAR(50) NOT NULL DEFAULT 'Activo', -- Puede ser 'Activo' o 'Inactivo'
    Creado_fecha DATETIME DEFAULT GETDATE(),
    Actualizado_fecha DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE Autor (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Nombre VARCHAR(255) NOT NULL,
	Creado_fecha DATETIME DEFAULT GETDATE(),
    Actualizado_fecha DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE Materia (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Nombre VARCHAR(150) NOT NULL UNIQUE,
	Creado_fecha DATETIME DEFAULT GETDATE(),
    Actualizado_fecha DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE Libro (
	Id INT PRIMARY KEY IDENTITY(1,1),
    Titulo VARCHAR(150) NOT NULL,
	Descripcion VARCHAR(255) NOT NULL,
    ISBN VARCHAR(10) UNIQUE NOT NULL,
    Genero VARCHAR(50) NOT NULL,
    Año_Publicacion INT NOT NULL,
	Foto VARCHAR(255),
    Estado VARCHAR(20) DEFAULT 'Disponible', -- Puede ser 'Disponible' o 'Prestado'
	Creado_fecha DATETIME DEFAULT GETDATE(),
    Actualizado_fecha DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE Prestamo (
    Id INT PRIMARY KEY IDENTITY(1,1),
    IdEstudiante INT NOT NULL,
    IdLibro INT NOT NULL,
    FechaPrestamo DATE,
    FechaDevolucion DATE,
    Estado VARCHAR(20) DEFAULT 'Activo', -- Puede ser 'Activo', 'Devuelto', 'Vencido'
    FOREIGN KEY (IdEstudiante) REFERENCES Estudiante(Id),
    FOREIGN KEY (IdLibro) REFERENCES Libro(Id)
);
GO

CREATE TABLE LibroAutor (
    IdLibro INT,
    IdAutor INT,
    PRIMARY KEY (IdLibro, IdAutor),
    FOREIGN KEY (IdLibro) REFERENCES Libro(Id),
    FOREIGN KEY (IdAutor) REFERENCES Autor(Id)
);
GO

CREATE TABLE LibroMateria (
    IdLibro INT,
    IdMateria INT,
    PRIMARY KEY (IdLibro, IdMateria),
    FOREIGN KEY (IdLibro) REFERENCES Libro(Id),
    FOREIGN KEY (IdMateria) REFERENCES Materia(Id)
);
GO