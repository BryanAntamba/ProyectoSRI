<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Añadir Producto - Mimir Petshop</title>
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
      background-color: #4CAF50;
      color: white;
      padding: 10px 0;
      width: 100%;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    button:hover { background-color: #45a049; }
    .top-link {
      margin-bottom: 20px;
      display: block;
      text-align: center;
    }
  </style>
</head>
<body>
<%
  // Verificar que la sesión exista y sea ADMIN
  String rol = (String) session.getAttribute("rol");
  if (rol == null || !"ADMIN".equals(rol)) {
    response.sendRedirect("productos");
    return;
  }
%>

<div class="form-container">
  <a href="productos" class="top-link">← Regresar a Productos</a>
  <h2>Añadir Nuevo Producto</h2>
  <form action="addProduct" method="post">
    <label for="nombre">Nombre:</label>
    <input type="text" name="nombre" id="nombre" required />

    <label for="marca">Marca:</label>
    <input type="text" name="marca" id="marca" required />

    <label for="precio">Precio:</label>
    <input type="text" name="precio" id="precio" required />

    <label for="stock">Stock:</label>
    <input type="number" name="stock" id="stock" min="0" required />

    <button type="submit">Guardar Producto</button>
  </form>
</div>
</body>
</html>
