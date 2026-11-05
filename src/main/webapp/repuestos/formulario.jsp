<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Repuesto | El Último Arranque</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div><h1>El Último Arranque</h1><span>Catálogo de repuestos</span></div>
    </div>
</header>
<main class="container">
    <div class="page-header">
        <h2>${empty repuesto ? 'Nuevo repuesto' : 'Editar repuesto'}</h2>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/repuestos">Regresar</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger"><c:out value="${error}"/></div>
    </c:if>

    <section class="card">
        <form method="post" action="${pageContext.request.contextPath}/repuestos">
            <input type="hidden" name="id" value="${empty repuesto ? 0 : repuesto.id}">
            <div class="form-grid">
                <div class="form-group">
                    <label for="nombre">Nombre</label>
                    <input id="nombre" type="text" name="nombre" value="${repuesto.nombre}" required>
                </div>
                <div class="form-group">
                    <label for="precio">Precio</label>
                    <input id="precio" type="number" name="precio" value="${repuesto.precio}" min="0" step="0.01" required>
                </div>
                <div class="form-group full">
                    <label for="descripcion">Descripción</label>
                    <textarea id="descripcion" name="descripcion"><c:out value="${repuesto.descripcion}"/></textarea>
                </div>

                <c:choose>
                    <c:when test="${empty repuesto}">
                        <div class="form-group">
                            <label for="existencia">Existencia inicial</label>
                            <input id="existencia" type="number" name="existencia" min="0" step="1" value="0" required>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="form-group">
                            <label>Existencia actual</label>
                            <p>${repuesto.existencia} unidades</p>
                            <a href="${pageContext.request.contextPath}/repuestos?accion=abastecer&id=${repuesto.id}">Registrar abastecimiento</a>
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="form-group full">
                    <div class="actions">
                        <button class="btn btn-primary" type="submit">Guardar</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/repuestos">Cancelar</a>
                    </div>
                </div>
            </div>
        </form>
    </section>
</main>
</body>
</html>
