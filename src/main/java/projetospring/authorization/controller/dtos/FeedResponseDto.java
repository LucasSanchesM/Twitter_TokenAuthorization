package projetospring.authorization.controller.dtos;

import java.util.List;

public record FeedResponseDto(List<FeedItemDto> feedItem, int page, int pageSize, int totalPages, Long TotalElements) {

}
