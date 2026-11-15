package com.github.xoaltie.tennisscoreboard.dto.response;

import java.util.List;

public record MatchPageResponseDto(List<MatchResponseDto> matches, int currentPage, int totalPages) {
}
