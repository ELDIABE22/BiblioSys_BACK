-- PROCEDIMIENTO PARA EJECUTAR LA VISTA vw_ObtenerLibrosConAutoresYMaterias
CREATE PROCEDURE sp_ObtenerLibrosConAutoresYMaterias
AS
BEGIN
    SELECT * 
    FROM vw_ObtenerLibrosConAutoresYMaterias;
END;
GO

-- PROCEDIMIENTO PARA INSERTAR AUTORES DEL LIBRO
CREATE PROCEDURE sp_InsertarLibroAutor
    @IdLibro INT,
    @IdAutor INT
AS
BEGIN
    -- Validar si el IdLibro existe en la tabla Libro
    IF NOT EXISTS (SELECT 1 FROM Libro WHERE Id = @IdLibro)
    BEGIN
        PRINT 'El libro no existe.';
        RETURN;
    END

    -- Validar si el IdAutor existe en la tabla Autor
    IF NOT EXISTS (SELECT 1 FROM Autor WHERE Id = @IdAutor)
    BEGIN
        PRINT 'El autor no existe.';
        RETURN;
    END

    -- Validar si la relación ya existe en la tabla LibroAutor
    IF EXISTS (SELECT 1 FROM LibroAutor WHERE IdLibro = @IdLibro AND IdAutor = @IdAutor)
    BEGIN
        PRINT 'El libro ya tiene el autor guardado.';
        RETURN;
    END

    INSERT INTO LibroAutor (IdLibro, IdAutor)
    VALUES (@IdLibro, @IdAutor);

    PRINT 'Autor del libro guardado exitosamente.';
END;
GO

-- PROCEDIMIENTO PARA INSERTAR MATERIAS DEL LIBRO
CREATE PROCEDURE sp_InsertarLibroMateria
    @IdLibro INT,
    @IdMateria INT
AS
BEGIN
    -- Validar si el IdLibro existe en la tabla Libro
    IF NOT EXISTS (SELECT 1 FROM Libro WHERE Id = @IdLibro)
    BEGIN
        PRINT 'El libro no existe.';
        RETURN;
    END

    -- Validar si el IdMateria existe en la tabla Materia
    IF NOT EXISTS (SELECT 1 FROM Materia WHERE Id = @IdMateria)
    BEGIN
        PRINT 'La materia no existe.';
        RETURN;
    END

    -- Validar si la relación ya existe en la tabla LibroMateria
    IF EXISTS (SELECT 1 FROM LibroMateria WHERE IdLibro = @IdLibro AND IdMateria = @IdMateria)
    BEGIN
        PRINT 'El libro ya tiene la materia asignada.';
        RETURN;
    END

    INSERT INTO LibroMateria (IdLibro, IdMateria)
    VALUES (@IdLibro, @IdMateria);

    PRINT 'Materia del libro guardada exitosamente.';
END;
GO

-- PROCEDIMIENTO PARA INSERTAR LIBRO
CREATE PROCEDURE sp_InsertarLibro
    @Titulo VARCHAR(150),
    @Descripcion VARCHAR(255),
    @ISBN VARCHAR(20),
    @Genero VARCHAR(50),
    @Año_Publicacion INT,
    @Foto VARCHAR(255) = NULL,
	@NewBookId INT OUTPUT,
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Validar si el ISBN ya existe en la tabla
    IF EXISTS (SELECT 1 FROM Libro WHERE ISBN = @ISBN)
    BEGIN
        SET @MensajeSalida = 'El ISBN ya está registrado.';
        RETURN;
    END

    INSERT INTO Libro (Titulo, Descripcion, ISBN, Genero, Año_Publicacion, Foto)
    VALUES (@Titulo, @Descripcion, @ISBN, @Genero, @Año_Publicacion, @Foto);

	SET @NewBookId = SCOPE_IDENTITY();

    SET @MensajeSalida = 'Libro creado exitosamente.';
END;
GO

