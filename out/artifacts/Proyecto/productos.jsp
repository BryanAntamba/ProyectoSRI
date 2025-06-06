<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, java.util.Map, model.Producto" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Productos - Mimir Petshop</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f9f9f9;
    }
    .top-bar {
      width: 90%;
      margin: 20px auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .btn {
      padding: 6px 12px;
      border: none;
      border-radius: 3px;
      cursor: pointer;
      font-size: 0.9rem;
    }
    .btn-comprar { background-color: #4CAF50; color: white; }
    .btn-editar { background-color: #2196F3; color: white; }
    .btn-eliminar { background-color: #f44336; color: white; }
    .btn-add { background-color: #555; color: white; margin-bottom: 15px; }
    table {
      width: 90%;
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
  </style>
</head>
<body>
<%
  // Recuperar lista de productos y rol de la sesión
  List<Producto> productos = (List<Producto>) request.getAttribute("productos");
  String rol = (String) session.getAttribute("rol");
  String usuario = (String) session.getAttribute("usuario");
  if (usuario == null) {
    // Si no hay sesión, redirigir a login (seguridad extra)
    response.sendRedirect("login");
    return;
  }
%>

<div class="top-bar">
  <div>
    Bienvenido, <strong><%= usuario %></strong> (<%= rol %>)
  </div>
  <div>
    <a href="cart"><button class="btn">Ver Carrito</button></a>
    <a href="logout"><button class="btn">Cerrar Sesión</button></a>
  </div>
</div>

<% if ("ADMIN".equals(rol)) { %>
<div style="width: 90%; margin: 0 auto;">
  <a href="addProduct"><button class="btn btn-add">+ Añadir Producto</button></a>
</div>
<% } %>

<table>
  <thead>
  <tr>
    <th>Nombre</th>
    <th>Marca</th>
    <th>Precio</th>
    <th>Stock</th>
    <th>Acciones</th>
  </tr>
  </thead>
  <tbody>
  <%
    if (productos != null) {
      for (Producto prod : productos) {
  %>
  <tr>
    <td><%= prod.getNombre() %></td>
    <td><%= prod.getMarca() %></td>
    <td>$ <%= prod.getPrecio() %></td>
    <td><%= prod.getStock() %></td>
    <td>
      <!-- Form para agregar al carrito -->
      <form action="addToCart" method="post" style="display: inline-block;">
        <input type="hidden" name="productoId" value="<%= prod.getId() %>" />
        <input type="number" name="cantidad" value="1" min="1" max="<%= prod.getStock() %>"
               style="width: 50px;" required />
        <button type="submit" class="btn btn-comprar">Comprar</button>
      </form>
      <% if ("ADMIN".equals(rol)) { %>
      <a href="editProduct?id=<%= prod.getId() %>">
        <button class="btn btn-editar">Editar</button>
      </a>
      <a href="deleteProduct?id=<%= prod.getId() %>"
         onclick="return confirm('¿Eliminar este producto?');">
        <button class="btn btn-eliminar">Eliminar</button>
      </a>
      <% } %>
    </td>
  </tr>
  <%
      }
    }
  %>
  </tbody>
</table>
</body>
</html>
