-- VISTA PARA OBTENER LIBROS CON SUS AUTORES Y MATERIAS
CREATE VIEW vw_ObtenerLibrosConAutoresYMaterias
AS
    SELECT 
        l.Id AS BookId,
        l.Titulo,
        l.Descripcion,
        l.ISBN,
        l.Genero,
        l.Año_Publicacion,
        l.Foto,
        l.Estado,
        a.Id AS AuthorId,
        a.Nombre AS AuthorName,
        m.Id AS SubjectId,
        m.Nombre AS SubjectName
    FROM Libro l
    LEFT JOIN LibroAutor la ON la.idLibro = l.Id
    LEFT JOIN Autor a ON la.idAutor = a.Id
    LEFT JOIN LibroMateria lm ON lm.idLibro = l.Id
    LEFT JOIN Materia m ON lm.idMateria = m.Id;
GO

-- VISTA PARA OBTENER PRESTAMOS CON EL ESTUDIANTE Y LIBRO
CREATE VIEW vw_ObtenerPrestamos AS
SELECT 
    p.Id AS PrestamoId,
	p.FechaPrestamo,
    p.FechaDevolucion,
    p.Estado AS PrestamoEstado,
    e.Id AS EstudianteId,
    e.Nombres AS EstudianteNombres,
    e.Apellidos AS EstudianteApellidos,
    l.Id AS LibroId,
    l.Titulo AS LibroTitulo
FROM 
    Prestamo p
JOIN 
    Estudiante e ON p.IdEstudiante = e.Id
JOIN 
    Libro l ON p.IdLibro = l.Id;
GO

-- VISTA PARA OBTENER LOS PRESTAMOS VENCIDOS
CREATE VIEW vw_ObtenerPrestamosVencidos AS
SELECT 
    E.Id AS idEstudiante,
	L.Id AS idLibro,
    E.Nombres AS nombresEstudiante,
    E.Apellidos AS apellidosEstudiante,
    E.Correo AS correoEstudiante,
    P.FechaPrestamo AS fechaPrestamo,
    P.FechaDevolucion AS fechaDevolucion
FROM
    Prestamo P
INNER JOIN 
    Estudiante E ON P.IdEstudiante = E.Id
INNER JOIN
	Libro L ON P.IdLibro = L.Id
WHERE 
    P.Estado = 'Vencido'
    OR (P.FechaDevolucion < GETDATE() AND P.Estado != 'Devuelto');
GO

-- VISTA PARA OBTENER TOTAL DEL PRESTAMO, LIBRO, ESTUDIANTE Y USUARIOS
CREATE VIEW vw_DetallesPanel
AS
SELECT 
    (SELECT COUNT(*) FROM Prestamo) AS TotalPrestamos,
    (SELECT COUNT(*) FROM Libro) AS TotalLibros,
    (SELECT COUNT(*) FROM Estudiante) AS TotalEstudiantes,
    (SELECT COUNT(*) FROM Usuario) AS TotalUsuarios;

-- VISTA PARA OBTENER LA CANTIDAD DE PRESTAMOS POR DIA
CREATE VIEW vw_CantidadPrestamosPorDia
AS
SELECT 
    FechaPrestamo AS Dia,
    COUNT(*) AS CantidadPrestamos
FROM 
    Prestamo
GROUP BY 
    FechaPrestamo;
GO

-- VISTA PARA OBTENER LA CANTIDAD DE PRESTAMOS POR MES
CREATE VIEW vw_CantidadPrestamosPorMes
AS
SELECT 
    MONTH(FechaPrestamo) AS Mes,
    COUNT(*) AS CantidadPrestamos
FROM 
    Prestamo
GROUP BY 
    YEAR(FechaPrestamo),
    MONTH(FechaPrestamo);
GO

-- VISTA PARA OBTENER LA TOP 5 LIBROS MAS PRESTADOS
CREATE VIEW Top5LibrosMasPrestados AS
SELECT TOP 5
    L.Titulo AS NombreDelLibro,
    COUNT(P.Id) AS CantidadDePrestamos
FROM 
    Libro L
JOIN 
    Prestamo P ON L.Id = P.IdLibro
GROUP BY 
    L.Titulo
ORDER BY 
    CantidadDePrestamos DESC;
GO


