<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Órdenes de trabajo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Órdenes de trabajo</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">

        <h2>Órdenes de trabajo</h2>

        <a class="btn btn-primary"
           href="${pageContext.request.contextPath}/ordenes?accion=nuevo">
            Nueva orden
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
                <th>Problema</th>
                <th>Estado</th>
                <th>Total</th>
                <th>Acciones</th>
            </tr>
            </thead>

            <tbody>

            <c:forEach var="orden" items="${ordenes}">

                <tr>

                    <td>${orden.id}</td>

                    <td>${orden.descripcionProblema}</td>

                    <td>

                        <span class="status status-${orden.estado.toString().toLowerCase().replace('_', '-')}">
                            ${orden.estado}
                        </span>

                    </td>

                    <td>Q ${orden.total}</td>

                    <td>

                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/ordenes?accion=detalle&id=${orden.id}">
                            Ver
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