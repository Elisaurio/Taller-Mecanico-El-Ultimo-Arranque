<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Cliente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Cliente</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">
        <h2>${empty cliente ? 'Nuevo cliente' : 'Editar cliente'}</h2>

        <a class="btn btn-secondary"
           href="${pageContext.request.contextPath}/clientes">
            Regresar
        </a>
    </div>

    <div class="card">

        <form method="post"
              action="${pageContext.request.contextPath}/clientes">

            <input type="hidden" name="id"
                   value="${empty cliente ? 0 : cliente.id}">

            <div class="form-grid">

                <div class="form-group">
                    <label>Nombre</label>
                    <input type="text" name="nombre"
                           value="${cliente.nombre}" required>
                </div>

                <div class="form-group">
                    <label>Teléfono</label>
                    <input type="text" name="telefono"
                           value="${cliente.telefono}">
                </div>

                <div class="form-group">
                    <label>Identificación</label>
                    <input type="text" name="identificacion"
                           value="${cliente.identificacion}" required>
                </div>

                <div class="form-group">
                    <label>Dirección</label>
                    <input type="text" name="direccion"
                           value="${cliente.direccion}">
                </div>

                <div class="form-group full">
                    <div class="actions">
                        <button class="btn btn-primary" type="submit">
                            Guardar
                        </button>

                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/clientes">
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