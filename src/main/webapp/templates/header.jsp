<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Header Template</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />
  </head>
  <body>
    <nav>
      <div class="navbar">
        <div class="logo">Ecommerce-website</div>
        
        <form action="/product/search" method="get">
          <div class="search-bar">
            <input name="prefix" type="text" placeholder="Search products...">
            <button type="submit">Search</button>
          </div>
        </form>

        <div class="category">
	        <% List<String> categoryList = (List<String>)request.getAttribute("categoryList"); %>
	        <% if (categoryList != null) { %>
	        	<label for="category">Category:</label>
		          <select name="category" id="category" onchange="window.location.href='/product/category/' + this.value">
		          	<option value="None"></option>
		            <% for (String category : categoryList) { %>
		                <option value="<%= category %>"><%= category %></option>
		            <% } %>
		          </select>
	        <% } %>
        </div>

        <div class="sections">
          <button id="aboutusBtn">About us</button>
          <button class="cart" onclick="window.location.href='/cartitem/cart'">
            Your cart <img src="${pageContext.request.contextPath}/images/cart.svg" alt="" />
          </button>
          <button class="profile" id="profile-btn">
            Profile <img src="${pageContext.request.contextPath}/images/profile.svg" />
          </button>
        </div>
      </div>
    </nav>

    <div class="profile-form" id="profile-form">
      <!-- Add the back button at the top-left corner -->
      <button id="back-btn" class="back-button">&larr; Back</button>

      <h2>Hey name! See your Profile</h2>
      <div class="avatar">
        <img src="../images/avatar.jpg" alt="" />
      </div>
      <div><b>Name :</b> Undefined</div>
      <div><b>Email-id :</b> Undefined</div>
      <div><b>Address :</b> Undefined</div>
      <div><b>Phone number :</b> Undefined</div>
      <button id="update-btn" onclick="window.location.href='/updateinfo'">
        Update
      </button>
      <button id="log-out-btn">Log out</button>
    </div>
    <script src="${pageContext.request.contextPath}/js/home.js"></script>
    <script src="${pageContext.request.contextPath}/js/about.js"></script>
  </body>
</html>