-- PROCEDIMIENTO PARA ELIMINAR LIBRO
CREATE PROCEDURE sp_EliminarLibro
    @LibroId INT,
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Verificar si el libro existe
    IF NOT EXISTS (SELECT 1 FROM Libro WHERE Id = @LibroId)
    BEGIN
        SET @MensajeSalida = 'El libro no existe.';
        RETURN;
    END

    -- Eliminar relaciones con autores y materias si existen
    DELETE FROM LibroAutor WHERE IdLibro = @LibroId;
    DELETE FROM LibroMateria WHERE IdLibro = @LibroId;

    DELETE FROM Prestamo WHERE IdLibro = @IdLibro;

    -- Eliminar el libro
    DELETE FROM Libro WHERE Id = @LibroId;

    SET @MensajeSalida = 'Libro eliminado.';
END;
GO

-- PROCEDIMIENTO PARA ACTUALIZAR LIBRO
CREATE PROCEDURE sp_ActualizarLibro
    @LibroId INT,
    @NuevoTitulo VARCHAR(150),
    @NuevaDescripcion VARCHAR(255),
    @NuevoISBN VARCHAR(20),
    @NuevoGenero VARCHAR(50),
    @NuevoAño_Publicacion INT,
    @NuevaFoto VARCHAR(255) = NULL,
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Verificar si el libro con el ID existe
    IF NOT EXISTS (SELECT 1 FROM Libro WHERE Id = @LibroId)
    BEGIN
        SET @MensajeSalida = 'El libro no existe.';
        RETURN;
    END

    -- Validar si el ISBN ya existe en otro libro
    IF EXISTS (SELECT 1 FROM Libro WHERE ISBN = @NuevoISBN AND Id <> @LibroId)
    BEGIN
        SET @MensajeSalida = 'El ISBN ya está registrado.';
        RETURN;
    END

    UPDATE Libro
    SET 
        Titulo = @NuevoTitulo,
        Descripcion = @NuevaDescripcion,
        ISBN = @NuevoISBN,
        Genero = @NuevoGenero,
        Año_Publicacion = @NuevoAño_Publicacion,
        Foto = @NuevaFoto
    WHERE Id = @LibroId;

    SET @MensajeSalida = 'Libro actualizado.';
END;
GO

-- PROCEDIMIENTO PARA ELIMINAR AUTORES DEL LIBRO
CREATE PROCEDURE sp_EliminarLibroAutor
	@IdLibro INT,
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
	SET @MensajeSalida = '';

    -- Validar si el libro existe 
    IF NOT EXISTS (SELECT 1 FROM Libro WHERE Id = @IdLibro)
    BEGIN
        SET @MensajeSalida = 'El libro no existe.';
        RETURN;
    END

	-- Borra todos los libros para despues actualizarlos
	DELETE FROM LibroAutor WHERE IdLibro = @IdLibro;

	SET @MensajeSalida = 'Autores del libro eliminados.';
END;
GO

-- PROCEDIMIENTO PARA ELIMINAR MATERIAS DEL LIBRO
CREATE PROCEDURE sp_EliminarLibroMateria
	@IdLibro INT,
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
	SET @MensajeSalida = '';

    -- Validar si el libro existe 
    IF NOT EXISTS (SELECT 1 FROM Libro WHERE Id = @IdLibro)
    BEGIN
        SET @MensajeSalida = 'El libro no existe.';
        RETURN;
    END

	-- Borra todos los libros para despues actualizarlos
	DELETE FROM LibroMateria WHERE IdLibro = @IdLibro;

	SET @MensajeSalida = 'Materias del libro eliminadas.';
END;
GO

-- PROCEDIMIENTO PARA INSERTAR ESTUDIANTES
CREATE PROCEDURE sp_InsertarEstudiante
    @Nombres VARCHAR(255),
    @Apellidos VARCHAR(255),
    @Correo VARCHAR(50),
    @Direccion VARCHAR(255),
    @Telefono INT,
    @Carrera VARCHAR(100),
    @Foto VARCHAR(255) = NULL,
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Validar si el correo ya existe
    IF EXISTS (SELECT 1 FROM Estudiante WHERE Correo = @Correo)
    BEGIN
        SET @MensajeSalida = 'El correo ya está registrado.';
        RETURN;
    END

    -- Validar si el teléfono ya existe
    IF EXISTS (SELECT 1 FROM Estudiante WHERE Telefono = @Telefono)
    BEGIN
        SET @MensajeSalida = 'El teléfono ya está registrado.';
        RETURN;
    END

    -- Insertar el nuevo estudiante
    INSERT INTO Estudiante (Nombres, Apellidos, Correo, Direccion, Telefono, Carrera, Foto)
    VALUES (@Nombres, @Apellidos, @Correo, @Direccion, @Telefono, @Carrera, @Foto);

    SET @MensajeSalida = 'Estudiante agregado.';
