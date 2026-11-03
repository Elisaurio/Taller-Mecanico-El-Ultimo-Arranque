<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mecánico</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Mecánico</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">

        <h2>${empty mecanico ? 'Nuevo mecánico' : 'Editar mecánico'}</h2>

        <a class="btn btn-secondary"
           href="${pageContext.request.contextPath}/mecanicos">
            Regresar
        </a>

    </div>

    <div class="card">

        <form method="post"
              action="${pageContext.request.contextPath}/mecanicos">

            <input type="hidden"
                   name="id"
                   value="${empty mecanico ? 0 : mecanico.id}">

            <div class="form-grid">

                <div class="form-group">
                    <label>Nombre</label>
                    <input type="text"
                           name="nombre"
                           value="${mecanico.nombre}"
                           required>
                </div>

                <div class="form-group">
                    <label>Teléfono</label>
                    <input type="text"
                           name="telefono"
                           value="${mecanico.telefono}">
                </div>

                <div class="form-group">
                    <label>Especialidad</label>
                    <input type="text"
                           name="especialidad"
                           value="${mecanico.especialidad}">
                </div>

                <div class="form-group full">

                    <div class="actions">

                        <button class="btn btn-primary" type="submit">
                            Guardar
                        </button>

                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/mecanicos">
                            Cancelar
                        </a>

                    </div>

                </div>

            </div>

        </form>

    </div>

</main>

</body>
</html>