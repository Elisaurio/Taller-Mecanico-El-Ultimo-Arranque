<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>El Último Arranque</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<header class="navbar">
    <div class="brand">
        <span class="brand-icon">🔧</span>
        <div>
            <h1>El Último Arranque</h1>
            <span>Gestión de Taller Mecánico</span>
        </div>
    </div>
</header>

<main class="container">

    <section class="hero">
        <div>
            <span class="eyebrow">SISTEMA DE GESTIÓN</span>
            <h2>Controla tu taller desde un solo lugar.</h2>
            <p>
                Administra clientes, vehículos, mecánicos,
                inventario, servicios y órdenes de trabajo.
            </p>
        </div>
    </section>

    <section class="module-grid">

        <a href="${pageContext.request.contextPath}/clientes" class="module-card">
            <span class="module-icon">👤</span>
            <h3>Clientes</h3>
            <p>Administrar clientes del taller.</p>
        </a>

        <a href="${pageContext.request.contextPath}/vehiculos" class="module-card">
            <span class="module-icon">🚗</span>
            <h3>Vehículos</h3>
            <p>Registrar vehículos e historial.</p>
        </a>

        <a href="${pageContext.request.contextPath}/mecanicos" class="module-card">
            <span class="module-icon">🧑‍🔧</span>
            <h3>Mecánicos</h3>
            <p>Gestionar personal técnico.</p>
        </a>

        <a href="${pageContext.request.contextPath}/repuestos" class="module-card">
            <span class="module-icon">⚙️</span>
            <h3>Repuestos</h3>
            <p>Controlar inventario y existencias.</p>
        </a>

        <a href="${pageContext.request.contextPath}/servicios" class="module-card">
            <span class="module-icon">🛠️</span>
            <h3>Servicios</h3>
            <p>Gestionar mano de obra.</p>
        </a>

        <a href="${pageContext.request.contextPath}/ordenes" class="module-card module-card-main">
            <span class="module-icon">📋</span>
            <h3>Órdenes de trabajo</h3>
            <p>Gestionar el flujo completo de reparación.</p>
        </a>

        <a href="${pageContext.request.contextPath}/reportes">
            Reportes PDF
        </a>

    </section>

</main>

<footer class="footer">
    El Último Arranque · Sistema de Gestión de Taller
</footer>

</body>
</html>