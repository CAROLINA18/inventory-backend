package com.company.inventory.services;



import java.util.List;

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

}
