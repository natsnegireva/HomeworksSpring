//-- в процессе активации Идеи не успела сдать проектом
//-- в pom добавляем зависимости
//-- <dependency>
//--             <groupId>org.hibernate</groupId>
//--            <artifactId>hibernate-core</artifactId>
//--            <version>5.4.4.Final</version>
//--         </dependency>
//--        <dependency>
//--             <groupId>org.postgresql</groupId>
//--             <artifactId>postgresql</artifactId>
//--             <version>42.2.5</version>
//--         </dependency>
//-- </dependency>		

// -- в PrepareDataApp прописываем путь к файлу full.sql с базовой структурой таблиц Product и путь к конфигурации hibernate в файле configs/hibernate.cfg.xml
// -- В hibernate.cfg.xml: тип базы, путь к ней, пароль-логин и диалект базы
// -- в ресурсной папке конфигураций crud линкуем файл Product <mapping class="com.flamexander.hibernate.crud.Product"/>

// -- в файле full.sql создаем запись:

//-- DROP TABLE IF EXISTS product CASCADE;
//-- CREATE TABLE product (id bigserial PRIMARY KEY, title VARCHAR(255), price int);
//-- INSERT INTO product (title, price) VALUES
//-- ('product1', 10),
//-- ('product2', 20),
//-- ('product3', 100),
//-- ('product4', 50),
//-- ('product5', 500);


// -- создаем сущность Product в папке проекта crud
=====================================================================
@Entity 
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private int price;


    public Product() {
    }

    public Product(String title, BigDecimal price) {
        this.title = title;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }

}
==========================================================
// -- создаем файл ProductDao в папке crud 

public class ProductDao {
private static SessionFactory factory;

    public static void init() {
        PrepareDataApp.forcePrepareData();
        factory = new Configuration()
                .configure("configs/crud/hibernate.cfg.xml")
                .buildSessionFactory();
    }

    public static void findAll() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            List<Product> product = session.createQuery("from Product").getResultList();
            System.out.println(product + "\n");
            session.getTransaction().commit();
        }
    }
	
	    public static void findOnlyOn() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Product product = session.createQuery("select s from Product s where s.id = 3", Product.class).getSingleResult();
            System.out.println(product + "\n");
            session.getTransaction().commit();
        }
    }
	
	public static void findByPrice() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            List<Product> product = session.createQuery("select s from Product s where s.price < 80").getResultList();
            System.out.println(product2 + "\n");
            session.getTransaction().commit();
        }
    }

    public static void createProduct() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Product product6 = new Product(6L, "product6", 100);
            session.save(product6);
            session.getTransaction().commit();
            System.out.println(product6);
        }
    }

	// другой способ
    public static void readAndPrintProduct() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, 6L);
            System.out.println(product);
            session.getTransaction().commit();
        }
    }

    public static void saveOrUpdate() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, 6L);
            product.setPrice(60);
            session.getTransaction().commit();
        }
    }

    public static void deleteProduct() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, 6L);
            session.delete(product );
            session.getTransaction().commit();
        }
    }
	
	    public static void shutdown() {
        factory.close();
    }


    public static void main(String[] args) {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }
}
