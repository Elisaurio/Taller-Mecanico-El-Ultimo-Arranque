<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Servicio</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Servicio</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">

        <h2>${empty servicio ? 'Nuevo servicio' : 'Editar servicio'}</h2>

        <a class="btn btn-secondary"
           href="${pageContext.request.contextPath}/servicios">
            Regresar
        </a>

    </div>

    <div class="card">

        <form method="post"
              action="${pageContext.request.contextPath}/servicios">

            <input type="hidden"
                   name="id"
                   value="${empty servicio ? 0 : servicio.id}">

            <div class="form-grid">

                <div class="form-group full">
                    <label>Descripción</label>

                    <input type="text"
                           name="descripcion"
                           value="${servicio.descripcion}"
                           required>
                </div>

                <div class="form-group">
                    <label>Costo mano de obra</label>

                    <input type="number"
                           name="costoManoObra"
                           value="${servicio.costoManoObra}"
                           min="0"
                           step="0.01"
                           required>
                </div>

                <div class="form-group full">

                    <div class="actions">

                        <button class="btn btn-primary" type="submit">
                            Guardar
                        </button>

                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/servicios">
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