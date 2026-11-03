<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Vehículo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Vehículo</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">
        <h2>${empty vehiculo ? 'Nuevo vehículo' : 'Editar vehículo'}</h2>

        <a class="btn btn-secondary"
           href="${pageContext.request.contextPath}/vehiculos">
            Regresar
        </a>
    </div>

    <div class="card">

        <form method="post"
              action="${pageContext.request.contextPath}/vehiculos">

            <input type="hidden" name="id"
                   value="${empty vehiculo ? 0 : vehiculo.id}">

            <div class="form-grid">

                <div class="form-group">
                    <label>Placa</label>
                    <input type="text" name="placa"
                           value="${vehiculo.placa}" required>
                </div>

                <div class="form-group">
                    <label>Marca</label>
                    <input type="text" name="marca"
                           value="${vehiculo.marca}" required>
                </div>

                <div class="form-group">
                    <label>Modelo</label>
                    <input type="text" name="modelo"
                           value="${vehiculo.modelo}" required>
                </div>

                <div class="form-group">
                    <label>Año</label>
                    <input type="number" name="anio"
                           value="${vehiculo.anio}" min="1900" required>
                </div>

                <div class="form-group full">
                    <label>Propietario</label>

                    <select name="clienteId" required>

                        <option value="">Seleccione un cliente</option>

                        <c:forEach var="cliente" items="${clientes}">
                            <option value="${cliente.id}"
                                ${vehiculo.propietario != null &&
                                  vehiculo.propietario.id == cliente.id ? 'selected' : ''}>
                                ${cliente.nombre} - ${cliente.identificacion}
                            </option>
                        </c:forEach>

                    </select>
                </div>

                <div class="form-group full">

                    <div class="actions">

                        <button class="btn btn-primary" type="submit">
                            Guardar
                        </button>

                        <a class="btn btn-secondary"
                           href="${pageContext.request.contextPath}/vehiculos">
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