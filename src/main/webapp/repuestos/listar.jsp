<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventario | El Último Arranque</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div><h1>El Último Arranque</h1><span>Inventario</span></div>
    </div>
</header>
<main class="container">
    <div class="page-header">
        <h2>Repuestos</h2>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/repuestos?accion=nuevo">Nuevo repuesto</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger"><c:out value="${error}"/></div>
    </c:if>

    <div class="card table-container">
        <table class="table">
            <thead>
            <tr><th>Nombre</th><th>Descripción</th><th>Precio</th><th>Existencia</th><th>Acciones</th></tr>
            </thead>
            <tbody>
            <c:forEach var="repuesto" items="${repuestos}">
                <tr>
                    <td><c:out value="${repuesto.nombre}"/></td>
                    <td><c:out value="${repuesto.descripcion}"/></td>
                    <td>Q ${repuesto.precio}</td>
                    <td>
                        <c:choose>
                            <c:when test="${repuesto.existencia == 0}"><strong>Agotado</strong></c:when>
                            <c:when test="${repuesto.existencia <= 5}">${repuesto.existencia} (Stock bajo)</c:when>
                            <c:otherwise>${repuesto.existencia}</c:otherwise>
                        </c:choose>
                    </td>
                    <td class="actions">
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/repuestos?accion=editar&id=${repuesto.id}">Editar</a>
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/repuestos?accion=abastecer&id=${repuesto.id}">Abastecer</a>
                        <a class="btn btn-danger" href="${pageContext.request.contextPath}/repuestos?accion=eliminar&id=${repuesto.id}"
                           onclick="return confirm('¿Eliminar este repuesto?');">Eliminar</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty repuestos}">
                <tr><td colspan="5">No hay repuestos registrados.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
