<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Abastecer repuesto | El Último Arranque</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div><h1>El Último Arranque</h1><span>Abastecimiento de inventario</span></div>
    </div>
</header>
<main class="container">
    <div class="page-header">
        <h2>Abastecer repuesto</h2>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/repuestos">Regresar</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger"><c:out value="${error}"/></div>
    </c:if>

    <section class="card">
        <p><strong>Repuesto:</strong> <c:out value="${repuesto.nombre}"/></p>
        <p><strong>Existencia actual:</strong> ${repuesto.existencia}</p>
        <form method="post" action="${pageContext.request.contextPath}/repuestos">
            <input type="hidden" name="accion" value="abastecer">
            <input type="hidden" name="id" value="${repuesto.id}">
            <div class="form-group">
                <label for="cantidad">Unidades recibidas</label>
                <input id="cantidad" type="number" name="cantidad" min="1" step="1" required>
            </div>
            <div class="actions">
                <button class="btn btn-primary" type="submit">Registrar abastecimiento</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/repuestos">Cancelar</a>
            </div>
        </form>
    </section>
</main>
</body>
</html>
