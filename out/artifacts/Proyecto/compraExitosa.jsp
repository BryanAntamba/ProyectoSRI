<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Compra Exitosa - Mimir Petshop</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f5f5f5;
    }
    .message {
      width: 400px;
      margin: 100px auto;
      text-align: center;
      padding: 20px;
      background-color: #e8f5e9;
      border: 1px solid #4CAF50;
      border-radius: 4px;
    }
    .btn {
      background-color: #4CAF50;
      color: white;
      padding: 10px 20px;
      margin: 10px 5px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    .btn:hover {
      background-color: #45a049;
    }
  </style>
</head>
<body>
<%
  // Levantamos los atributos enviados por CheckoutServlet:
  String mensajeExito = (String) request.getAttribute("mensajeExito");
  Integer invoiceId = (Integer) request.getAttribute("invoiceId");
  if (mensajeExito == null || invoiceId == null) {
    // Si no hay mensaje o invoiceId, redirigimos a productos
    response.sendRedirect("productos");
    return;
  }
%>
<div class="message">
  <h2>¡Compra Realizada Con Éxito!</h2>
  <p><%= mensajeExito %></p>
  <!-- Botón para volver a Productos -->
  <a href="productos"><button class="btn">Volver a Productos</button></a>
  <!-- Botón para descargar la factura en PDF -->
  <a href="downloadInvoice?invoiceId=<%= invoiceId %>">
    <button class="btn">Descargar Factura</button>
  </a>
</div>
</body>
</html>