END;
GO

-- PROCEDIMIENTO PARA ELIMINAR USUARIOS
CREATE PROCEDURE sp_EliminarUsuario
    @Id INT,
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Validar si el usuario existe
    IF NOT EXISTS (SELECT 1 FROM Usuario WHERE Id = @Id)
    BEGIN
        SET @MensajeSalida = 'El usuario no existe.';
        RETURN;
    END

    -- Eliminar el usuario
    DELETE FROM Usuario WHERE Id = @Id;

    SET @MensajeSalida = 'Usuario eliminado.';
END;
GO

-- PROCEDIMIENTO PARA INSERTAR USUARIO
CREATE PROCEDURE sp_InsertarUsuario
    @Nombres VARCHAR(255),
    @Apellidos VARCHAR(255),
    @Usuario VARCHAR(10),
    @Correo VARCHAR(50),
	@Rol VARCHAR(50),
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Validar si el usuario ya está registrado
    IF EXISTS (SELECT 1 FROM Usuario WHERE Usuario = @Usuario)
    BEGIN
        SET @MensajeSalida = 'El usuario ya está registrado.';
        RETURN;
    END

    -- Validar si el correo ya está registrado
    IF EXISTS (SELECT 1 FROM Usuario WHERE Correo = @Correo)
    BEGIN
        SET @MensajeSalida = 'El correo ya está registrado.';
        RETURN;
    END

    -- Generar una contraseña temporal
    DECLARE @ContraseñaTemporal VARCHAR(255);
    SET @ContraseñaTemporal = 'Temp' + CAST(NEWID() AS VARCHAR(50));

    INSERT INTO Usuario (Nombres, Apellidos, Usuario, Correo, Contraseña, rol)
    VALUES (@Nombres, @Apellidos, @Usuario, @Correo, @ContraseñaTemporal, @Rol);

    SET @MensajeSalida = 'Usuario registrado';
END;
GO

-- PROCEDIMIENTO PARA ACTUALIZAR USUARIO
CREATE PROCEDURE sp_ActualizarUsuario
    @Id INT,
    @Nombres VARCHAR(255),
    @Apellidos VARCHAR(255),
    @Usuario VARCHAR(10),
    @Correo VARCHAR(50),
    @Rol VARCHAR(50),
    @Estado VARCHAR(50),
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Verificar si el usuario existe
    IF NOT EXISTS (SELECT 1 FROM Usuario WHERE Id = @Id)
    BEGIN
        SET @MensajeSalida = 'El usuario no existe.';
        RETURN;
    END

    -- Validar si el nombre de usuario ya está en uso
    IF EXISTS (SELECT 1 FROM Usuario WHERE Usuario = @Usuario AND Id <> @Id)
    BEGIN
        SET @MensajeSalida = 'El nombre de usuario ya está en uso.';
        RETURN;
    END

    UPDATE Usuario
    SET Nombres = @Nombres,
        Apellidos = @Apellidos,
        Usuario = @Usuario,
        Correo = @Correo,
        rol = @Rol,
        Estado = @Estado
    WHERE Id = @Id;

    SET @MensajeSalida = 'Usuario actualizado.';
END;
GO

-- PROCEDIMIENTO PARA ELIMINAR AUTOR
CREATE PROCEDURE sp_EliminarAutor
    @IdAutor INT,
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Verificar si el autor existe
    IF NOT EXISTS (SELECT 1 FROM Autor WHERE Id = @IdAutor)
    BEGIN
        SET @MensajeSalida = 'Autor no encontrado.';
        RETURN;
    END

    -- Verificar si hay libros asociados en LibroAutor
    IF EXISTS (SELECT 1 FROM LibroAutor WHERE IdAutor = @IdAutor)
    BEGIN
        SET @MensajeSalida = 'No se puede eliminar el autor. Primero elimine los libros asociados.';
        RETURN;
    END

    DELETE FROM Autor WHERE Id = @IdAutor;

    SET @MensajeSalida = 'Autor eliminado.';
