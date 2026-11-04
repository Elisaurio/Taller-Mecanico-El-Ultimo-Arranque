<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <title>Reportes | El Último Arranque</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>

<body>

<main class="contenedor">

    <h1>Reportes</h1>

    <section class="card">

        <h2>Órdenes por estado</h2>

        <form method="post"
              action="${pageContext.request.contextPath}/reportes"
              target="_blank">

            <input type="hidden"
                   name="tipo"
                   value="estado">

            <label>Estado</label>

            <select name="estado" required>
                <option value="PENDIENTE">Pendiente</option>
                <option value="EN_PROCESO">En proceso</option>
                <option value="FINALIZADA">Finalizada</option>
                <option value="CANCELADA">Cancelada</option>
            </select>

            <button type="submit">
                Generar PDF
            </button>

        </form>

    </section>

    <section class="card">

        <h2>Historial de reparaciones por vehículo</h2>

        <form method="post"
              action="${pageContext.request.contextPath}/reportes"
              target="_blank">

            <input type="hidden"
                   name="tipo"
                   value="vehiculo">

            <label>Placa</label>

            <input type="text"
                   name="placa"
                   required>

            <button type="submit">
                Generar PDF
            </button>

        </form>

    </section>

    <section class="card">

        <h2>Stock bajo</h2>

        <form method="post"
              action="${pageContext.request.contextPath}/reportes"
              target="_blank">

            <input type="hidden"
                   name="tipo"
                   value="stock">

            <button type="submit">
                Generar PDF
            </button>

        </form>

    </section>

    <section class="card">

        <h2>Órdenes por mecánico y estado</h2>

        <form method="post"
              action="${pageContext.request.contextPath}/reportes"
              target="_blank">

            <input type="hidden"
                   name="tipo"
                   value="mecanico">

            <label>ID del mecánico</label>

            <input type="number"
                   name="mecanicoId"
                   min="1"
                   required>

            <label>Estado</label>

            <select name="estado" required>
                <option value="PENDIENTE">Pendiente</option>
                <option value="EN_PROCESO">En proceso</option>
                <option value="FINALIZADA">Finalizada</option>
                <option value="CANCELADA">Cancelada</option>
            </select>

            <button type="submit">
                Generar PDF
            </button>

        </form>

    </section>

    <section class="card">

        <h2>Ingresos por rango de fechas</h2>

        <form method="post"
              action="${pageContext.request.contextPath}/reportes"
              target="_blank">

            <input type="hidden"
                   name="tipo"
                   value="ingresos">

            <label>Fecha inicial</label>

            <input type="date"
                   name="fechaInicio"
                   required>

            <label>Fecha final</label>

            <input type="date"
                   name="fechaFin"
                   required>

            <button type="submit">
                Generar PDF
            </button>

        </form>

    </section>

</main>

</body>
</html>