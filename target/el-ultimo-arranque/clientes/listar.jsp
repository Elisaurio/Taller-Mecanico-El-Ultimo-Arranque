<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clientes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Clientes</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">
        <h2>Clientes</h2>
        <a class="btn btn-primary"
           href="${pageContext.request.contextPath}/clientes?accion=nuevo">
            Nuevo cliente
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
                <th>Identificación</th>
                <th>Teléfono</th>
                <th>Dirección</th>
                <th>Acciones</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="cliente" items="${clientes}">
                <tr>
                    <td>${cliente.id}</td>
                    <td>${cliente.nombre}</td>
                    <td>${cliente.identificacion}</td>
                    <td>${cliente.telefono}</td>
                    <td>${cliente.direccion}</td>
                    <td class="actions">
                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/clientes?accion=editar&id=${cliente.id}">
                            Editar
                        </a>

                        <a class="btn btn-danger"
                           href="${pageContext.request.contextPath}/clientes?accion=eliminar&id=${cliente.id}"
                           onclick="return confirm('¿Eliminar este cliente?');">
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