END;
GO

-- PROCEDIMIENTO PARA ELIMINAR MATERIA
CREATE PROCEDURE sp_EliminarMateria
    @IdMateria INT,
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Verificar si la materia existe
    IF NOT EXISTS (SELECT 1 FROM Materia WHERE Id = @IdMateria)
    BEGIN
        SET @MensajeSalida = 'Materia no encontrada.';
        RETURN;
    END

    -- Verificar si hay libros asociados en LibroMateria
    IF EXISTS (SELECT 1 FROM LibroMateria WHERE IdMateria = @IdMateria)
    BEGIN
        SET @MensajeSalida = 'No se puede eliminar la materia. Primero elimine los libros asociados.';
        RETURN;
    END

    DELETE FROM Materia WHERE Id = @IdMateria;

    SET @MensajeSalida = 'Materia eliminada.';
END;
GO

-- PROCEDIMIENTO PARA ACTUALIZAR MATERIA
CREATE PROCEDURE sp_ActualizarMateria
    @IdMateria INT,
    @Nombre VARCHAR(150),
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Verificar si la materia existe
    IF NOT EXISTS (SELECT 1 FROM Materia WHERE Id = @IdMateria)
    BEGIN
        SET @MensajeSalida = 'La materia no existe.';
        RETURN;
    END

    -- Validar si el nombre de la materia ya está en uso
    IF NOT EXISTS (SELECT 1 FROM Materia WHERE Id = @IdMateria AND LOWER(Nombre) = LOWER(@Nombre) )
    BEGIN
        SET @MensajeSalida = 'El nombre de la materia ya está registrado.';
        RETURN;
    END

    UPDATE Materia
    SET 
        Nombre = @Nombre
    WHERE Id = @IdMateria;

    SET @MensajeSalida = 'Materia actualizada.';
END;
GO

-- PROCEDIMIENTO PARA INSERTAR PRESTAMO
CREATE PROCEDURE sp_InsertarPrestamo
    @IdLibro INT,
    @IdEstudiante INT,
    @FechaPrestamo DATE,
    @FechaDevolucion DATE,
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Validar si el libro existe
    IF NOT EXISTS (SELECT 1 FROM Libro WHERE Id = @IdLibro)
    BEGIN
        SET @MensajeSalida = 'El libro no existe.';
        RETURN;
    END

    -- Validar si el estudiante existe
    IF NOT EXISTS (SELECT 1 FROM Estudiante WHERE Id = @IdEstudiante)
    BEGIN
        SET @MensajeSalida = 'El estudiante no existe.';
        RETURN;
    END

    -- Validar si el libro ya está prestado
    IF EXISTS (SELECT 1 FROM Prestamo WHERE IdLibro = @IdLibro AND Estado != 'Devuelto')
    BEGIN
        SET @MensajeSalida = 'El libro ya está prestado y no ha sido devuelto.';
        RETURN;
    END

    -- Validar si el usuario ya tiene un libro prestado
    IF EXISTS (SELECT 1 FROM Prestamo WHERE IdEstudiante = @IdEstudiante AND Estado != 'Devuelto')
    BEGIN
        SET @MensajeSalida = 'El usuario ya tiene un libro prestado.';
        RETURN;
    END

    INSERT INTO Prestamo (IdLibro, IdEstudiante, FechaPrestamo, FechaDevolucion)
    VALUES (@IdLibro, @IdEstudiante, @FechaPrestamo, @FechaDevolucion);

    -- Actualizar el estado del libro a 'Prestado' 
    UPDATE Libro
    SET Estado = 'Prestado'
    WHERE Id = @IdLibro;

    SET @MensajeSalida = 'Préstamo registrado.';
END;
GO

-- PROCEDIMIENTO PARA EJECUTAR LA VISTA vw_ObtenerPrestamos
CREATE PROCEDURE sp_ObtenerPrestamos
AS
BEGIN
    SELECT * FROM vw_ObtenerPrestamos;
END;
GO

