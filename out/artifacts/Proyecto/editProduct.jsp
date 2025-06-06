<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Producto" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Editar Producto - Mimir Petshop</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f5f5f5;
    }
    .form-container {
      width: 400px;
      margin: 50px auto;
      padding: 20px;
      background-color: #fff;
      border: 1px solid #ddd;
      border-radius: 4px;
    }
    input[type=text], input[type=number] {
      width: 100%;
      padding: 8px;
      margin: 5px 0 15px 0;
      border: 1px solid #ccc;
      box-sizing: border-box;
    }
    button {
      background-color: #2196F3;
      color: white;
      padding: 10px 0;
      width: 100%;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    button:hover { background-color: #1976D2; }
    .top-link {
      margin-bottom: 20px;
      display: block;
      text-align: center;
    }
  </style>
</head>
<body>
<%
  // Verificar rol ADMIN
  String rol = (String) session.getAttribute("rol");
  if (rol == null || !"ADMIN".equals(rol)) {
    response.sendRedirect("productos");
    return;
  }

  // Recuperar el objeto Producto que envió el servlet en request
  Producto producto = (Producto) request.getAttribute("producto");
  if (producto == null) {
    // Si no llegó nada, redirigimos de nuevo a lista
    response.sendRedirect("productos");
    return;
  }
%>

<div class="form-container">
  <a href="productos" class="top-link">← Regresar a Productos</a>
  <h2>Editar Producto</h2>
  <form action="editProduct" method="post">
    <!-- ID oculto -->
    <input type="hidden" name="id" value="<%= producto.getId() %>" />

    <label for="nombre">Nombre:</label>
    <input type="text" name="nombre" id="nombre" value="<%= producto.getNombre() %>" required />

    <label for="marca">Marca:</label>
    <input type="text" name="marca" id="marca" value="<%= producto.getMarca() %>" required />

    <label for="precio">Precio:</label>
    <input type="text" name="precio" id="precio" value="<%= producto.getPrecio() %>" required />

    <label for="stock">Stock:</label>
    <input type="number" name="stock" id="stock" min="0" value="<%= producto.getStock() %>" required />

    <button type="submit">Actualizar Producto</button>
  </form>
</div>
</body>
</html>
