<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Orden ${orden.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Orden de trabajo #${orden.id}</span>
        </div>
    </div>
</header>

<main class="container">

    <div class="page-header">

        <h2>Orden #${orden.id}</h2>

        <a class="btn btn-secondary"
           href="${pageContext.request.contextPath}/ordenes">
            Regresar
        </a>

    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card">

        <h3>Información de la orden</h3>

        <p><strong>Problema:</strong> ${orden.descripcionProblema}</p>

        <p>
            <strong>Estado:</strong>

            <span class="status status-${orden.estado.toString().toLowerCase().replace('_', '-')}">
                ${orden.estado}
            </span>
        </p>

        <p>
            <strong>Total:</strong>
            Q ${orden.total}
        </p>

        <div class="actions">

            <c:if test="${orden.estado.toString() == 'PENDIENTE'}">

                <form method="post"
                      action="${pageContext.request.contextPath}/ordenes">

                    <input type="hidden"
                           name="accion"
                           value="iniciar">

                    <input type="hidden"
                           name="id"
                           value="${orden.id}">

                    <button class="btn btn-primary"
                            type="submit">
                        Iniciar orden
                    </button>

                </form>

            </c:if>

            <c:if test="${orden.estado.toString() == 'PENDIENTE' ||
                         orden.estado.toString() == 'EN_PROCESO'}">

                <form method="post"
                      action="${pageContext.request.contextPath}/ordenes">

                    <input type="hidden"
                           name="accion"
                           value="cancelar">

                    <input type="hidden"
                           name="id"
                           value="${orden.id}">

                    <button class="btn btn-danger"
                            type="submit"
                            onclick="return confirm('¿Cancelar esta orden?');">
                        Cancelar orden
                    </button>

                </form>

            </c:if>

            <c:if test="${orden.estado.toString() == 'EN_PROCESO'}">

                <form method="post"
                      action="${pageContext.request.contextPath}/ordenes">

                    <input type="hidden"
                           name="accion"
                           value="finalizar">

                    <input type="hidden"
                           name="id"
                           value="${orden.id}">

                    <button class="btn btn-success"
                            type="submit">
                        Finalizar orden
                    </button>

                </form>

            </c:if>

        </div>

    </div>

    <br>

    <c:if test="${orden.estado.toString() == 'PENDIENTE' ||
                 orden.estado.toString() == 'EN_PROCESO'}">

        <div class="card">

            <h3>Agregar servicio</h3>

            <form method="post"
                  action="${pageContext.request.contextPath}/ordenes">

                <input type="hidden"
                       name="accion"
                       value="agregarServicio">

                <input type="hidden"
                       name="ordenId"
                       value="${orden.id}">

                <div class="form-grid">

                    <div class="form-group">

                        <label>Servicio</label>

                        <select name="servicioId" required>

                            <option value="">
                                Seleccione un servicio
                            </option>

                            <c:forEach var="servicio" items="${servicios}">

                                <option value="${servicio.id}">
                                    ${servicio.descripcion}
                                    - Q ${servicio.costoManoObra}
                                </option>

                            </c:forEach>

                        </select>

                    </div>

                    <div class="form-group">

                        <label>&nbsp;</label>

                        <button class="btn btn-primary"
                                type="submit">
                            Agregar servicio
                        </button>

                    </div>

                </div>

            </form>

        </div>

        <br>

        <div class="card">

            <h3>Agregar repuesto</h3>

            <form method="post"
                  action="${pageContext.request.contextPath}/ordenes">

                <input type="hidden"
                       name="accion"
                       value="agregarRepuesto">

                <input type="hidden"
                       name="ordenId"
                       value="${orden.id}">

                <div class="form-grid">

                    <div class="form-group">

                        <label>Repuesto</label>

                        <select name="repuestoId" required>

                            <option value="">
                                Seleccione un repuesto
                            </option>

                            <c:forEach var="repuesto" items="${repuestos}">

                                <option value="${repuesto.id}">
                                    ${repuesto.nombre}
                                    - Stock: ${repuesto.existencia}
                                    - Q ${repuesto.precio}
                                </option>

                            </c:forEach>

                        </select>

                    </div>

                    <div class="form-group">

                        <label>Cantidad</label>

                        <input type="number"
                               name="cantidad"
                               min="1"
                               required>

                    </div>

                    <div class="form-group">

                        <label>&nbsp;</label>

                        <button class="btn btn-primary"
                                type="submit">
                            Agregar repuesto
                        </button>

                    </div>

                </div>

            </form>

        </div>

    </c:if>

</main>

</body>
</html>