-- PROCEDIMIENTO PARA ELIMINAR PRESTAMO
CREATE PROCEDURE sp_EliminarPrestamo (
@PrestamoId INT,  
@MensajeSalida VARCHAR(255) OUTPUT
)
AS
BEGIN
	SET @MensajeSalida = '';

    -- Verificar si el préstamo existe
    IF NOT EXISTS (SELECT 1 FROM Prestamo WHERE Id = @PrestamoId)
    BEGIN
		SET @MensajeSalida = 'No se encontró el préstamo.';
        RETURN;
    END

    DECLARE @IdLibro INT;

    SELECT @IdLibro = IdLibro FROM Prestamo WHERE Id = @PrestamoId;

    DELETE FROM Prestamo WHERE Id = @PrestamoId;

    UPDATE Libro SET Estado = 'Disponible' WHERE Id = @IdLibro;

	SET @MensajeSalida = 'Préstamo eliminado.';
END;
GO

-- PROCEDIMIENTO PARA ACTUALIZAR PRESTAMO
CREATE PROCEDURE sp_ActualizarPrestamo
    @IdPrestamo INT,
    @IdLibro INT,
    @IdEstudiante INT,
    @FechaPrestamo DATE,
    @FechaDevolucion DATE,
	@Estado VARCHAR(20),
    @MensajeSalida VARCHAR(255) OUTPUT
AS
BEGIN
    SET @MensajeSalida = '';

    -- Verificar si el préstamo existe
    IF NOT EXISTS (SELECT 1 FROM Prestamo WHERE Id = @IdPrestamo)
    BEGIN
        SET @MensajeSalida = 'El préstamo no existe.';
        RETURN;
    END

    -- Validar si el libro existe
    IF NOT EXISTS (SELECT 1 FROM Libro WHERE Id = @IdLibro)
    BEGIN
        SET @MensajeSalida = 'El libro no existe.';
        RETURN;
    END

    -- Validar si el estudiante existe
    IF NOT EXISTS (SELECT 1 FROM Estudiante WHERE Id = @IdEstudiante)
    BEGIN
        SET @MensajeSalida = 'El estudiante no existe.';
        RETURN;
    END

    -- Validar si el libro ya está prestado
    IF EXISTS (SELECT 1 FROM Prestamo WHERE IdLibro = @IdLibro AND IdEstudiante != @IdEstudiante AND Estado != 'Devuelto')
    BEGIN
        SET @MensajeSalida = 'El libro ya está prestado y no ha sido devuelto.';
        RETURN;
    END

    UPDATE Prestamo
    SET 
        IdLibro = @IdLibro,
        IdEstudiante = @IdEstudiante,
        FechaPrestamo = @FechaPrestamo,
        FechaDevolucion = @FechaDevolucion,
        Estado = @Estado
    WHERE Id = @IdPrestamo;

    -- Actualizar el estado del libro 
    IF @Estado = 'Devuelto' 
    BEGIN 
        UPDATE Libro 
        SET Estado = 'Disponible'
        WHERE Id = @IdLibro;
    END
    ELSE 
    BEGIN
        UPDATE Libro
        SET Estado = 'Prestado' 
        WHERE Id = @IdLibro; 
    END

    SET @MensajeSalida = 'Préstamo actualizado.';
END;
GO

-- PROCEDIMIENTO PARA EJECUTAR LA VISTA vw_DetallesPanel
CREATE PROCEDURE sp_DetallesPanel
AS
BEGIN
    SELECT * FROM vw_DetallesPanel;
END;
GO

-- PROCEDIMIENTO PARA EJECUTAR LA VISTA vw_ObtenerPrestamosVencidos
CREATE PROCEDURE sp_ObtenerPrestamosVencidos
AS
BEGIN
    SELECT * FROM vw_ObtenerPrestamosVencidos;
END;
GO

-- PROCEDIMIENTO PARA ACTUALIZAR ESTADO DEL PRESTAMO Y LIBRO
CREATE PROCEDURE sp_ActualizarPrestamosVencidos
AS
BEGIN
    UPDATE Prestamo
    SET Estado = 'Vencido'
    WHERE FechaDevolucion < GETDATE() AND Estado != 'Devuelto';

    UPDATE Libro
    SET Estado = 'Prestado'
    WHERE Id IN (
        SELECT IdLibro FROM Prestamo
        WHERE FechaDevolucion < GETDATE() AND Estado = 'Vencido'
    );
END;
GO




