<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mecánicos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Mecánicos</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">
        <h2>Mecánicos</h2>

        <a class="btn btn-primary"
           href="${pageContext.request.contextPath}/mecanicos?accion=nuevo">
            Nuevo mecánico
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
                <th>Nombre</th>
                <th>Teléfono</th>
                <th>Especialidad</th>
                <th>Acciones</th>
            </tr>
            </thead>

            <tbody>

            <c:forEach var="mecanico" items="${mecanicos}">
                <tr>

                    <td>${mecanico.id}</td>
                    <td>${mecanico.nombre}</td>
                    <td>${mecanico.telefono}</td>
                    <td>${mecanico.especialidad}</td>

                    <td class="actions">

                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/mecanicos?accion=editar&id=${mecanico.id}">
                            Editar
                        </a>

                        <a class="btn btn-danger"
                           href="${pageContext.request.contextPath}/mecanicos?accion=eliminar&id=${mecanico.id}"
                           onclick="return confirm('¿Eliminar este mecánico?');">
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