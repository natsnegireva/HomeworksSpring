package shop.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "order")
    private List<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @OneToOne
    @JoinColumn (name = "employee_id")
    private Employee employee;

    public final Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", products=" + products +
                ", employee=" + employee +
                '}';
    }
}

======================================================================================
package shop.model;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "details_id")
    private EmployeeDetails details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public final EmployeeDetails getDetails() {
        return details;
    }

    public void setDetails(EmployeeDetails details) {
        this.details = details;
    }

    @OneToOne
    @JoinColumn (name = "order_id")
    private Order order;

    public final Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return String.format("Employee [id = %d, name = %s]", id, name);
    }

    @OneToOne(mappedBy = "employee", optional = false)
    private Order order2;

    public Order getOrder2() {
        return order2;
    }

    public void setOrder2(Order order2) {
        this.order2 = order2;
    }
}

=========================================================================
package shop.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import shop.model.Product;

import java.util.List;

public class ProductDao {
 public static SessionFactory factory;

    public List<Product> findAll() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            List<Product> products = session.createQuery("from Product").getResultList();
            session.getTransaction().commit();
            return products;
        }
    }

    public List findByPrice(int price) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            List products = session.createQuery("select p from Product p where price = price ").getResultList();
            session.getTransaction().commit();
            return products;
        }
    }

    public Product findById(Long id) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, "id");
            session.getTransaction().commit();
            return product;
        }
    }

    public Product updatePrice (Long id, int price) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, "id");
            product.setPrice(price);
            session.getTransaction().commit();
            return product;
        }
    }

    public void deleteProduct(Long id) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, "id");
            session.delete(product);
            session.getTransaction().commit();
        }
    }

    public void saveProduct(Product product) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.saveOrUpdate (product);
            session.getTransaction().commit();
        }
    }

    public static void main(String[] args) {
        try {
            factory.getCurrentSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
=====================================================================

package shop.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import shop.model.Employee;

import java.util.List;

public class EmployeeDao {
    public static SessionFactory factory;

    public List<Employee> findAll() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            List<Employee> employees = session.createQuery("from Employee").getResultList();
            session.getTransaction().commit();
            return employees;
        }
    }

    public List findByName(String name) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            List employees = session.createQuery("select p from Employee p where name = name").getResultList();
            session.getTransaction().commit();
            return employees;
        }
    }

    public Employee findById(Long id) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Employee employee = session.get(Employee.class, "id");
            session.getTransaction().commit();
            return employee;
        }
    }

    public Employee updateName (String name, Long id) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Employee employee = session.get(Employee.class, "id");
            employee.setName (name);
            session.getTransaction().commit();
            return employee;
        }
    }

    public void deleteEmployee(Long id) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Employee employee = session.get(Employee.class, "id");
            session.delete(employee);
            session.getTransaction().commit();
        }
    }

    public void saveEmployee(Employee employee) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.saveOrUpdate (employee);
            session.getTransaction().commit();
        }
    }

    public static void main(String[] args) {
        try {
            factory.getCurrentSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
=======================================================================
BEGIN;
DROP TABLE IF EXISTS employees_details CASCADE;
CREATE TABLE employees_details (id bigserial PRIMARY KEY, email VARCHAR(255), city varchar(255));
INSERT INTO employees_details (email, city) VALUES
('terminator@gmail.com', 'California'),
('rembo@gmail.com', 'Atlanta'),
('corben_dallas@gmail.com', 'New York');

DROP TABLE IF EXISTS employees CASCADE;
CREATE TABLE employees (id bigserial PRIMARY KEY, name VARCHAR(255), details_id bigint, FOREIGN KEY (details_id) REFERENCES employees_details (id));
INSERT INTO employees (name, details_id, order_id) VALUES
('Arnold S.', 1),
('Silvester S.', 2),
('Willis B.', 3);

DROP TABLE IF EXISTS orders CASCADE;
CREATE TABLE orders (id bigserial PRIMARY KEY, title varchar(255), employee_id bigint references employee(id));
INSERT INTO orders (title, employee_id) VALUES;

DROP TABLE IF EXISTS products CASCADE;
CREATE TABLE products (id bigserial PRIMARY KEY, title varchar(255), price int, order_id bigint references orders(id));
INSERT INTO products (title, price, order_id) VALUES
('milk', 79.90),
('bread', 24.90),
('butter', 220.00),
('cheese', 350.55),
('coca-cola', 69.90);

COMMIT;
