package com.sahadev.springbootin100.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.sahadev.springbootin100.entities.User;
import com.sahadev.springbootin100.exceptions.UserExistsException;
import com.sahadev.springbootin100.exceptions.UserNameNotFoundException;
import com.sahadev.springbootin100.exceptions.UserNotFoundException;
import com.sahadev.springbootin100.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
//////
@Api(tags="User management resful services",value="UserController",description="Controller for user management services")
@RestController
@Validated
@RequestMapping(value="/users")
public class UserController {
	@Autowired
	private UserService userService;
	 @ApiOperation(value="retrive list of user")
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public List<User> getAllUser(){
		return userService.getAllUser();
	}
      @ApiOperation(value="create a new user")
      @PostMapping
      public ResponseEntity<Void> createUser(@ApiParam("user information for a new user to be created")@Valid @RequestBody User user,UriComponentsBuilder builder) {
    	  try {
        	   userService.createUser(user);
        	   HttpHeaders headers=new HttpHeaders();
        	   headers.setLocation(builder.path("/users/{id}").buildAndExpand(user.getId()).toUri());
        	   return new ResponseEntity<Void>(headers,HttpStatus.CREATED);
                 		  
    	  }catch(UserExistsException ex) {
    		  throw new ResponseStatusException(HttpStatus.BAD_REQUEST,ex.getMessage());
    	  }
      }
     
      @GetMapping("/{id}")
      public User getUserById(@PathVariable("id")@Min(1) Long id){
    	  try {
        	  Optional<User> userOptional=userService.getUserById(id);
        	  return userOptional.get();
    	  }catch(UserNotFoundException ex) {
    		  throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
    	  }
    	  }
      @PutMapping("/{id}")
      public User updateUserById(@PathVariable("id")Long id,@RequestBody User user) {
    	  try {
        	  return userService.updateUserById(id, user);    		  
    	  }catch(UserNotFoundException ex) {
    		  throw new ResponseStatusException(HttpStatus.BAD_REQUEST,ex.getMessage());
    	  }
    	  
      }
      @DeleteMapping("/{id}")
      public void deleteUserById(@PathVariable("id")Long id) {
    	  userService.deleteUserById(id);
      }
      @GetMapping("/byusername/{username}")
      public User getUserByUsername(@PathVariable("username")String username) throws UserNameNotFoundException {
    	 User user= userService.getUserByUsername(username);
    	if(user==null) {
    		throw new UserNameNotFoundException("Username:'" +username+"' not found in repository");
    		
    	}
    	return user;
      }
}
