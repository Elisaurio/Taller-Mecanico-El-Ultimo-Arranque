<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Repuesto</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Repuesto</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">

        <h2>${empty repuesto ? 'Nuevo repuesto' : 'Editar repuesto'}</h2>

        <a class="btn btn-secondary"
           href="${pageContext.request.contextPath}/repuestos">
            Regresar
        </a>

    </div>

    <div class="card">

        <form method="post"
              action="${pageContext.request.contextPath}/repuestos">

            <input type="hidden"
                   name="id"
                   value="${empty repuesto ? 0 : repuesto.id}">

            <div class="form-grid">

                <div class="form-group">
                    <label>Nombre</label>
                    <input type="text"
                           name="nombre"
                           value="${repuesto.nombre}"
                           required>
                </div>

                <div class="form-group">
                    <label>Precio</label>
                    <input type="number"
                           name="precio"
                           value="${repuesto.precio}"
                           min="0"
                           step="0.01"
                           required>
                </div>

                <div class="form-group full">
                    <label>Descripción</label>
                    <textarea name="descripcion">${repuesto.descripcion}</textarea>
                </div>

                <div class="form-group">
                    <label>Existencia</label>
                    <input type="number"
                           name="existencia"
                           value="${repuesto.existencia}"
                           min="0"
                           required>
                </div>

                <div class="form-group full">

                    <div class="actions">

                        <button class="btn btn-primary" type="submit">
                            Guardar
                        </button>

                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/repuestos">
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