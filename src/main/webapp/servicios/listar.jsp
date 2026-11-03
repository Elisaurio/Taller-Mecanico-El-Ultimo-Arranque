<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Servicios</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Servicios</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">

        <h2>Servicios</h2>

        <a class="btn btn-primary"
           href="${pageContext.request.contextPath}/servicios?accion=nuevo">
            Nuevo servicio
        </a>

    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card table-container">

        <table class="table">

            <thead>
            <tr>
                <th>ID</th>
                <th>Descripción</th>
                <th>Costo mano de obra</th>
                <th>Acciones</th>
            </tr>
            </thead>

            <tbody>

            <c:forEach var="servicio" items="${servicios}">

                <tr>

                    <td>${servicio.id}</td>
                    <td>${servicio.descripcion}</td>
                    <td>Q ${servicio.costoManoObra}</td>

                    <td class="actions">

                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/servicios?accion=editar&id=${servicio.id}">
                            Editar
                        </a>

                        <a class="btn btn-danger"
                           href="${pageContext.request.contextPath}/servicios?accion=eliminar&id=${servicio.id}"
                           onclick="return confirm('¿Eliminar este servicio?');">
                            Eliminar
                        </a>

                    </td>

                </tr>

            </c:forEach>

            </tbody>

        </table>

    </div>

</main>

</body>
</html>