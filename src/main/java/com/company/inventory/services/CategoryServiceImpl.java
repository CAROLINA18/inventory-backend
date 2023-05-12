package com.company.inventory.services;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;

@Service
public class CategoryServiceImpl  implements ICategoryService{
	
	@Autowired
	private ICategoryDao categoryDao;
	

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> search() {
		// TODO Auto-generated method stub
		CategoryResponseRest response  = new CategoryResponseRest();
		try {
			List<Category> category = (List<Category>) categoryDao.findAll();
			response.getCategoryResponse().setCategory(category);
			response.setMetadata("Respuesta AN", "00", "Respuesta Exitosa AN");
			
		}catch(Exception e) {
			response.setMetadata("Respuesta No Corrects", "-1", "Error exception");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.OK);
		
	}


	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {
		
		CategoryResponseRest response  = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			Optional<Category> category = categoryDao.findById(id);
			if(category.isPresent()) {
				list.add(category.get());
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("Respuesta ok", "00", "categoria encontrada");
			}else {
				response.setMetadata("Respuesta No Corrects", "-1", "No se encontro nada");
				return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.NOT_FOUND);
			}
			
		}catch(Exception e) {
			response.setMetadata("Respuesta No Corrects", "-1", "Error exception");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.OK);
		
	}


	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> save(Category category) {
		
		CategoryResponseRest response  = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			Category categorySaved= categoryDao.save(category);
			
			if(categorySaved != null) {
				list.add(categorySaved);
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("Respuesta ok", "00", "categoria guardada ");
			}else {
				response.setMetadata("Respuesta No Corrects", "-1", "Error Guardando ");
				return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.BAD_REQUEST);
			}
			
		}catch(Exception e) {
			response.setMetadata("Respuesta No Corrects", "-1", "Error grabando");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.OK);
		
	}


	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> update(Category category, Long id) {
		
		CategoryResponseRest response  = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			
			Optional<Category> categorySearch = categoryDao.findById(id);
			
			if(categorySearch.isPresent()) {
				//se procede a actualizar el item
				categorySearch.get().setName(category.getName());
				categorySearch.get().setDescription(category.getDescription());
				
				Category categoryToUpdate = categoryDao.save(categorySearch.get());
				
				if(categoryToUpdate != null) {
					list.add(categoryToUpdate);
					response.getCategoryResponse().setCategory(list);
					response.setMetadata("Respuesta ok", "00", "categoria actualizada ");
				}else {
					response.setMetadata("Respuesta No Corrects", "-1", "Error actualizando ");
					return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.BAD_REQUEST);
				}
				
			}else {
				response.setMetadata("Respuesta No Corrects", "-1", "No se encontro nada");
				return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.NOT_FOUND);
			}
			
			
		}catch(Exception e) {
			response.setMetadata("Respuesta No Corrects", "-1", "Error actualizando");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.OK);
	
		
	}


	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> deleteById(Long id) {
		
		CategoryResponseRest response  = new CategoryResponseRest();
		try {
			categoryDao.deleteById(id);
			response.setMetadata("respuesta ok", "00", "Registro eliminado");
			
		}catch(Exception e) {
			response.setMetadata("Respuesta No Corrects", "-1", "Error eliminando");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response , HttpStatus.OK);
		
		
	}

}
