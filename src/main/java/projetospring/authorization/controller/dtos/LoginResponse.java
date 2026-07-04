package projetospring.authorization.controller.dtos;

public record LoginResponse(String acessToken, Long expiresIn) {

}
