package kapyrin.myshopspring.controller.product;

import kapyrin.myshopspring.entity.Product;
import kapyrin.myshopspring.exception.entity.ProductException;
import kapyrin.myshopspring.service.impl.ProductImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductImplService productService;

    @GetMapping("/products")
    public String showProducts(Model model) {
        model.addAttribute("products", productService.getAll());
        return "product/productManagement";
    }

    @GetMapping("/addProduct")
    public String showAddProductForm() {
        return "product/addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam("productName") String productName,
                             @RequestParam("productDescription") String productDescription,
                             @RequestParam("productPrice") Double productPrice,
                             @RequestParam("productQuantity") int productQuantity) {

        Product product = Product.builder()
                .productName(productName)
                .productDescription(productDescription)
                .price(productPrice)
                .productRemain(productQuantity)
                .build();
        productService.add(product);
        return "redirect:/products";
    }

    @GetMapping("/editProduct")
    public String showEditProductForm(@RequestParam("id") Long productId, Model model) {
        Optional<Product> product = productService.getById(productId);
        product.ifPresent(value -> model.addAttribute("product", value));
        return product.isPresent() ? "product/editProduct" : "redirect:/products";
    }

    @PostMapping("/editProduct")
    public String editProduct(@RequestParam("productId") Long productId,
                              @RequestParam("productName") String productName,
                              @RequestParam("productDescription") String productDescription,
                              @RequestParam("productPrice") Double productPrice,
                              @RequestParam("productRemain") int productRemain) {

        Optional<Product> optionalProduct = productService.getById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setProductName(productName);
            product.setProductDescription(productDescription);
            product.setPrice(productPrice);
            product.setProductRemain(productRemain);
            productService.update(product);
        }
        return "redirect:/products";
    }

    @PostMapping("/products")
    public String deleteProduct(@RequestParam("action") String action,
                                @RequestParam("productId") Long productId, Model model) {
        if ("delete".equals(action)) {
            try {
                productService.deleteById(productId);
            } catch (ProductException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return showProducts(model);
            }
        }
        return "redirect:/products";
    }
}