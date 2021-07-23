package com.ndrmf.config.documentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ndrmf.config.security.LoginRequest;
import com.ndrmf.config.security.LoginResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Authentication API specification for Swagger documentation and Code Generation.
 * Implemented by Spring Security.
 */

@Api(tags = "Authentication")
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthApiSpec {
	/**
     * Implemented by Spring Security
     */
    @ApiOperation(value = "Login", notes = "Login with the given credentials.")
    @ApiResponses({@ApiResponse(code = 200, message = "", response = LoginResponse.class)})
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest body) {
        
    	return new ResponseEntity<LoginResponse>(new LoginResponse(), HttpStatus.OK);
    }
}
