The HappySweets repository holds a pet-project that might further be developed 
into an e-commerce website for a small business, that was created with the following technologies:
1)Programming languages: Java, JavaScript + html, css and sql
2)My SQl database
3)Spring Framework(Spring Boot, Spring Data Jpa, Spring Security)
4)Thymeleaf template engine
5)JUnit testing.

Spring Security divides the website for the users with two roles: ADMIN and USER
What can be done with ROLE_ADMIN:
1)Manage categories: create/read/update/delete
2)Mange products: create/read/update/delete
Besides the regular CRUD operations admin will be informed, when products would have to high/small price/weight ratio

What can be done with ROLE_USER:
1)See the products(read)
2)Add them to cart
3)Proceed to check out
Another interesting feature is the home page gallery. 
It takes 4 of the most expensive products from the database and displays their pictures. 
Pictures also contain links that lead to product cards, that allow to add product to a cart.

For a database schema check out images folder


