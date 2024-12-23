package com.springspartans.shopkart.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springspartans.shopkart.exception.InvalidImageUploadException;
import com.springspartans.shopkart.model.Product;
import com.springspartans.shopkart.repository.ProductRepository;
import com.springspartans.shopkart.util.ImageUploadValidator;

import jakarta.annotation.PostConstruct;

@Service
public class ProductService {
	
	@Autowired
	private String uploadPath;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
    private ImageUploadValidator imageUploadValidator;
	
	private List<String> categoryList;
	
	@PostConstruct 
	public void addDummyProducts() {
		if (productRepository.count() == 0) {
            List<Product> sampleData = Arrays.asList(
            		new Product(1, "Ultrabook Laptop", "Electronics", "Dell", 29999.00, "laptop.jpg", 100, 0.00),
                    new Product(2, "T Shirt", "Clothing", "Nike", 1999.00, "tshirt.jpg", 200, 0.00),
                    new Product(3, "Soccer Ball", "Sports", "Adidas", 999.00, "soccer.jpg", 150, 0.00),
                    new Product(4, "Stratocaster Guitar", "Musical Instruments", "Fender", 74999.00, "guitar.jpg", 50, 0.00),
                    new Product(5, "Piano", "Musical Instruments", "Yamaha", 149999.00, "piano.jpg", 30, 0.00),
                    new Product(6, "Perfume", "Beauty", "Wild Stone", 2999.00, "perfume.jpg", 120, 0.00),
                    new Product(7, "4K Smart TV", "Electronics", "Samsung", 59999.00, "tv.jpg", 70, 0.00),
                    new Product(8, "501 Jeans", "Clothing", "Levi's", 3999.00, "jeans.jpg", 200, 0.00),
                    new Product(9, "Fleece", "Clothing", "Unbranded", 2999.00, "fleece.jpg", 150, 0.00),
                    new Product(10, "Drum Set", "Musical Instruments", "Pearl", 59999.00, "drum.jpg", 40, 0.00),
                    new Product(11, "Body Lotion", "Beauty", "Nivea", 699.00, "bodylotion.jpg", 300, 0.00),
                    new Product(12, "Microphone", "Musical Instruments", "Shure", 2999.00, "microphone.jpg", 100, 0.00),
                    new Product(13, "iPhone 13", "Electronics", "Apple", 74999.00, "iphone.jpg", 60, 0.00),
                    new Product(14, "Basketball", "Sports", "Wilson", 1499.00, "basketball.jpg", 150, 0.00),
                    new Product(15, "Tennis Racket", "Sports", "Wilson", 4999.00, "tennis.jpg", 100, 0.00),
                    new Product(16, "Face Cream", "Beauty", "Olay", 999.00, "facecream.jpg", 200, 0.00),
                    new Product(17, "Golf Clubs", "Sports", "Callaway", 9999.00, "golf.jpg", 50, 0.00),
                    new Product(18, "PlayStation 5", "Electronics", "Sony", 39999.00, "playstation.jpg", 30, 0.00),
                    new Product(19, "Superstar Jacket", "Clothing", "Adidas", 4999.00, "jacket.jpg", 100, 0.00),
                    new Product(20, "Mascara", "Beauty", "Maybelline", 499.00, "mascara.jpg", 300, 0.00)
                );
            productRepository.saveAll(sampleData);
        }
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProductById(int id) {
		return productRepository.findById(id).orElse(null);
	}

	public List<String> getAllCategories() {
		if (categoryList == null) {
			categoryList = new ArrayList<>();
			categoryList.add("All");
			categoryList.addAll(productRepository.findAllCategories());
		}
		return categoryList;
	}

	public List<Product> getProductsByCategory(String category) {
		if (category.equals("All"))
			return getAllProducts();
		return productRepository.findByCategory(category);
	}

	public List<Product> getProductsByStartName(String prefix) {
		return productRepository.findByStartName(prefix);
	}
	
	public void addProduct(int id, String name, String category, String brand, double price, MultipartFile image, int stock, double discount) 
			throws IOException, InvalidImageUploadException {
		String imageName = null;
		if (image != null && !image.isEmpty()) {
			if (! imageUploadValidator.isValidImage(image)) { 
	            throw new InvalidImageUploadException("Improper file format!");
	        }
	        imageName = "product" + id + ".jpg";
	    }
		Product product = new Product(id, name, category, brand, price, imageName, stock, discount);
		productRepository.save(product);
		if (imageName != null)
			saveImageToDirectory(image, imageName, "product");
	}
	
	public void updateProduct(int id, String name, String category, String brand, double price, MultipartFile image, int stock, double discount) 
			throws IOException, InvalidImageUploadException {
		Product existingProduct = productRepository.findById(id)
		        .orElseThrow(() -> new RuntimeException("Product not found"));
		existingProduct.setName(name);
	    existingProduct.setCategory(category);
	    existingProduct.setBrand(brand);
	    existingProduct.setPrice(price);
	    existingProduct.setStock(stock);
	    existingProduct.setDiscount(discount); 
		if (image != null && !image.isEmpty()) {
			if (!imageUploadValidator.isValidImage(image)) { 
	            throw new InvalidImageUploadException("Improper file format!");
	        }
	        String imageName = "product" + id + ".jpg";
	        existingProduct.setImage(imageName);
	        saveImageToDirectory(image, imageName, "product");
		}
		productRepository.save(existingProduct);
	}
	
	private void saveImageToDirectory(MultipartFile image, String imageName, String folderName) throws IOException {
	    String imageUploadPath = uploadPath + "\\image" ;
	    File destination = new File(imageUploadPath);
	    if (!destination.exists()) {
	        destination.mkdirs(); 
	    }
	    File fileToSave = new File(destination, imageName);
	    image.transferTo(fileToSave);
	}

	public void deleteProduct(int id) {
		productRepository.deleteById(id);
	}
	
	public int countProducts() {
		return (int) productRepository.count();
	}
	
}
