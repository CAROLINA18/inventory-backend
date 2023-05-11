package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.util.util;


@Service
public class ProductServiceImpl implements IProductService {
	
	private ICategoryDao categoryDao;
	private IProductDao productDao;

	public ProductServiceImpl(ICategoryDao categoryDao, IProductDao productDao) {
		super();
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}


	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		try {
			Optional<Category> category = categoryDao.findById(categoryId);
			
			if(category.isPresent()) {
				product.setCategory(category.get());
			}else {
				response.setMetadata("respuesta ok", "-1", "Categoria encontrada asociada a producto");
				return new ResponseEntity<ProductResponseRest>(response , HttpStatus.NOT_FOUND);
			}
			
			Product productSaved= productDao.save(product);
			if(productSaved != null) {
				list.add(productSaved);
				response.getProduct().setProducts(list);
				response.setMetadata("respuesta ok", "00", "producto guardado");
			}else {
				response.setMetadata("respuesta ok", "-1", "producto no guardado");
				return new ResponseEntity<ProductResponseRest>(response , HttpStatus.BAD_REQUEST);
			}
			
		}catch(Exception e){
			e.getStackTrace();
			response.setMetadata("respuesta ok", "-1", "error al guardar");
			return new ResponseEntity<ProductResponseRest>(response , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response , HttpStatus.OK);
	}


	@Override
	@Transactional(readOnly =true)
	public ResponseEntity<ProductResponseRest> searchById(Long id) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			Optional<Product> product = productDao.findById(id);
			
			if(product.isPresent()) {
				
				byte[] imageDescompressed = util.decompressZLib(product.get().getPicture());
				product.get().setPicture(imageDescompressed);
				list.add(product.get());
				response.getProduct().setProducts(list);
				response.setMetadata("Respuesta ok", "00","Product Encontrado");
				
			}else {
				response.setMetadata("respuesta nok", "-1", "producto no encontrado");
				return new ResponseEntity<ProductResponseRest>(response , HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			e.getStackTrace();
			response.setMetadata("response nok", "-1","Error al guardar product");
			return new ResponseEntity<ProductResponseRest>(response , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response , HttpStatus.OK);

	}


	@Override
	@Transactional(readOnly =true)
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAuxx = new ArrayList<>();
		
		try {
			
			listAuxx = productDao.findByNameContainingIgnoreCase(name);
			
			if(listAuxx.size()> 0) {
				
				listAuxx.stream().forEach((p)->{
					byte[] imageDescompressed = util.decompressZLib(p.getPicture());
					p.setPicture(imageDescompressed);
					list.add(p);
				});
				
				response.getProduct().setProducts(list);
				response.setMetadata("Respuesta ok", "00","Products Encontrado");
				
			}else {
				response.setMetadata("respuesta nok", "-1", "producto no encontrado");
				return new ResponseEntity<ProductResponseRest>(response , HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			e.getStackTrace();
			response.setMetadata("response nok", "-1","Error al buscar product");
			return new ResponseEntity<ProductResponseRest>(response , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response , HttpStatus.OK);
	}


	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> deleteById(Long id) {
		ProductResponseRest response = new ProductResponseRest();
		
		
		try {
			 productDao.deleteById(id);
			 response.setMetadata("Respuesta ok", "00","Product eliminado");
			
		}catch(Exception e) {
			e.getStackTrace();
			response.setMetadata("response nok", "-1","Error al eliminar product");
			return new ResponseEntity<ProductResponseRest>(response , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<ProductResponseRest>(response , HttpStatus.OK);
	}

}
