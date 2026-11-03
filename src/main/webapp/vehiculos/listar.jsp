<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Vehículos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Vehículos</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">
        <h2>Vehículos</h2>

        <a class="btn btn-primary"
           href="${pageContext.request.contextPath}/vehiculos?accion=nuevo">
            Nuevo vehículo
        </a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card table-container">

        <table class="table">
            <thead>
            <tr>
                <th>Placa</th>
                <th>Marca</th>
                <th>Modelo</th>
                <th>Año</th>
                <th>Propietario</th>
                <th>Acciones</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="vehiculo" items="${vehiculos}">
                <tr>
                    <td>${vehiculo.placa}</td>
                    <td>${vehiculo.marca}</td>
                    <td>${vehiculo.modelo}</td>
                    <td>${vehiculo.anio}</td>
                    <td>${vehiculo.propietario.nombre}</td>
                    <td class="actions">

                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/vehiculos?accion=editar&id=${vehiculo.id}">
                            Editar
                        </a>

                        <a class="btn btn-danger"
                           href="${pageContext.request.contextPath}/vehiculos?accion=eliminar&id=${vehiculo.id}"
                           onclick="return confirm('¿Eliminar este vehículo?');">
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