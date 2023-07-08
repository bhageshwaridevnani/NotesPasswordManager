package com.example.demo.Service;

import com.example.demo.DTO.SignUpDTO;
import com.example.demo.Enum.LoginType;
import com.example.demo.Model.EntityUser;
import com.example.demo.Repositroy.UserRepository;
import com.example.demo.Security.JwtUtil;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class Oauth2Service extends BaseService {
    public Oauth2Service(ModelMapper mapper) {
        super(mapper);
    }

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri-template}")
    private String redirectUriTemplate;

    @Value("${spring.security.oauth2.client.registration.google.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.registration.google.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.registration.google.user-info-uri}")
    private String userInfoUri;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public ModelAndView loginWithOauth2(HttpSession session) {
        //Generate a random and unique state value
        String state = UUID.randomUUID().toString();

        // Store the state value in the session
        session.setAttribute("oauth2_state", state);

        // Construct the authorization URL with the state parameter
        String authorizationUrl = authorizationUri
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUriTemplate
                + "&response_type=code"
                + "&scope=openid%20profile%20email"
                + "&state=" + state;
        return new ModelAndView("redirect:" + authorizationUrl); // Redirect to the OAuth2 provider's authorization URL
    }

    public ModelAndView oauth2Callback(HttpServletRequest request) {
        String authorizationCode = request.getParameter("code");

        // Retrieve the stored state value from the session
        HttpSession session = request.getSession();
        String storedState = (String) session.getAttribute("oauth2_state");

        // Retrieve the state value from the callback request
        String state = request.getParameter("state");

        // Compare the retrieved state value with the stored state value
        if (state == null || !state.equals(storedState)) {
            // Handle potential CSRF attack: state mismatch
            return new ModelAndView();
        }

        // Clear the stored state value from the session
        session.removeAttribute("oauth2_state");

        // Process the authorization code and continue with the authentication flow
        OAuth2AccessToken accessToken = getTokenResponse(authorizationCode);
        SignUpDTO signUpDTO = new SignUpDTO();
        if (accessToken != null) {
            signUpDTO = (SignUpDTO) getEmailFromAccessToken(accessToken);
        }
        String email = signUpDTO!=null&&signUpDTO.getEmail()!= null?signUpDTO.getEmail():null;
        String firstName = signUpDTO!=null&&signUpDTO.getFirstName()!= null?signUpDTO.getFirstName():null;
        String lastName = signUpDTO!=null&&signUpDTO.getFirstName()!= null?signUpDTO.getLastName():null;

        return new ModelAndView("redirect:http://localhost:9192/after/callback/?email=" + email +"&firstName=" + firstName + "&lastName=" + lastName);
    }

    private OAuth2AccessToken getTokenResponse(String authorizationCode) {
        MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
        tokenParams.add("grant_type", "authorization_code");
        tokenParams.add("code", authorizationCode);
        tokenParams.add("client_id", clientId);
        tokenParams.add("client_secret", clientSecret);
        tokenParams.add("redirect_uri", redirectUriTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(tokenParams, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            OAuth2AccessToken accessToken = null;
            if (responseBody != null) {
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                String accessTokenValue = jsonObject.get("access_token").getAsString();
                long expiresIn = jsonObject.get("expires_in").getAsLong();
                Instant issuedAt = jsonObject.has("iat") ? Instant.ofEpochSecond(jsonObject.get("iat").getAsLong()) : Instant.now();

                Set<String> scopes = new HashSet<>();

                accessToken = new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        accessTokenValue,
                        issuedAt,
                        Instant.now().plusSeconds(expiresIn),
                        scopes
                );
            }
            return accessToken;
        }

        return null;
    }

    private Object getEmailFromAccessToken(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> userInfo = responseEntity.getBody();
            SignUpDTO signUpDTO = new SignUpDTO();
            if (userInfo != null) {
                signUpDTO.setEmail((String) userInfo.get("email"));
                signUpDTO.setFirstName((String) userInfo.get("given_name"));
                signUpDTO.setLastName((String) userInfo.get("family_name"));
                System.out.println(signUpDTO.getEmail());
                return signUpDTO;
            }
        }
        return null;
    }
    public String generateToken(String email,String firstName,String lastName) {
        EntityUser entityUser = mongoTemplate.findOne(Query.query(Criteria.where(EntityUser.Fields.email).is(email)), EntityUser.class);
        if (entityUser == null) {
            entityUser = new EntityUser();
            entityUser.setEmail(email);
            entityUser.setFirstName(firstName);
            entityUser.setLastName(lastName);
            entityUser.setLoginType(LoginType.GOOGLE);
            userRepository.save(entityUser);
        }
        return jwtUtil.generateToken1(entityUser.getEmail(), null, entityUser.getUserId());
    }



}
