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

-- VISTA PARA OBTENER TOTAL DEL PRESTAMO, LIBRO, ESTUDIANTE Y USUARIOS
CREATE VIEW vw_DetallesPanel
AS
SELECT 
    (SELECT COUNT(*) FROM Prestamo) AS TotalPrestamos,
    (SELECT COUNT(*) FROM Libro) AS TotalLibros,
    (SELECT COUNT(*) FROM Estudiante) AS TotalEstudiantes,
    (SELECT COUNT(*) FROM Usuario) AS TotalUsuarios;

