<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.math.BigDecimal, java.util.List, java.util.Map, model.Producto" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Carrito de Compras - Mimir Petshop</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
        }
        .top-bar {
            width: 80%;
            margin: 20px auto;
            display: flex;
            justify-content: space-between;
        }
        .btn {
            padding: 6px 12px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            font-size: 0.9rem;
        }
        .btn-checkout { background-color: #4CAF50; color: white; }
        .btn-cancel { background-color: #f44336; color: white; }
        table {
            width: 80%;
            margin: 0 auto 30px auto;
            border-collapse: collapse;
            background-color: white;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: center;
        }
        th { background-color: #f2f2f2; }
        .form-checkout {
            width: 80%;
            margin: 20px auto;
            padding: 15px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<%
    // Verificar sesión y usuario
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("login");
        return;
    }

    // Recuperar lista “items”: List<Map<String,Object>>
    List<Map<String, Object>> items =
            (List<Map<String, Object>>) request.getAttribute("items");
    BigDecimal totalGeneral = (BigDecimal) request.getAttribute("totalGeneral");

    // Mensajes posibles
    String mensajeError = (String) request.getAttribute("mensajeError");
    String mensajeExito = (String) request.getAttribute("mensajeExito");
%>

<div class="top-bar">
    <div>
        <a href="productos"><button class="btn">← Seguir Comprando</button></a>
    </div>
    <div>
        <a href="logout"><button class="btn">Cerrar Sesión</button></a>
    </div>
</div>

<% if (mensajeError != null) { %>
<div style="color: red; text-align: center;"><%= mensajeError %></div>
<% } %>
<% if (mensajeExito != null) { %>
<div style="color: green; text-align: center;"><%= mensajeExito %></div>
<% } %>

<table>
    <thead>
    <tr>
        <th>Nombre</th>
        <th>Precio Unitario</th>
        <th>Cantidad</th>
        <th>Subtotal</th>
    </tr>
    </thead>
    <tbody>
    <%
        if (items != null) {
            for (Map<String, Object> item : items) {
                Producto p = (Producto) item.get("producto");
                Integer cantidad = (Integer) item.get("cantidad");
                BigDecimal subtotal = (BigDecimal) item.get("subtotal");
    %>
    <tr>
        <td><%= p.getNombre() %></td>
        <td>$ <%= p.getPrecio() %></td>
        <td><%= cantidad %></td>
        <td>$ <%= subtotal %></td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
    <tfoot>
    <tr>
        <th colspan="3">Total General</th>
        <th>$ <%= (totalGeneral != null ? totalGeneral : BigDecimal.ZERO) %></th>
    </tr>
    </tfoot>
</table>

<div class="form-checkout">
    <h3>Confirmar Compra</h3>
    <form action="checkout" method="post">
        <label for="clienteNombre">Nombre del Cliente:</label><br/>
        <input type="text" name="clienteNombre" id="clienteNombre" required
               style="width: 100%; padding: 8px; margin: 5px 0 15px 0; border: 1px solid #ccc;" /><br/>

        <label for="clienteCedula">Cédula:</label><br/>
        <input type="text" name="clienteCedula" id="clienteCedula" required
               style="width: 100%; padding: 8px; margin: 5px 0 15px 0; border: 1px solid #ccc;" /><br/>

        <button type="submit" class="btn btn-checkout">Realizar Pago</button>
    </form>
</div>
</body>
</html>
