<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orden ${orden.id} | El Último Arranque</title>
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
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/ordenes">Regresar</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger"><c:out value="${error}"/></div>
    </c:if>

    <section class="card">
        <h3>Información de la orden</h3>
        <p><strong>Estado:</strong> ${orden.estado}</p>
        <p><strong>Fecha de creación:</strong> ${orden.fechaCreacion}</p>
        <p><strong>Fecha de finalización:</strong> ${orden.fechaFinalizacion}</p>
        <p><strong>Problema reportado:</strong> <c:out value="${orden.descripcionProblema}"/></p>
        <p><strong>Cliente:</strong> <c:out value="${orden.vehiculo.propietario.nombre}"/></p>
        <p><strong>Identificación:</strong> <c:out value="${orden.vehiculo.propietario.identificacion}"/></p>
        <p><strong>Vehículo:</strong> <c:out value="${orden.vehiculo.marca}"/> <c:out value="${orden.vehiculo.modelo}"/> (${orden.vehiculo.anio})</p>
        <p><strong>Placa:</strong> <c:out value="${orden.vehiculo.placa}"/></p>
        <p><strong>Mecánico:</strong> <c:out value="${orden.mecanico.nombre}"/></p>
        <p><strong>Especialidad:</strong> <c:out value="${orden.mecanico.especialidad}"/></p>

        <div class="actions">
            <c:if test="${orden.estado.toString() == 'PENDIENTE'}">
                <form method="post" action="${pageContext.request.contextPath}/ordenes">
                    <input type="hidden" name="accion" value="iniciar">
                    <input type="hidden" name="ordenId" value="${orden.id}">
                    <button class="btn btn-primary" type="submit">Iniciar reparación</button>
                </form>
            </c:if>

            <c:if test="${orden.estado.toString() == 'PENDIENTE' || orden.estado.toString() == 'EN_PROCESO'}">
                <form method="post" action="${pageContext.request.contextPath}/ordenes"
                      onsubmit="return confirm('¿Deseas cancelar esta orden? Se devolverán los repuestos al inventario.');">
                    <input type="hidden" name="accion" value="cancelar">
                    <input type="hidden" name="ordenId" value="${orden.id}">
                    <button class="btn btn-danger" type="submit">Cancelar orden</button>
                </form>
            </c:if>

            <c:if test="${orden.estado.toString() == 'FINALIZADA'}">
                <a class="btn btn-success"
                   href="${pageContext.request.contextPath}/ordenes?accion=comprobante&id=${orden.id}"
                   target="_blank">Ver comprobante PDF</a>
            </c:if>
        </div>
    </section>

    <section class="card table-container">
        <h3>Servicios realizados</h3>
        <table class="table">
            <thead>
            <tr><th>Servicio</th><th>Mano de obra</th></tr>
            </thead>
            <tbody>
            <c:forEach var="servicio" items="${orden.servicios}">
                <tr>
                    <td><c:out value="${servicio.descripcion}"/></td>
                    <td>Q ${servicio.costoManoObra}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty orden.servicios}">
                <tr><td colspan="2">Todavía no hay servicios registrados.</td></tr>
            </c:if>
            </tbody>
        </table>
        <p><strong>Total mano de obra:</strong> Q ${orden.totalServicios}</p>
    </section>

    <section class="card table-container">
        <h3>Repuestos asociados a la orden</h3>
        <c:if test="${orden.estado.toString() == 'CANCELADA'}">
            <p>Esta orden fue cancelada. Los repuestos asociados fueron devueltos al inventario.</p>
        </c:if>
        <table class="table">
            <thead>
            <tr><th>Repuesto</th><th>Cantidad</th><th>Precio unitario</th><th>Subtotal</th>
                <c:if test="${orden.estado.toString() == 'EN_PROCESO'}"><th>Acción</th></c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="detalle" items="${orden.repuestos}">
                <tr>
                    <td><c:out value="${detalle.repuesto.nombre}"/></td>
                    <td>${detalle.cantidad}</td>
                    <td>Q ${detalle.precioUnitario}</td>
                    <td>Q ${detalle.subtotal}</td>
                    <c:if test="${orden.estado.toString() == 'EN_PROCESO'}">
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/ordenes"
                                  onsubmit="return confirm('¿Retirar este repuesto y devolverlo al inventario?');">
                                <input type="hidden" name="accion" value="eliminarRepuesto">
                                <input type="hidden" name="ordenId" value="${orden.id}">
                                <input type="hidden" name="repuestoId" value="${detalle.repuesto.id}">
                                <button class="btn btn-danger" type="submit">Retirar</button>
                            </form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            <c:if test="${empty orden.repuestos}">
                <tr><td colspan="5">Todavía no hay repuestos asociados.</td></tr>
            </c:if>
            </tbody>
        </table>
        <p><strong>Total repuestos:</strong> Q ${orden.totalRepuestos}</p>
    </section>

    <c:if test="${orden.estado.toString() != 'CANCELADA'}">
        <section class="card">
            <h3>Resumen de costos</h3>
            <p><strong>Mano de obra:</strong> Q ${orden.totalServicios}</p>
            <p><strong>Repuestos:</strong> Q ${orden.totalRepuestos}</p>
            <h2>Total: Q ${orden.total}</h2>
        </section>
    </c:if>

    <c:if test="${orden.estado.toString() == 'EN_PROCESO'}">
        <section class="card">
            <h3>Agregar servicio</h3>
            <form method="post" action="${pageContext.request.contextPath}/ordenes">
                <input type="hidden" name="accion" value="agregarServicio">
                <input type="hidden" name="ordenId" value="${orden.id}">
                <div class="form-grid">
                    <div class="form-group">
                        <label for="servicioId">Servicio</label>
                        <select id="servicioId" name="servicioId" required>
                            <option value="">Seleccione un servicio</option>
                            <c:forEach var="servicio" items="${servicios}">
                                <option value="${servicio.id}">
                                    <c:out value="${servicio.descripcion}"/> - Q ${servicio.costoManoObra}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>&nbsp;</label>
                        <button class="btn btn-primary" type="submit">Agregar servicio</button>
                    </div>
                </div>
            </form>
        </section>

        <section class="card">
            <h3>Asignar repuesto</h3>
            <form method="post" action="${pageContext.request.contextPath}/ordenes">
                <input type="hidden" name="accion" value="agregarRepuesto">
                <input type="hidden" name="ordenId" value="${orden.id}">
                <div class="form-grid">
                    <div class="form-group">
                        <label for="repuestoId">Repuesto</label>
                        <select id="repuestoId" name="repuestoId" required>
                            <option value="">Seleccione un repuesto</option>
                            <c:forEach var="repuesto" items="${repuestos}">
                                <option value="${repuesto.id}" ${repuesto.existencia == 0 ? 'disabled' : ''}>
                                    <c:out value="${repuesto.nombre}"/> - Existencia: ${repuesto.existencia} - Q ${repuesto.precio}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="cantidad">Cantidad</label>
                        <input id="cantidad" type="number" name="cantidad" min="1" required>
                    </div>
                    <div class="form-group">
                        <label>&nbsp;</label>
                        <button class="btn btn-primary" type="submit">Asignar repuesto</button>
                    </div>
                </div>
            </form>
        </section>

        <section class="card">
            <form method="post" action="${pageContext.request.contextPath}/ordenes"
                  onsubmit="return confirm('¿Finalizar la orden? Ya no podrá modificarse.');">
                <input type="hidden" name="accion" value="finalizar">
                <input type="hidden" name="ordenId" value="${orden.id}">
                <button class="btn btn-success" type="submit">Finalizar reparación y calcular total</button>
            </form>
        </section>
    </c:if>
</main>
</body>
</html>
