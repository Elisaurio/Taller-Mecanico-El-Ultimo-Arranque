<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Nueva orden</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Nueva orden</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">

        <h2>Nueva orden de trabajo</h2>

        <a class="btn btn-secondary"
           href="${pageContext.request.contextPath}/ordenes">
            Regresar
        </a>

    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card">

        <form method="post"
              action="${pageContext.request.contextPath}/ordenes">

            <input type="hidden" name="accion" value="crear">

            <div class="form-grid">

                <div class="form-group">
                    <label>Vehículo</label>

                    <select name="vehiculoId" required>

                        <option value="">Seleccione un vehículo</option>

                        <c:forEach var="vehiculo" items="${vehiculos}">

                            <option value="${vehiculo.id}">
                                ${vehiculo.placa} -
                                ${vehiculo.marca}
                                ${vehiculo.modelo}
                            </option>

                        </c:forEach>

                    </select>

                </div>

                <div class="form-group">

                    <label>Mecánico</label>

                    <select name="mecanicoId" required>

                        <option value="">Seleccione un mecánico</option>

                        <c:forEach var="mecanico" items="${mecanicos}">

                            <option value="${mecanico.id}">
                                ${mecanico.nombre}
                            </option>

                        </c:forEach>

                    </select>

                </div>

                <div class="form-group full">

                    <label>Descripción del problema</label>

                    <textarea name="descripcionProblema"
                              required></textarea>

                </div>

                <div class="form-group full">

                    <div class="actions">

                        <button class="btn btn-primary"
                                type="submit">
                            Crear orden
                        </button>

                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/ordenes">
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